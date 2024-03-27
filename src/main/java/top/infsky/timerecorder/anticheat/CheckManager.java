package top.infsky.timerecorder.anticheat;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.anticheat.checks.BlinkA;
import top.infsky.timerecorder.anticheat.checks.FlightA;
import top.infsky.timerecorder.data.PlayerData;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {
    public final PlayerData playerData;
    public List<Check> checks = new ArrayList<>();
    public double violations = 0;
    public CheckManager(List<Check> checks, PlayerData playerData) {
        this.playerData = playerData;
        this.checks.addAll(checks);
    }

    @Contract("_ -> new")
    public static @NotNull CheckManager create(PlayerData playerData) {
        final CheckManager checkManager = new CheckManager(List.of(
                new FlightA(playerData),
                new BlinkA(playerData)
        ), playerData);
        checkManager.onTeleport();
        return checkManager;
    }

    public void update() {
        assert playerData.player != null;
        if (playerData.player.isSpectator()) return;
        for (Check check : checks) {
            check.update();
        }
    }

    public void onTeleport() {
        for (Check check : checks) {
            check._onTeleport();
        }
    }

    public void onPacketReceive(Packet<ServerGamePacketListener> packet) {
        for (Check check : checks) {
            check._onPacketReceive(packet);
        }
    }
}
