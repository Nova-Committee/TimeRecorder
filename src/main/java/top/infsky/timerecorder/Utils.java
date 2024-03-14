package top.infsky.timerecorder;

import lombok.Getter;
import net.minecraft.server.MinecraftServer;
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

    public static @Nullable PlayerData getPlayer(UUID uuid){
        try {
            return Objects.requireNonNull(Utils.getStatsData().getPlayerDataMap().get(uuid));
        } catch (NullPointerException e) {
            LogUtils.LOGGER.error("获取PlayerData时失败", e);
            return null;
        }
    }
}
