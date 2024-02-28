package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.log.LogUtils;

public class ReportQQCommand {
    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LogUtils.LOGGER.info("通过指令输出报告 reportQQ");
        McStats.getStatsData().report();
        return 1;
    }
}
