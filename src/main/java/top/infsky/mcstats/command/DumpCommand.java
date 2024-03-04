package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.data.StatsDump;
import top.infsky.mcstats.log.LogUtils;

public class DumpCommand {
    public static class Save {
        public static int execute(CommandContext<CommandSourceStack> context) {
            LogUtils.LOGGER.info("通过指令保存统计数据");

            if (StatsDump.save(StatsDump.getDump(McStats.getStatsData())) == 1) {
                context.getSource().sendSuccess(() -> Component.literal("统计数据已保存。"), true);
                return 1;
            }
            return -1;
        }
    }

    public static class Load {
        public static int execute(CommandContext<CommandSourceStack> context) {
            LogUtils.LOGGER.info("通过指令加载统计数据");

            try {
                StatsDump.load();
                context.getSource().sendSuccess(() -> Component.literal("成功还原统计数据。"), true);
            } catch (RuntimeException e) {
                context.getSource().sendFailure(Component.literal(e.getMessage()));
                return -1;
            }
            return 1;
        }
    }
}
