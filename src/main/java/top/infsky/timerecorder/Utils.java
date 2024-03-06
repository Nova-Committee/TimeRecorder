package top.infsky.timerecorder;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import top.infsky.timerecorder.data.StatsData;

import java.nio.file.Path;

public class Utils {
    @Getter
    public static final String MOD_ID = "timerecorder";
    @Getter
    @Nullable
    public static MinecraftServer SERVER = null;
    @Getter
    @Nullable
    public static Minecraft CLIENT = null;
    public static boolean ClientPlaying = false;
    public static Path CONFIG_FOLDER;
    public static Path CONFIG_FILE;

    @Getter
    public static StatsData statsData;

    public static boolean isClient() {
        return getCLIENT() != null;
    }

    public static boolean isClientPlaying() {
        if (isClient()) return ClientPlaying;
        return false;
    }
}
