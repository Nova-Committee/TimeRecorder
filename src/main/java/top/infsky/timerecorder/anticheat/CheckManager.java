package top.infsky.timerecorder.anticheat;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.timerecorder.anticheat.checks.*;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {
    public final TRPlayer player;
    public List<Check> checks = new ArrayList<>();
    public CheckManager(List<Check> checks, TRPlayer player) {
        this.player = player;
        this.checks.addAll(checks);
    }

    @Contract("_ -> new")
    public static @NotNull CheckManager create(TRPlayer player) {
        final CheckManager checkManager = new CheckManager(List.of(
                new FlightA(player),
                new BlinkA(player),
                new AirJumpA(player),
                new AirPlaceA(player),
                new SpeedA(player)
        ), player);
        checkManager.onTeleport();
        return checkManager;
    }

    public void update() {
        if (player.fabricPlayer.isSpectator() || player.fabricPlayer.isCreative()) return;
        for (Check check : checks) {
            check._onTick();
        }
    }

    public void onPacketReceive(Packet<ServerGamePacketListener> packet, CallbackInfo ci) {
        for (Check check : checks) {
            check._onPacketReceive(packet, ci);
        }
    }

    public void onTeleport() {
        for (Check check : checks) {
            check._onTeleport();
        }
    }

    public void onJump() {
        for (Check check : checks) {
            check._onJump();
        }
    }
}
