package top.infsky.timerecorder.anticheat.events;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.anticheat.TRPlayer;

public class PacketPlayerAbilities extends Check {
    /**
     * -1 = don't set.<p>
     * 0 is the tick to let flying be true.<p>
     * 1 is the tick to apply this.
     */
    int setFlyToFalse = -1;
    boolean hasSetFlying = false;

    public PacketPlayerAbilities(TRPlayer player) {
        super(player, "PacketPlayerAbilities");
    }

    @Override
    public void onPacketReceive(@NotNull PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            hasSetFlying = false;

            if (setFlyToFalse == 0) {
                setFlyToFalse = 1;
            } else if (setFlyToFalse == 1) {
                player.isFlying = false;
                setFlyToFalse = -1;
            }

            if (event.getPacketType() == PacketType.Play.Client.PLAYER_ABILITIES) {
                WrapperPlayClientPlayerAbilities abilities = new WrapperPlayClientPlayerAbilities(event);
                TRPlayer player = Utils.getTRPlayer(event.getUser());
                if (player == null) return;

                if (hasSetFlying && !abilities.isFlying()) {
                    hasSetFlying = false;
                    setFlyToFalse = 0;
                    return;
                }

                if (abilities.isFlying()) {
                    hasSetFlying = true;
                }

                player.isFlying = abilities.isFlying() && player.canFly;
            }
        }
    }

    @Override
    public void onPacketSend(@NotNull PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.PLAYER_ABILITIES) {
            WrapperPlayServerPlayerAbilities abilities = new WrapperPlayServerPlayerAbilities(event);
            TRPlayer player = Utils.getTRPlayer(event.getUser());

            if (player == null) return;

            setFlyToFalse = -1;
            player.canFly = abilities.isFlightAllowed();
            player.isFlying = abilities.isFlying();
        }
    }
}
