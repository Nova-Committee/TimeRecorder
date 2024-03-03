package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.log.LogUtils;
import top.infsky.mcstats.mcbot.McBot;

public class ReportQQCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令输出报告 reportQQ");
        context.getSource().sendSuccess(() -> Component.literal("将当日截止目前的统计信息发送到QQ"), true);
        McBot.sendGroupMsg(McStats.getStatsData().getReport());
        return 1;
    }
}
