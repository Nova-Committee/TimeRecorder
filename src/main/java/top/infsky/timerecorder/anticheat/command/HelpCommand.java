package top.infsky.timerecorder.anticheat.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class HelpCommand {
    private static final String opHelpMsg = """
                §r§b§lTimeRecorder Anti-Cheat§r
                §r/trac help §f- §7显示此帮助信息§r
                §r/trac alert §f- §7显示警报信息§r
                """;

    private static final String memberHelpMsg = """
                §r§b§lTimeRecorder Anti-Cheat§r
                §r/tr help §f- §7显示此帮助信息§r
                """;

    public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal(
                context.getSource().hasPermission(2) ? opHelpMsg : memberHelpMsg
        ));

        return 1;
    }
}
