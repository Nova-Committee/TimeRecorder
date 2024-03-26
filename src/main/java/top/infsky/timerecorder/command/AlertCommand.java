package top.infsky.timerecorder.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.config.ModConfig;

import java.util.Objects;

public class AlertCommand {
    public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
        if (context.getSource().isPlayer()) {
            ModConfig.INSTANCE.getAntiCheat().getAllowAlertPlayers().add(Objects.requireNonNull(context.getSource().getPlayer()).getStringUUID());
            ModConfig.INSTANCE.save();
            context.getSource().sendSystemMessage(Component.literal("警报已开启").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD));
        }
        return 1;
    }
}
