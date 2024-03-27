package top.infsky.timerecorder.anticheat.checks;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundKeepAlivePacket;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.data.PlayerData;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlinkA extends Check {
    public Queue<ServerboundKeepAlivePacket> keepAlivePackets = new LinkedBlockingQueue<>(10);

    public BlinkA(@NotNull PlayerData playerData) {
        super("BlinkA", playerData);
    }

    @Override
    public void _onPacketReceive(Packet<ServerGamePacketListener> packet) {
        if (packet instanceof ServerboundKeepAlivePacket) {
            try {
                keepAlivePackets.add((ServerboundKeepAlivePacket) packet);
            } catch (IllegalStateException ignored) {
                keepAlivePackets.poll();
                keepAlivePackets.add((ServerboundKeepAlivePacket) packet);
            }
        }
    }

    @Override
    public void _onTick() {

    }
}
