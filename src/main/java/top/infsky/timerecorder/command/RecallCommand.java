package top.infsky.timerecorder.command;

import cn.evole.mods.mcbot.Const;
import cn.evole.onebot.sdk.enums.ActionType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.log.LogUtils;

import java.util.Objects;

public class RecallCommand {
    public static int execute(@NotNull CommandContext<CommandSourceStack> context) {
        int messageId;
        try {
            messageId = Objects.requireNonNull(Utils.getPlayer(Objects.requireNonNull(context.getSource().getPlayer()).getUUID())).getMessageSent().removeFirst();
        } catch (NullPointerException e) {
            LogUtils.LOGGER.error("尝试撤回消息时失败", e);
            context.getSource().sendFailure(Component.literal("尝试撤回消息时失败。"));
            return -1;
        }
        JsonObject json = new Gson().fromJson(
                String.format("{'message_id': %s}", messageId),
                JsonObject.class);
        Const.customRequest(ActionType.DELETE_MSG, json);
        return 1;
    }
}
