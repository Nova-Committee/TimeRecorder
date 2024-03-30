package top.infsky.timerecorder.mixins;


import lombok.val;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.RelativeMovement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.timerecorder.Utils;

import java.util.Objects;
import java.util.Set;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinPacket {
    @Shadow public abstract ServerPlayer getPlayer();

    @Inject(method = "teleport(DDDFFLjava/util/Set;)V", at = @At(value = "HEAD"))
    public void teleport(double d, double e, double f, float g, float h, Set<RelativeMovement> set, CallbackInfo ci) {
        try {
            val trPlayer = Objects.requireNonNull(Utils.getPlayer(this.getPlayer().getUUID())).antiCheat;

            trPlayer.manager.onTeleport();
        } catch (NullPointerException ignored) {}
    }

    @Inject(method = "handleMovePlayer", at = @At(value = "HEAD"), cancellable = true)
    public void handleMovePlayer(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        try {
            val trPlayer = Objects.requireNonNull(Utils.getPlayer(this.getPlayer().getUUID())).antiCheat;

            trPlayer.manager.onPacketReceive(packet, ci);
            if (getPlayer().onGround() && !packet.isOnGround()
//                    && packet.getY(getPlayer().getY()) - getPlayer().getY() > 0
            ) {
                trPlayer.manager.onJump();
            }
        } catch (NullPointerException ignored) {}
    }

    @Inject(method = "handleUseItemOn", at = @At(value = "HEAD"), cancellable = true)
    public void handleUseItemOn(ServerboundUseItemOnPacket packet, CallbackInfo ci) {
        try {
            val trPlayer = Objects.requireNonNull(Utils.getPlayer(this.getPlayer().getUUID())).antiCheat;

            trPlayer.manager.onPacketReceive(packet, ci);
        } catch (NullPointerException ignored) {}
    }
}
