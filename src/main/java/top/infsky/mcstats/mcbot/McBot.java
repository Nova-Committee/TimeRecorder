package top.infsky.mcstats.mcbot;

import cn.evole.onebot.client.connection.ConnectFactory;
import cn.evole.onebot.client.core.Bot;
import cn.evole.onebot.sdk.util.BotUtils;
import top.infsky.mcstats.config.ModConfig;
import top.infsky.mcstats.log.LogUtils;

import java.util.concurrent.LinkedBlockingQueue;

public class McBot {
    public static LinkedBlockingQueue<String> blockingQueue;
    public static ConnectFactory service;
    public static Bot bot;
    public static Thread app;
    public static MessageThread messageThread;

    static {
        blockingQueue = new LinkedBlockingQueue<>();//使用队列传输数据
        try {
            app = new Thread(() -> {
                service = new ConnectFactory(ModConfig.INSTANCE.getBotConfig().toBot(), blockingQueue);//创建websocket连接
                bot = service.ws.createBot();//创建机器人实例
            }, "BotServer");
            app.start();
            LogUtils.LOGGER.info("QQ机器人已连接");
        } catch (Exception e) {
            LogUtils.LOGGER.error("▌ §c机器人服务端未配置或未打开");
        }
        messageThread = new MessageThread();  // 创建消息处理线程池
    }

    public static void sendGroupMsg(String message){
        for (long id : ModConfig.INSTANCE.getCommon().getGroupIdList()){
            groupMsg(id, message);
        }
    }

    private static void groupMsg(long id, String message){
        // 发送消息时实际上所调用的函数。
        if (ModConfig.INSTANCE.getBotConfig().getMsgType().equalsIgnoreCase("string")){
            McBot.messageThread.submit(id, message, false);
        }
        else {
            McBot.messageThread.submit(id, BotUtils.rawToJson(message), false);
        }
    }

    public static void stop() {
        service.stop();
        app.interrupt();
        messageThread.stop();
    }

    public static void killOutThreads() {
        try {
            service.stop();
            app.interrupt();
        } catch (Exception ignored) {
        }
    }
}
