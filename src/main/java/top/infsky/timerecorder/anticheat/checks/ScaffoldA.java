package top.infsky.timerecorder.anticheat.checks;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.anticheat.TRPlayer;

public class ScaffoldA extends Check {
    public ScaffoldA(TRPlayer player) {
        super("ScaffoldA", player);
    }

    @Override
    public void _onPacketReceive(Packet<ServerGamePacketListener> basePacket, CallbackInfo ci) {
        if (basePacket instanceof ServerboundUseItemOnPacket packet) {
            final BlockHitResult hitResult = packet.getHitResult();

            if (hitResult.getBlockPos().getX() == player.fabricPlayer.getBlockX()
                    && hitResult.getBlockPos().getZ() == player.fabricPlayer.getBlockZ()
                    && hitResult.getBlockPos().getY() < player.fabricPlayer.getBlockY()
                    && player.fabricPlayer.onGround()
            ) {  // is scaffolding
                if (Math.abs(player.fabricPlayer.getYHeadRot()) > 60) {
                    flag();
                    setback(player.lastPos);
                    setback(ci);
                }
            }
        }
    }
}
