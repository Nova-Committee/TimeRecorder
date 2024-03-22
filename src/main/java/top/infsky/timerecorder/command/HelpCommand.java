package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class HelpCommand {
    private static final String opHelpMsg = """
                §r§b§lTimeRecorder 帮助信息§r
                §r/tr help §f- §7显示此帮助信息§r
                §r/tr report §f- §7显示当日服务器日报§r
                §r/tr recall §f- §7撤回上一条消息§r
                §r/tr reportQQ §f- §7显示和发送当日服务器日报到QQ§r
                §r/tr reportAll §f- §7显示当日所有玩家的统计信息§r
                §r/tr reload §f- §7重载配置文件§r
                §r/tr dump save §f- §7保存统计信息§r
                §r/tr dump load §8[<filename>] §f- §7还原统计信息§r
                """;

    private static final String memberHelpMsg = """
                §r§b§lTimeRecorder 帮助信息§r
                §r/tr help §f- §7显示此帮助信息§r
                §r/tr report §f- §7显示当日服务器日报§r
                §r/tr recall §f- §7撤回上一条消息§r
                """;
    public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
        context.getSource().sendSystemMessage(Component.literal(
                context.getSource().hasPermission(2) ? opHelpMsg : memberHelpMsg
        ));

        return 1;
    }
}
