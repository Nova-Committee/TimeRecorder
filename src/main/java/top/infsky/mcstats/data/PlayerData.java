package top.infsky.mcstats.data;

import lombok.Getter;
import net.minecraft.world.entity.player.Player;
import top.infsky.mcstats.log.LogUtils;

import java.util.LinkedList;
import java.util.List;

@Getter
public class PlayerData {
    public Player player;  // 玩家

    public boolean fakePlayer;  // 是否为假玩家

    public long playTime;  // 当天游玩tick数

    public List<String> OPCommandUsed;  // 当天使用OP指令的列表

    public PlayerData(Player gamePlayer, boolean isFakePlayer) {
        LogUtils.LOGGER.debug(String.format("初始化玩家数据: %s", gamePlayer.getName().getString()));
        player = gamePlayer;
        fakePlayer = isFakePlayer;
        playTime = 0;
        OPCommandUsed = new LinkedList<>();
    }

    /**
     * 玩家在服务器又待了1tick
     */
    public void timeAdd() {
        playTime += 1;
    }

    /**
     * 一天过完了
     */
    public void reset() {
        playTime = 0;
        OPCommandUsed = new LinkedList<>();
    }
}
