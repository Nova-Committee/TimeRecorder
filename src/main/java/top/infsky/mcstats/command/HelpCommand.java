package top.infsky.mcstats.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.val;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Objects;

public class HelpCommand {
    private static final String opHelpMsg = """
                §r§b§lMcStats 帮助信息§r
                §r/mcstats help §f- §7显示此帮助信息§r
                §r/mcstats report §f- §7显示当日截止目前的统计信息§r
                §r/mcstats reportQQ §f- §7显示和发送当日截止目前的统计信息到QQ§r
                §r/mcstats reportAll §f- §7显示当日截止目前的所有玩家的统计信息§r
                §r/mcstats connect §f- §7连接到QQ机器人§r
                """;

    private static final String memberHelpMsg = """
                §r§b§lMcStats 帮助信息§r
                §r/mcstats help §f- §7显示此帮助信息§r
                §r/mcstats report §f- §7显示当日截止目前的统计信息§r
                """;
    public static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        context.getSource().sendSuccess(() -> Component.literal(context.getSource().hasPermission(2) ? opHelpMsg : memberHelpMsg), true);
        return 1;
    }
}
