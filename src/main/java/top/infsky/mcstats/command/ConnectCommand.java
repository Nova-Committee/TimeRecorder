package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.mcstats.mcbot.McBot;

public class ConnectCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal("正在尝试连接QQ机器人"), true);
        if (McBot.init()) {
            context.getSource().sendSuccess(() -> Component.literal("连接成功"), true);
            return 1;
        } else {
            context.getSource().sendSuccess(() -> Component.literal("连接失败，请检查日志"), true);
        }
        return -1;
    }
}
