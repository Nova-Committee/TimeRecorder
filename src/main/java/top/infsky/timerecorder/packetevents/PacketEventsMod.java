package top.infsky.timerecorder.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.infsky.timerecorder.packetevents.factory.fabric.FabricPacketEventsBuilder;
import top.infsky.timerecorder.packetevents.factory.fabric.ServerInstanceManager;
import top.infsky.timerecorder.packetevents.mixin.ClientPacketListenerAccessor;
import top.infsky.timerecorder.packetevents.mixin.ConnectionAccessor;
import top.infsky.timerecorder.packetevents.mixin.ServerGamePacketListenerImplAccessor;

public class PacketEventsMod {
    //single player world are locked on loading, don't know why
    public static final Logger LOGGER = LoggerFactory.getLogger("packetevents");

    public static void onPreLaunch() {
        PacketEvents.setAPI(FabricPacketEventsBuilder.build("packetevents"));
        PacketEvents.getAPI().getSettings().debug(true).bStats(true);
        PacketEvents.getAPI().load();
        //TODO Test if userconnectevent and userdisconnectevent work.
        //especially disconnect
    }

    public static void onInitialize() {
        ServerInstanceManager.init();
        PacketEvents.getAPI().init();
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            var connection = ((ServerGamePacketListenerImplAccessor)handler).getConnection();
            var channel = ((ConnectionAccessor)connection).getChannel();
            if(ChannelUtilities.pipelineContainsSplitterAndPrepender(channel.pipeline()))
                PacketEvents.getAPI().getInjector().setPlayer(channel, handler.player);
        });

        PacketEvents.getAPI().getEventManager().registerListener(new PacketListenerAbstract() {
            @Override
            public void onPacketSend(PacketSendEvent event) {
                //System.out.println("Packet send: " + event.getPacketType());
                //System.out.println("player: " + event.getPlayer());
            }
        });
    }

    public static void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            var connection = ((ClientPacketListenerAccessor)handler).getConnection();
            var channel = ((ConnectionAccessor)connection).getChannel();
            if(ChannelUtilities.pipelineContainsSplitterAndPrepender(channel.pipeline()))
                PacketEvents.getAPI().getInjector().setPlayer(channel, Minecraft.getInstance().player);
        });
    }
}
