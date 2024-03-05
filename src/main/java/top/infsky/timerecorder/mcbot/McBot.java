package top.infsky.timerecorder.mcbot;

import cn.evole.onebot.client.connection.ConnectFactory;
import cn.evole.onebot.client.core.Bot;
import cn.evole.onebot.sdk.util.BotUtils;
import com.google.gson.JsonObject;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.log.LogUtils;

import java.util.concurrent.LinkedBlockingQueue;

public class McBot {
    public static LinkedBlockingQueue<JsonObject> blockingQueue;
    public static ConnectFactory service;
    public static Bot bot;
    public static MessageThread messageThread;

    static {
        blockingQueue = new LinkedBlockingQueue<>();//使用队列传输数据
        messageThread = new MessageThread();  // 创建消息处理线程池
    }

    /**
     * 连接QQ机器人
     * @return true -> 成功 | false -> 失败
     */
    public static boolean init() {
        LogUtils.LOGGER.info("连接QQ机器人");
        try {
            service = new ConnectFactory(ModConfig.INSTANCE.getBotConfig().toBot(), blockingQueue);//创建websocket连接
            bot = service.getBot();//创建机器人实例
            LogUtils.LOGGER.info("QQ机器人已连接");
            return true;
        } catch (Exception e) {
            LogUtils.LOGGER.error("▌ §c机器人服务端未配置或未打开");
        }
        return false;
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
        messageThread.stop();
    }

    public static void killOutThreads() {
        try {
            service.stop();
        } catch (Exception ignored) {
        }
    }
}
