package top.infsky.timerecorder.anticheat.checks;

import net.minecraft.world.phys.Vec3;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.data.PlayerData;

public class FlightA extends Check {
    public short jumpTick = 0;
    public short waterTick = 0;
    public short disableTick = 0;

    public Vec3 setbackPos;

    public FlightA(PlayerData player) {
        super("FlightA", player);
        setbackPos = currentPos;
    }

    @Override
    public void _onTick() {

        if (disableTick > 0) {
            disableTick--;
            return;
        }

        // fix jump
        if (player.onGround()) {
            jumpTick = 8;
            setbackPos = lastOnGroundPos;
        }

        // fix water
        if (player.isInWater()) {
            waterTick = 8;
            setbackPos = lastInWaterPos;
        }


        if (!player.onGround() && jumpTick > 0
                && currentPos.y() - lastOnGroundPos.y() < 1.25219 + CONFIG().getThreshold()) {
            jumpTick--;
        } else if (!player.isInWater() && waterTick > 0
//                && (lastPos.y() - lastPos2.y() + CONFIG().getThreshold()) > (player.position().y() - lastPos.y())  // 警惕出水弱检测
        ) {
            waterTick--;
        } else if (lastPos2 != null && !player.onGround() && !player.isInWater()) {
            jumpTick = 0;
            waterTick = 0;
            if (lastPos.y() - player.position().y() < CONFIG().getThreshold() &&
                    lastPos2.y() - lastPos.y() <= CONFIG().getThreshold()) {
                flag();
                setback(setbackPos);
            }
        }
    }

    @Override
    public void _onTeleport() {
        disableTick = 6;
    }
}
