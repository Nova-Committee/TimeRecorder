package top.infsky.timerecorder.data;

import lombok.Getter;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import top.infsky.timerecorder.TimeRecorder;
import top.infsky.timerecorder.log.LogUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class PlayerData {
    @Nullable
    public Player player;  // 玩家

    public String name;  // 名字

    public UUID UUID;  // UUID

    public boolean OP;  // 玩原神玩的

    public boolean fakePlayer;  // 是否为假玩家（仅视觉）

    public long playTime;  // 当天游玩tick数

    public List<String> OPCommandUsed;  // 当天使用OP指令的列表

    public PlayerData(Player gamePlayer, boolean isFakePlayer) {
        LogUtils.LOGGER.debug(String.format("初始化玩家数据: %s", gamePlayer.getName().getString()));
        player = gamePlayer;
        name = player.getName().getString();
        UUID = player.getUUID();
        OP = player.hasPermissions(2);
        fakePlayer = isFakePlayer;
        playTime = 0;
        OPCommandUsed = new LinkedList<>();
    }

    /**
     * 从已有数据还原实例
     * @param name 名字
     * @param UUID UUID
     * @param OP 有2级及以上权限
     * @param fakePlayer 是否为假玩家
     * @param playTime 当天游玩tick数
     * @param OPCommandUsed 当天使用OP指令的列表
     */
    public PlayerData(String name, UUID UUID, boolean OP, boolean fakePlayer, long playTime, List<String> OPCommandUsed) {
        this.name = name;
        this.UUID = UUID;
        this.OP = OP;
        this.fakePlayer = fakePlayer;
        this.playTime = playTime;
        this.OPCommandUsed = OPCommandUsed;
        playerBuilder();
    }

    public void playerBuilder() {
        try {
            player = TimeRecorder.getSERVER().getPlayerList().getPlayer(UUID);
        } catch (NullPointerException e) {
            LogUtils.LOGGER.error(String.format("恢复玩家 %s 失败。", UUID));
        }
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
