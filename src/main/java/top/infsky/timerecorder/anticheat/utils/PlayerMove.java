package top.infsky.timerecorder.anticheat.utils;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.Utils;

public class PlayerMove {
    public static double getXzTickSpeed(@NotNull Vec3 lastTick, @NotNull Vec3 currentTick) {
        Vec3 prefixLast = new Vec3(lastTick.x(), 0, lastTick.z());
        Vec3 prefixCurrent = new Vec3(currentTick.x(), 0, currentTick.z());
        return prefixCurrent.distanceTo(prefixLast);
    }

    public static double getXzSecSpeed(@NotNull Vec3 lastTick, @NotNull Vec3 currentTick) {
        assert Utils.getSERVER() != null;

        final double tickSpeed = getXzTickSpeed(lastTick, currentTick);
        final double tickTime = Utils.getSERVER().tickTimes[Utils.getSERVER().getTickCount() % 100] / 1000000.0;
//        LogUtils.alert("tickTime", "debug", String.valueOf(tickTime));
        if (tickTime <= 50)
            return tickSpeed * 20;
        else
            return tickSpeed * 1000 / tickTime;
    }
}
