package top.infsky.timerecorder.anticheat;

import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.config.AntiCheatConfig;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.data.PlayerData;
import top.infsky.timerecorder.log.LogUtils;

@Getter
public abstract class Check {
    public String checkName;
    public PlayerData playerData;
    public final ServerPlayer player;
    public static AntiCheatConfig CONFIG() { return ModConfig.INSTANCE.getAntiCheat(); }

    public Vec3 currentPos;
    public Vec3 lastPos;
    public Vec3 lastPos2;
    public Vec3 lastOnGroundPos;
    public Vec3 lastInWaterPos;

    public Check(String checkName, @NotNull PlayerData playerData) {
        assert playerData.player != null;
        this.checkName = checkName;
        this.playerData = playerData;
        this.player = (ServerPlayer) playerData.player;
        currentPos = player.position();
    }

    public final void flag() {
        playerData.antiCheat.violations++;
        LogUtils.alert(playerData.getName(), checkName, String.format("VL: %s", playerData.antiCheat.violations));
    }

    public final void flag(String extraMsg) {
        playerData.antiCheat.violations++;
        LogUtils.alert(playerData.getName(), checkName, extraMsg);
    }

    public final void setback(@NotNull Vec3 position) {
        if (!CONFIG().isAllowSetback()) return;
        player.teleportTo(position.x(), position.y(), position.z());
        player.connection.resetPosition();
    }

    public void _onTick() {}

    public void _onTeleport() {}

    public void _onPacketReceive(Packet<ServerGamePacketListener> packet) {}

    public void update() {
        if (player == null) return;
        currentPos = player.position();
        if (player.onGround()) {
            lastOnGroundPos = currentPos;
        }
        if (player.isInWater()) {
            lastInWaterPos = currentPos;
        }
        _onTick();
        lastPos2 = lastPos;
        lastPos = currentPos;
    }
}
