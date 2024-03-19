package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.log.LogUtils;
import top.infsky.timerecorder.compat.McBotSupport;

public class ReportQQCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        LogUtils.LOGGER.info("通过指令输出报告 reportQQ");
        McBotSupport.sendAllGroupMsg(Utils.getStatsData().getReport());
        if (context.getSource().isPlayer())
            context.getSource().sendSuccess(() -> Component.literal("成功发送统计信息。").withStyle(ChatFormatting.GREEN), true);
        return 1;
    }
}
