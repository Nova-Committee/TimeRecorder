package top.infsky.timerecorder.anticheat;

import lombok.Getter;
import top.infsky.timerecorder.data.PlayerData;
import top.infsky.timerecorder.log.LogUtils;

@Getter
public abstract class Check {
    public String checkName;
    public PlayerData player;

    public Check(String checkName, PlayerData player) {
        this.checkName = checkName;
        this.player = player;
    }

    public final void flag() {
        assert player.antiCheat != null;
        player.antiCheat.violations++;
        LogUtils.alert(player.getName(), checkName);
    }

    public final void flag(String extraMsg) {
        assert player.antiCheat != null;
        player.antiCheat.violations++;
        LogUtils.alert(player.getName(), checkName, extraMsg);
    }

    public abstract void _onTick();

    public void update() {
        if (player == null) return;
        _onTick();
    }
}
