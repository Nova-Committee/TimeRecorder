package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.log.LogUtils;

import java.util.Arrays;

public class ReloadCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令重载配置文件");
        try {
            McStats.getStatsData().init();
        } catch (Exception e) {
            LogUtils.LOGGER.error("在重载配置文件时发生异常");
            LogUtils.LOGGER.error(Arrays.toString(e.getStackTrace()));
            context.getSource().sendFailure(Component.literal("在重载配置文件时发生异常，请检查日志"));
            return -1;
        }
        context.getSource().sendSuccess(() -> Component.literal("重载配置文件！"), true);
        return 1;
    }
}
