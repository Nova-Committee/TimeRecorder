package top.infsky.mcstats.command;

import cn.evole.onebot.client.connection.ConnectFactory;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.mcstats.config.ModConfig;
import top.infsky.mcstats.log.LogUtils;
import top.infsky.mcstats.mcbot.McBot;

public class ConnectCommand {
    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LogUtils.LOGGER.info("连接QQ机器人");
        context.getSource().sendSuccess(() -> Component.literal("正在尝试连接QQ机器人"), true);
        try {
            McBot.app = new Thread(() -> {
                McBot.service = new ConnectFactory(ModConfig.INSTANCE.getBotConfig().toBot(), McBot.blockingQueue);//创建websocket连接
                McBot.bot = McBot.service.ws.createBot();//创建机器人实例
            }, "BotServer");
            McBot.app.start();
        } catch (Exception e) {
            LogUtils.LOGGER.error("§c机器人服务端配置不正确");
        }
        return 1;
    }
}
