package top.infsky.timerecorder;

import cn.evole.onebot.sdk.util.FileUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import top.infsky.timerecorder.command.ICmdEvent;
import top.infsky.timerecorder.data.StatsData;
import top.infsky.timerecorder.data.StatsDump;
import top.infsky.timerecorder.mcbot.McBot;

public class TimeRecorder implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        init();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ICmdEvent.register(dispatcher));

        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStopped);

    }

    public void init() {
        Utils.CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(Utils.MOD_ID);
        FileUtils.checkFolder(Utils.CONFIG_FOLDER);
        Utils.CONFIG_FILE = Utils.CONFIG_FOLDER.resolve("config.toml");
        Runtime.getRuntime().addShutdownHook(new Thread(McBot::killOutThreads));
    }

    public void onServerStarting(MinecraftServer server) {
        Utils.SERVER = server;
    }

    public void onServerStarted(MinecraftServer server) {
        Utils.statsData = new StatsData();

        McBot.init();
        ServerTickEvents.END_SERVER_TICK.register(Utils.statsData::update);
    }

    public void onServerStopping(MinecraftServer server) {
        StatsDump.save(StatsDump.getDump(Utils.getStatsData()));
        McBot.stop();
    }
    public void onServerStopped(MinecraftServer server) {
        McBot.killOutThreads();
    }
}
