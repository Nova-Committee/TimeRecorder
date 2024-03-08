package top.infsky.timerecorder.anticheat.events;

import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.anticheat.CheckManager;
import top.infsky.timerecorder.anticheat.TRPlayer;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.data.PlayerData;
import top.infsky.timerecorder.log.LogUtils;

import java.util.Objects;

public class PlayerLoggedIn {
    public static void onUserLogin(ProtocolPacketEvent<Object> event) {
        if (ModConfig.INSTANCE.getAntiCheatConfig().isEnable()) {
            try {
                PlayerData data = Objects.requireNonNull(Utils.getStatsData().getPlayerDataMap().get(event.getUser().getUUID()));
                data.trPlayer = new TRPlayer(event.getUser());
                data.checkManager = new CheckManager(data.trPlayer);
                Utils.getStatsData().getPlayerDataMap().put(event.getUser().getUUID(), data);
                LogUtils.LOGGER.info("创建TRPlayer");
            } catch (NullPointerException e) {
                Utils.getStatsData().update(null);
                onUserLogin(event);
            }
        }
    }
}
