package top.infsky.timerecorder.data;

import net.minecraft.client.multiplayer.PlayerInfo;
import lombok.Getter;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.log.LogUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class PlayerData {
    /**
     * 仅作备用，尽量不要使用
     */
    @Nullable
    public Player player;  // 玩家

    /**
     * 仅作备用，尽量不要使用
     */
    @Nullable
    public PlayerInfo clientPlayer; // 客户端玩家

    public String name;  // 名字

    public UUID uuid;  // UUID

    public boolean OP;  // 玩原神玩的

    public boolean fakePlayer;  // 是否为假玩家（仅视觉）

    public long playTime;  // 当天游玩tick数

    public List<String> OPCommandUsed;  // 当天使用OP指令的列表

    public PlayerData(Player gamePlayer, boolean isFakePlayer) {
        LogUtils.LOGGER.debug(String.format("初始化玩家数据: %s", gamePlayer.getName().getString()));
        player = gamePlayer;
        name = player.getName().getString();
        uuid = player.getUUID();
        OP = player.hasPermissions(2);
        fakePlayer = isFakePlayer;
        playTime = 0;
        OPCommandUsed = new LinkedList<>();
    }

    /**
     * 纯客户端支持
     * @param gamePlayer 玩家
     * @param isFakePlayer 是否为假人
     */
    public PlayerData(@NotNull PlayerInfo gamePlayer, boolean isFakePlayer) {
        LogUtils.LOGGER.debug(String.format("初始化玩家数据: %s", gamePlayer.getProfile().getName()));
        clientPlayer = gamePlayer;
        name = clientPlayer.getProfile().getName();
        uuid = clientPlayer.getProfile().getId();
        OP = false;
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
        this.uuid = UUID;
        this.OP = OP;
        this.fakePlayer = fakePlayer;
        this.playTime = playTime;
        this.OPCommandUsed = OPCommandUsed;
        playerBuilder();
    }

    public void playerBuilder() {
        try {
            if (!Utils.isClient() && Utils.getSERVER() != null) {
                player = Utils.getSERVER().getPlayerList().getPlayer(uuid);
                return;
            } else if (Utils.getCLIENT() != null) {
                clientPlayer = Objects.requireNonNull(Utils.getCLIENT().getConnection()).getPlayerInfo(uuid);
                return;
            }
            throw new RuntimeException("意外的playerBuilder()当无任何有效连接");
        } catch (RuntimeException e) {
            LogUtils.LOGGER.error(String.format("恢复玩家 %s 失败。", uuid), e);
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
