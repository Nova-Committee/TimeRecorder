package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.log.LogUtils;

public class ReportAllCommand {
    public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令输出报告");
        context.getSource().sendSuccess(() -> Component.literal(Utils.getStatsData().getFullReport()), true);
        return 1;
    }
}
