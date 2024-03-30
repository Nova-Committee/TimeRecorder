package top.infsky.timerecorder.anticheat.checks;

import lombok.val;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.anticheat.Check;
import top.infsky.timerecorder.anticheat.TRPlayer;

import static top.infsky.timerecorder.anticheat.TRPlayer.CONFIG;

public class SpeedA extends Check {
    public boolean hasJumped = false;
    public short jumpTick = 0;
    public SpeedA(@NotNull TRPlayer player) {
        super("SpeedA", player);
    }

    @Override
    public void _onTick() {
        if (hasJumped && !player.jumping) {
            hasJumped = false;
            jumpTick = 10;
            return;
        }

        if (jumpTick > 0) jumpTick--;

        // check if player is on ground (not in liquid or in water)
        if (player.lastPos == null || player.hasSetback || !player.fabricPlayer.onGround() || !player.lastOnGround || player.fabricPlayer.isInWater()) return;

        val curPrefixPos = new Vec3(player.currentPos.x, 0, player.currentPos.z);
        val lastPrefixPos = new Vec3(player.lastPos.x, 0, player.lastPos.z);
        double maxTickSpeed;
        if (jumpTick > 0)
            maxTickSpeed = 0.37;
        else if (player.fabricPlayer.isSprinting())
            maxTickSpeed = 0.2806;
        else if (player.fabricPlayer.isSilent())
            maxTickSpeed = 0.06475;
        else  // walking
            maxTickSpeed = 0.21585;

        if (curPrefixPos.distanceTo(lastPrefixPos) > (maxTickSpeed * (1 + player.fabricPlayer.getSpeed()) + CONFIG().getThreshold())) {
            flag(String.valueOf(curPrefixPos.distanceTo(lastPrefixPos) * 20));
            setback(player.lastPos);
        }
    }

    @Override
    public void _onJump() {
        hasJumped = true;
    }
}
