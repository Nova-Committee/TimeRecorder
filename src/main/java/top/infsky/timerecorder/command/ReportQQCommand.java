package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import top.infsky.timerecorder.TimeRecorder;
import top.infsky.timerecorder.log.LogUtils;
import top.infsky.timerecorder.mcbot.McBot;

public class ReportQQCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令输出报告 reportQQ");
        if (context.getSource().getEntity() instanceof Player)
            context.getSource().sendSuccess(new TextComponent("将当日截止目前的统计信息发送到QQ"), true);
        McBot.sendGroupMsg(TimeRecorder.getStatsData().getReport());
        return 1;
    }
}
