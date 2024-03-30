package top.infsky.timerecorder.mixins;


import com.mojang.authlib.GameProfile;
import lombok.val;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.timerecorder.Utils;

import java.util.Objects;

@Mixin(Player.class)
public class MixinPlayer {
    @Shadow @Final private GameProfile gameProfile;

    @Inject(method = "jumpFromGround", at = @At(value = "HEAD"))
    public void jumpFromGround(CallbackInfo ci) {
        try {
            val trPlayer = Objects.requireNonNull(Utils.getPlayer(this.gameProfile.getId())).antiCheat;

            trPlayer.manager.onJump();
        } catch (NullPointerException ignored) {}
    }
}
