package top.infsky.timerecorder.anticheat;

import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.timerecorder.log.LogUtils;

import static top.infsky.timerecorder.anticheat.TRPlayer.CONFIG;

@Getter
public abstract class Check {
    protected final TRPlayer player;
    public String checkName;
    public double violations;

    @Nullable public Integer setbackFoodLevel;


    public Check(String checkName, @NotNull TRPlayer player) {
        this.checkName = checkName;
        this.player = player;
    }

    public final void flag() {
        violations++;
        LogUtils.alert(player.playerData.getName(), checkName, String.format("VL: %s", violations));
    }

    public final void flag(String extraMsg) {
        violations++;
        LogUtils.alert(player.playerData.getName(), checkName, extraMsg);
    }

    public final void setback(@NotNull Vec3 position) {
        if (!CONFIG().isAllowSetback()) return;
        player.hasSetback = true;
        player.needToDoNextTick.add(() -> player.needToDoNextTick.add(() -> player.hasSetback = false));
        player.fabricPlayer.moveTo(position);
        player.fabricPlayer.teleportTo(position.x(), position.y(), position.z());
        player.fabricPlayer.connection.resetPosition();
    }

    public final void setback(@NotNull CallbackInfo ci) {
        if (!CONFIG().isAllowSetback()) return;
        ci.cancel();
        player.fabricPlayer.connection.resetPosition();
        if (setbackFoodLevel == null)
            setbackFoodLevel = player.fabricPlayer.getFoodData().getFoodLevel();
        player.fabricPlayer.getFoodData().setFoodLevel(0);
        player.needToDoNextTick.add(() -> {
            player.fabricPlayer.getFoodData().setFoodLevel(setbackFoodLevel);
            setbackFoodLevel = null;
        });
    }

    public void _onTick() {}
    public void _onTeleport() {}
    public void _onJump() {}
    public void _onPacketReceive(Packet<ServerGamePacketListener> packet, CallbackInfo ci) {}

}
