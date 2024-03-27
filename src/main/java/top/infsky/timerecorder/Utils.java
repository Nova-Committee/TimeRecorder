package top.infsky.timerecorder;

import cn.evole.mods.mcbot.init.callbacks.IEvents;
import lombok.Getter;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import top.infsky.timerecorder.data.PlayerData;
import top.infsky.timerecorder.data.StatsData;
import top.infsky.timerecorder.log.LogUtils;

import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

public class Utils {
    @Getter
    public static final String MOD_ID = "timerecorder";
    @Getter
    @Nullable
    public static MinecraftServer SERVER = null;
    public static Path CONFIG_FOLDER;
    public static Path CONFIG_FILE;

    @Getter
    public static StatsData statsData;

    public static @Nullable PlayerData getPlayer(UUID uuid) throws NullPointerException {
        return Utils.getStatsData().getPlayerDataMap().get(uuid);
    }

    public static void sendChatAs(ServerPlayer player, String message) {
        if (getSERVER() != null) {
            getSERVER().getPlayerList().broadcastChatMessage(
                    PlayerChatMessage.unsigned(player.getUUID(), message),
                    player,
                    ChatType.bind(ChatType.CHAT, player));
            IEvents.SERVER_CHAT.invoker().onChat(player, message);  // TODO [包含尚未解决的问题] 很奇怪，上面一条指令应该就能触发McBot的mixin的...但是并没有。
        }
    }
}
