package top.infsky.mcstats;

import cn.evole.onebot.sdk.util.FileUtils;
import com.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import top.infsky.mcstats.command.ICmdEvent;
import top.infsky.mcstats.config.ModConfig;
import top.infsky.mcstats.data.StatsData;
import top.infsky.mcstats.mcbot.McBot;
import top.infsky.mcstats.mixin.MixinPacket;

import java.nio.file.Path;

public class McStats implements ModInitializer {
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

        ServerTickEvents.END_SERVER_TICK.register(statsData::update);

    }

    public void init() {
        CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("mcstats");
        FileUtils.checkFolder(CONFIG_FOLDER);
        CONFIG_FILE = CONFIG_FOLDER.resolve("config.toml");
        Runtime.getRuntime().addShutdownHook(new Thread(McBot::killOutThreads));
    }

    public void onServerStarting(MinecraftServer server) {
        SERVER = server;
    }

    public void onServerStarted(MinecraftServer server) {
        statsData = new StatsData();

        if (ModConfig.INSTANCE.getCommon().isActiveStats()) {
            PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                    .checkForUpdates(true)
                    .bStats(true);
            PacketEvents.getAPI().load();
            PacketEvents.getAPI().getEventManager().registerListener(new MixinPacket());
            PacketEvents.getAPI().init();
        }
    }

    public void onServerStopping(MinecraftServer server) {
        PacketEvents.getAPI().terminate();
        McBot.stop();
    }
    public void onServerStopped(MinecraftServer server) {
        McBot.killOutThreads();
    }
}
