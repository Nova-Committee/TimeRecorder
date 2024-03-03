package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameRules;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.log.LogUtils;

public class ReportCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令输出报告 report");
        final boolean tmpRule = context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).get();
        context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(true, context.getSource().getServer());
        context.getSource().sendSuccess(() -> Component.literal(McStats.getStatsData().getReport()), false);
        context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(tmpRule, context.getSource().getServer());
        return 1;
    }
}
