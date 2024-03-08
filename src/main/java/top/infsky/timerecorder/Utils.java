package top.infsky.timerecorder;

import lombok.Getter;
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
    public static Path CONFIG_FOLDER;
    public static Path CONFIG_FILE;

    @Getter
    public static StatsData statsData;

}
