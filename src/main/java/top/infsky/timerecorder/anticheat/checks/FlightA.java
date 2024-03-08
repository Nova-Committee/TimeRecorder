package top.infsky.timerecorder.anticheat.checks;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.anticheat.TRPlayer;

public class FlightA extends Check {
    public FlightA(TRPlayer player) {
        super(player, "FlightA");
    }

    @Override
    public void onPacketReceive(@NotNull PacketReceiveEvent event) {
        // If the player sends a flying packet, but they aren't flying, then they are cheating.
        // GrimAC whats going on?
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType()) && !player.isFlying) {
            flag();
        }
    }
}
