package top.infsky.timerecorder.anticheat.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.config.ModConfig;

import java.util.Objects;

import static top.infsky.timerecorder.anticheat.TRPlayer.CONFIG;

public class AlertCommand {
    public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
        if (context.getSource().isPlayer()) {
            final String uuid = Objects.requireNonNull(context.getSource().getPlayer()).getStringUUID();
            if (CONFIG().getAllowAlertPlayers().contains(uuid)) {
                CONFIG().getAllowAlertPlayers().remove(uuid);
                context.getSource().sendSystemMessage(Component.literal("警报已关闭").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD));
            } else {
                CONFIG().getAllowAlertPlayers().add(uuid);
                context.getSource().sendSystemMessage(Component.literal("警报已开启").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD));
            }
            ModConfig.INSTANCE.save();
        }
        return 1;
    }
}
