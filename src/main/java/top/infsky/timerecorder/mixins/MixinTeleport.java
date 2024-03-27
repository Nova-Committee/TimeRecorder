package top.infsky.timerecorder.mixins;


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
public abstract class MixinTeleport {
    @Shadow public abstract ServerPlayer getPlayer();

    @Inject(method = "teleport(DDDFFLjava/util/Set;)V", at = @At(value = "HEAD"))
    public void teleport(double d, double e, double f, float g, float h, Set<RelativeMovement> set, CallbackInfo ci) {
        try {
            Objects.requireNonNull(Utils.getPlayer(this.getPlayer().getUUID())).antiCheat.onTeleport();
        } catch (NullPointerException ignored) {}
    }
}
