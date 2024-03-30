package top.infsky.timerecorder.anticheat.checks;

import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.anticheat.TRPlayer;

public class SpeedB extends Check {
    public SpeedB(TRPlayer player) {
        super("SpeedB", player);
    }

    @Override
    public void _onTick() {
        if (player.fabricPlayer.isSprinting() && player.fabricPlayer.getFoodData().getFoodLevel() <= 6) {
            flag();
            badPacket();
            player.fabricPlayer.setSprinting(false);
        }
    }
}
