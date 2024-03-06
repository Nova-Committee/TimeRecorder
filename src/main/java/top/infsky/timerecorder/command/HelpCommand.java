package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameRules;
import top.infsky.timerecorder.Utils;

public class HelpCommand {
    private static final String opHelpMsg = """
                §r§b§lTimeRecorder 帮助信息§r
                §r/tr help §f- §7显示此帮助信息§r
                §r/tr report §f- §7显示当日截止目前的统计信息§r
                §r/tr reportQQ §f- §7显示和发送当日截止目前的统计信息到QQ§r
                §r/tr reportAll §f- §7显示当日截止目前的所有玩家的统计信息§r
                §r/tr connect §f- §7连接到QQ机器人§r
                §r/tr reload §f- §7从配置文件重载§r
                §r/tr dump §f- §7保存/还原统计信息§r
                """;

    private static final String memberHelpMsg = """
                §r§b§lTimeRecorder 帮助信息§r
                §r/tr help §f- §7显示此帮助信息§r
                §r/tr report §f- §7显示当日截止目前的统计信息§r
                """;
    public static int execute(CommandContext<CommandSourceStack> context) {
        if (Utils.isClient()) {
            context.getSource().sendSuccess(() -> Component.literal(opHelpMsg), false);
        }

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
