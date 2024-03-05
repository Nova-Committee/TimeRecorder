package top.infsky.timerecorder.mcbot;

import com.google.gson.JsonArray;
import top.infsky.timerecorder.log.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageThread {
    private final ExecutorService executor;
    public MessageThread() {
        executor = Executors.newCachedThreadPool();
    }

    public void submit(long groupId, String msg, boolean autoEscape) {
        LogUtils.LOGGER.debug(String.format("向 QQ 发送消息: %s", msg));
        executor.submit(() -> McBot.bot.sendGroupMsg(groupId, msg, autoEscape));
    }

    public void submit(long groupId, JsonArray msg, boolean autoEscape) {
        LogUtils.LOGGER.debug(String.format("向 QQ 发送消息: %s", msg));
        executor.submit(() -> McBot.bot.sendGroupMsg(groupId, msg, autoEscape));
    }

    public void stop() {
        executor.shutdown();
    }
}
