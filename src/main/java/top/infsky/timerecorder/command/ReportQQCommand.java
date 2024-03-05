package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.timerecorder.TimeRecorder;
import top.infsky.timerecorder.log.LogUtils;
import top.infsky.timerecorder.mcbot.McBot;

public class ReportQQCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令输出报告 reportQQ");
        if (context.getSource().isPlayer())
            context.getSource().sendSuccess(() -> Component.literal("将当日截止目前的统计信息发送到QQ"), true);
        McBot.sendGroupMsg(TimeRecorder.getStatsData().getReport());
        return 1;
    }
}
