package top.infsky.timerecorder;

import cn.evole.onebot.sdk.util.FileUtils;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import top.infsky.timerecorder.command.ICmdEvent;
import top.infsky.timerecorder.data.StatsData;
import top.infsky.timerecorder.data.StatsDump;
import top.infsky.timerecorder.mcbot.McBot;

import java.nio.file.Path;

public class TimeRecorder implements ModInitializer {
    @Getter
    public static final String MOD_ID = "timerecorder";
    @Getter
    public static MinecraftServer SERVER = null;
    public static Path CONFIG_FOLDER;
    public static Path CONFIG_FILE;

    @Getter
    public static StatsData statsData;
    @Override
    public void onInitialize() {
        init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ICmdEvent.register(dispatcher));

        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);

    }

    public void init() {
        CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);
        FileUtils.checkFolder(CONFIG_FOLDER);
        CONFIG_FILE = CONFIG_FOLDER.resolve("config.toml");
        Runtime.getRuntime().addShutdownHook(new Thread(McBot::killOutThreads));
    }

    public void onServerStarting(MinecraftServer server) {
        SERVER = server;
    }

    public void onServerStarted(MinecraftServer server) {
        statsData = new StatsData();

        McBot.init();
        ServerTickEvents.END_SERVER_TICK.register(statsData::update);
    }

    public void onServerStopping(MinecraftServer server) {
        StatsDump.save(StatsDump.getDump(getStatsData()));
        McBot.stop();
    }
    public void onServerStopped(MinecraftServer server) {
        McBot.killOutThreads();
    }
}
