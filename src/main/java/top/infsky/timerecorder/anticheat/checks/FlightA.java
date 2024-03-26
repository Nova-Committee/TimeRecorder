package top.infsky.timerecorder.anticheat.checks;

import net.minecraft.world.phys.Vec3;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.data.PlayerData;

public class FlightA extends Check {
    public Vec3 lastPos;
    public Vec3 lastPos2;
    public Vec3 lastOnGroundPos;
    public short disableTick = 0;

    public FlightA(PlayerData player) {
        super("FlightA", player);
        assert player.player != null;
        lastPos = player.player.position();
    }

    @Override
    public void _onTick() {
        assert player.player != null;

        // fix jump
        if (player.player.onGround()) {
            lastOnGroundPos = player.player.position();
            disableTick = 8;
        }

        if (!player.player.onGround() && disableTick > 0
                && player.player.position().y() - lastOnGroundPos.y() < 1.25219 + ModConfig.INSTANCE.getAntiCheat().getThreshold()) {
            disableTick--;
        } else if (lastPos2 != null && !player.player.onGround()) {
            disableTick = 0;
            if (lastPos.y() - player.player.position().y() < ModConfig.INSTANCE.getAntiCheat().getThreshold() &&
                    lastPos2.y() - lastPos.y() <= ModConfig.INSTANCE.getAntiCheat().getThreshold()) {
                flag(String.format("Pos:%s OnGround:%s", player.player.position(), player.player.onGround()));
            }
        }
        lastPos2 = lastPos;
        lastPos = player.player.position();
    }
}
