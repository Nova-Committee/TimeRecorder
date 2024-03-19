package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.log.LogUtils;

import java.util.Arrays;

public class ReloadCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令重载配置文件");
        try {
            doReload();
        } catch (Exception e) {
            LogUtils.LOGGER.error("在重载配置文件时发生异常");
            LogUtils.LOGGER.error(Arrays.toString(e.getStackTrace()));
            context.getSource().sendSuccess(() -> Component.literal("在重载配置文件时发生异常，请检查日志").withStyle(ChatFormatting.RED), true);
            return 0;
        }
        context.getSource().sendSuccess(() -> Component.literal("重载配置文件！"), true);
        return 1;
    }

    public static void doReload() throws Exception {
        Utils.getStatsData().init();
        ModConfig.INSTANCE.save();
    }
}
