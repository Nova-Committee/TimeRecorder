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
            context.getSource().sendSuccess(() -> Component.literal("正在保存统计数据..."), true);

            return StatsDump.save(StatsDump.getDump(McStats.getStatsData()));
        }
    }

    public static class Load {
        public static int execute(CommandContext<CommandSourceStack> context) {
            LogUtils.LOGGER.info("通过指令加载统计数据");
            context.getSource().sendSuccess(() -> Component.literal("正在加载统计数据..."), true);

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
