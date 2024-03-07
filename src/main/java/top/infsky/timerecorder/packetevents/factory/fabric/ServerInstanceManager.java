package top.infsky.timerecorder.packetevents.factory.fabric;

import lombok.Getter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerInstanceManager {
    @Getter
    private static MinecraftServer serverInstance;

    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> serverInstance = server);
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> serverInstance = null);
    }
}
