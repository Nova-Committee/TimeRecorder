package top.infsky.timerecorder;

import cn.evole.onebot.sdk.util.FileUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import top.infsky.timerecorder.command.ICmdEvent;
import top.infsky.timerecorder.data.StatsData;
import top.infsky.timerecorder.data.StatsDump;
import top.infsky.timerecorder.mcbot.McBot;
import top.infsky.timerecorder.packetevents.PacketEventsMod;

public class TimeRecorderClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        init();
        PacketEventsMod.onInitializeClient();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ICmdEvent.register(dispatcher));

        ClientLifecycleEvents.CLIENT_STARTED.register(this::onClientStarted);
        ClientLifecycleEvents.CLIENT_STOPPING.register(this::onClientStopping);
        ClientPlayConnectionEvents.JOIN.register(this::onServerJoined);
        ClientPlayConnectionEvents.DISCONNECT.register(this::onServerLeaved);
    }

    public void init() {
        Utils.CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(Utils.MOD_ID);
        FileUtils.checkFolder(Utils.CONFIG_FOLDER);
        Utils.CONFIG_FILE = Utils.CONFIG_FOLDER.resolve("config.toml");
        Runtime.getRuntime().addShutdownHook(new Thread(McBot::killOutThreads));
    }

    public void onClientStarted(Minecraft client) {
        Utils.CLIENT = client;
        McBot.init();
        ClientTickEvents.END_CLIENT_TICK.register(Utils.statsData::update);
    }

    public void onClientStopping(Minecraft client) {
        StatsDump.save(StatsDump.getDump(Utils.getStatsData()));
        McBot.stop();
    }

    private void onServerJoined(ClientPacketListener clientPacketListener, PacketSender packetSender, Minecraft minecraft) {
        Utils.ClientPlaying = true;
        Utils.statsData = new StatsData();

    }

    private void onServerLeaved(ClientPacketListener clientPacketListener, Minecraft minecraft) {
        Utils.ClientPlaying = false;
        Utils.statsData = null;

    }
}
