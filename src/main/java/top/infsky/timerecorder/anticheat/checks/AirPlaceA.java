package top.infsky.timerecorder.anticheat.checks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.anticheat.TRPlayer;

import java.util.List;

public class AirPlaceA extends Check {
    public AirPlaceA(@NotNull TRPlayer player) {
        super("AirPlaceA", player);
    }

    @Contract("_ -> new")
    private static @Unmodifiable List<BlockPos> getBlocksNeedToCheck(@NotNull BlockPos pos) {
        return List.of(pos.east(), pos.west(), pos.north(), pos.south(), pos.above(), pos.below());
    }

    @Override
    public void _onPacketReceive(Packet<ServerGamePacketListener> basePacket, CallbackInfo ci) {
        if (basePacket instanceof ServerboundUseItemOnPacket packet) {
            final BlockHitResult hitResult = packet.getHitResult();
            final ServerLevel level = player.fabricPlayer.serverLevel();

            short airCount = 0;
            for (BlockPos pos : getBlocksNeedToCheck(hitResult.getBlockPos())) {
                if (level.getBlockState(pos).getBlock() instanceof AirBlock) {
                    airCount++;
                }
            }

            if (airCount >= 6) {
                flag();
                setback(ci);
            }
        }
    }
}
