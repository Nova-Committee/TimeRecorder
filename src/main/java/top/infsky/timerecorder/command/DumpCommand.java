package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.data.StatsDump;
import top.infsky.timerecorder.log.LogUtils;

public class DumpCommand {
    public static class Save {
        public static int execute(CommandContext<CommandSourceStack> context) {
            LogUtils.LOGGER.info("通过指令保存统计数据");

            if (StatsDump.save(StatsDump.getDump(Utils.getStatsData())) == 1) {
                context.getSource().sendSuccess(() -> Component.literal("统计数据已保存。").withStyle(ChatFormatting.GREEN), true);
                return 1;
            }
            context.getSource().sendSuccess(() -> Component.literal("统计数据保存失败").withStyle(ChatFormatting.RED), true);
            return 1;
        }
    }

    public static class Load {
        public static int execute(CommandContext<CommandSourceStack> context) {
            LogUtils.LOGGER.info("通过指令加载统计数据");

            try {
                if (StatsDump.load() == 1) {
                    context.getSource().sendSuccess(() -> Component.literal("成功还原统计数据。").withStyle(ChatFormatting.GREEN), true);
                    return 1;
                }
            } catch (RuntimeException e) {
                context.getSource().sendFailure(Component.literal(e.getMessage()));
                return -1;
            }
            context.getSource().sendSuccess(() -> Component.literal("统计数据还原失败").withStyle(ChatFormatting.RED), true);
            return 1;
        }
    }
}
