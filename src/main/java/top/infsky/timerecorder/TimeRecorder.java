package top.infsky.timerecorder;

import cn.evole.mods.mcbot.api.McBotEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import top.infsky.timerecorder.config.ICmdEvent;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.data.StatsData;
import top.infsky.timerecorder.data.StatsDump;

import java.io.IOException;
import java.nio.file.Files;

public class TimeRecorder implements ModInitializer {
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
        Utils.CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(Utils.MOD_ID);
        if (!Utils.CONFIG_FOLDER.toFile().isDirectory()) {
            try {
                Files.createDirectories(Utils.CONFIG_FOLDER);
            } catch (IOException ignored) {}
        }
        Utils.CONFIG_FILE = Utils.CONFIG_FOLDER.resolve("config.toml");
    }

    public void onServerStarting(MinecraftServer server) {
        Utils.SERVER = server;
    }

    public void onServerStarted(MinecraftServer server) {
        ModConfig.INSTANCE.save();
        Utils.statsData = new StatsData();

        ServerTickEvents.END_SERVER_TICK.register(Utils.statsData::update);
        McBotEvents.BEFORE_CHAT.register(Utils.statsData::onEarlyChat);
        McBotEvents.ON_CHAT.register(Utils.statsData::onChat);
    }

    public void onServerStopping(MinecraftServer server) {
        StatsDump.save(StatsDump.getDump(Utils.getStatsData()));
    }
    public void onServerStopped(MinecraftServer server) {
    }
}
