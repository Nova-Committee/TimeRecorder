package top.infsky.mcstats.data;

import lombok.Getter;
import lombok.extern.java.Log;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import top.infsky.mcstats.log.LogUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayerData {
    public Player player;  // 玩家

    public long playTime;  // 当天游玩tick数

    public long packetsCount;  // 当天发包数量

    public PlayerData(Player gamePlayer) {
        LogUtils.LOGGER.info(String.format("初始化玩家数据: %s", gamePlayer.getName().getString()));
        player = gamePlayer;
        playTime = 0;
        packetsCount = 0;
    }

    /**
     * 玩家在服务器又待了1tick
     */
    public void timeAdd() {
        playTime += 1;
    }

    /**
     * 玩家发了一个包
     */
    public void packetAdd() {
        packetsCount++;
    }

    /**
     * 一天过完了
     */
    public void reset() {
        playTime = 0;
        packetsCount = 0;
    }
}
