package top.infsky.timerecorder.anticheat.checks;

import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.anticheat.TRPlayer;

public class AirJumpA extends Check {
    public AirJumpA(@NotNull TRPlayer player) {
        super("AirJumpA", player);
    }

    @Override
    public void _onJump() {
        if (!player.fabricPlayer.onGround()) {
            flag();
            setback(player.currentPos);
        }
    }
}
