package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.log.LogUtils;

public class ReportCommand {
    public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令输出报告 report");
//        final boolean tmpRule = context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).get();
//        context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(true, context.getSource().getServer());
//        context.getSource().sendSuccess(() -> Component.literal(Utils.getStatsData().getReport()), false);
//        context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(tmpRule, context.getSource().getServer());
        context.getSource().sendSystemMessage(Component.literal(Utils.getStatsData().getReport()));
        return 1;
    }
}
