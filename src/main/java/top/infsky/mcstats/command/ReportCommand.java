package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.log.LogUtils;

public class ReportCommand {
    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LogUtils.LOGGER.info("通过指令输出报告 report");
        context.getSource().sendSystemMessage(Component.literal(McStats.getStatsData().getReport()));
        return 1;
    }
}
