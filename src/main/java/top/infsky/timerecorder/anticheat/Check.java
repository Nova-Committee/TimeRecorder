package top.infsky.timerecorder.anticheat;


import com.github.retrooper.packetevents.event.PacketListener;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.log.LogUtils;

public class Check implements PacketListener {
    protected final TRPlayer player;
    private String checkName;
    public double VL;

    public Check(TRPlayer player) {
        this.player = player;
    }

    public final boolean flag() {
        if (silentFlag()) {
            alert("");
            return true;
        }
        return false;
    }

    public final boolean flag(String message) {
        if (silentFlag()) {
            alert("| " + message);
            return true;
        }
        return false;
    }

    public final boolean silentFlag() {
        if (!player.disabled && ModConfig.INSTANCE.getAntiCheatConfig().isAntiCheat()) {
            VL++;
            return true;
        }
        return false;
    }

    public final void alert(String message) {
        LogUtils.LOGGER.info(player.sendMessage(
                String.format("%s 触发了 %s | VL=%s %s",
                        player.user.getName(), checkName, VL, message)
        ));
    }
}
