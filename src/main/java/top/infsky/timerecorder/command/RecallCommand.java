package top.infsky.timerecorder.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.compat.McBotSupport;
import top.infsky.timerecorder.data.MessageObject;
import top.infsky.timerecorder.log.LogUtils;

import java.util.NoSuchElementException;
import java.util.Objects;

public class RecallCommand {
    public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
        MessageObject messageObject;
        try {
            messageObject = Objects.requireNonNull(Utils.getPlayer(Objects.requireNonNull(context.getSource().getPlayer()).getUUID())).getMessageSent().removeLast();
        } catch (NullPointerException | NoSuchElementException ignored) {
            LogUtils.LOGGER.error("尝试撤回消息时失败");
            context.getSource().sendSystemMessage(Component.literal("无法撤回消息").withStyle(ChatFormatting.DARK_RED));
            return 1;
        }

        context.getSource().sendSystemMessage(
                Component.literal("撤回消息 ").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(messageObject.message()).withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(" ？ ").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal("[确认]").withStyle(ChatFormatting.GREEN).withStyle(ChatFormatting.BOLD)
                                .withStyle(style -> style.withHoverEvent(
                                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("确认撤回"))
                                ))
                                .withStyle(style -> style.withClickEvent(
                                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tr recall confirm %s", messageObject.messageId()))
                                )))
        );
        return 1;
    }

    public static class Confirm {
        public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
            final int messageId = IntegerArgumentType.getInteger(context, "message_id");

            //TODO 消息合法性验证(检查发送方)

            doRecall(messageId);
            context.getSource().sendSystemMessage(Component.literal("撤回成功。").withStyle(ChatFormatting.GREEN));
            return 1;
        }
    }

    public static void doRecall(int message_id) {
        McBotSupport.recallMessage(message_id);
        LogUtils.LOGGER.info("撤回消息: {}", message_id);
    }
}
