package top.infsky.timerecorder.anticheat.events;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
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
        super(player);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            hasSetFlying = false;

            if (setFlyToFalse == 0) {
                setFlyToFalse = 1;
            } else if (setFlyToFalse == 1) {
                player.isFlying = false;
                setFlyToFalse = -1;
            }

            // TODO
        }
    }
}
