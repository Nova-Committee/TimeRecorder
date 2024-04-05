package top.infsky.timerecorder.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.anticheat.TRPlayer;

import static top.infsky.timerecorder.anticheat.TRPlayer.CONFIG;


public class BlinkA extends Check {
    
    public BlinkA(@NotNull TRPlayer player) {
        super("BlinkA", player);
    }

    @Override
    public void _onTick() {
        if (player.lastPos == null || player.hasSetback) return;

        if (player.lastPos.distanceTo(player.currentPos) > (player.fabricPlayer.getSpeed() * 10 * player.speedMul + player.fabricPlayer.fallDistance + CONFIG().getThreshold())) {
            flag();
            setback(player.lastPos);
        }
    }
}
