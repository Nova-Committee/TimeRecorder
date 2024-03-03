package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameRules;

public class HelpCommand {
    private static final String opHelpMsg = """
                §r§b§lMcStats 帮助信息§r
                §r/mcstats help §f- §7显示此帮助信息§r
                §r/mcstats report §f- §7显示当日截止目前的统计信息§r
                §r/mcstats reportQQ §f- §7显示和发送当日截止目前的统计信息到QQ§r
                §r/mcstats reportAll §f- §7显示当日截止目前的所有玩家的统计信息§r
                §r/mcstats connect §f- §7连接到QQ机器人§r
                """;

    private static final String memberHelpMsg = """
                §r§b§lMcStats 帮助信息§r
                §r/mcstats help §f- §7显示此帮助信息§r
                §r/mcstats report §f- §7显示当日截止目前的统计信息§r
                """;
    public static int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().hasPermission(2)) {
            context.getSource().sendSuccess(() -> Component.literal(opHelpMsg), false);
        } else {
            final boolean tmpRule = context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).get();
            context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(true, context.getSource().getServer());
            context.getSource().sendSuccess(() -> Component.literal(memberHelpMsg), false);
            context.getSource().getLevel().getGameRules().getRule(GameRules.RULE_SENDCOMMANDFEEDBACK).set(tmpRule, context.getSource().getServer());
        }

        return 1;
    }
}
