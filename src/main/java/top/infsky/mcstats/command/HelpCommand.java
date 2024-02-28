package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class HelpCommand {
    public static int execute(CommandContext<CommandSourceStack> context) {
        String helpMsg = """
                §r§k§bMcStats 帮助信息
                §r/mcstats help §f- §7显示此帮助信息
                §r/mcstats report §f- §7显示当日截止目前的统计信息
                §r/mcstats reportQQ §f- §7显示和发送当日截止目前的统计信息到QQ
                §r/mcstats reportAll §f- §7显示当日截止目前的所有玩家的统计信息
                §r/mcstats connect §f- §7连接到QQ机器人
                """;
        context.getSource().sendSuccess(() -> Component.literal(helpMsg), true);
        return 1;
    }
}
