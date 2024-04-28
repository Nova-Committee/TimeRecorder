package top.infsky.timerecorder.data;

import lombok.Getter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.compat.CarpetCompat;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.data.mcstats.StatsObject;
import top.infsky.timerecorder.log.LogUtils;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

@Getter
public class PlayerData {
    /**
     * 仅作备用，尽量不要使用
     */
    @Nullable
    public Player player;  // 玩家
    @Nullable
    public ServerStatsCounter vanillaStats;  // 原版统计信息

    public String name;  // 名字

    public UUID uuid;  // UUID

    public boolean OP;  // 玩原神玩的

    public boolean fakePlayer;  // 是否为假玩家（仅视觉）

    public long playTime;  // 当天游玩tick数

    public StatsObject statsObject; // 原版统计信息的友善化对象。基于偏移量。

    public List<String> OPCommandUsed;  // 当天使用OP指令的列表

    public Deque<MessageObject> messageSent;  // 玩家的消息历史

    public PlayerData(@NotNull Player gamePlayer, boolean isFakePlayer) {
        LogUtils.LOGGER.debug(String.format("初始化玩家数据: %s", gamePlayer.getName().getString()));
        player = gamePlayer;
        name = player.getName().getString();
        uuid = player.getUUID();
        OP = player.hasPermissions(2);
        fakePlayer = isFakePlayer;
        playTime = 0;
        OPCommandUsed = new LinkedList<>();
        messageSent = new LinkedBlockingDeque<>();
        vanillaStats = ((ServerPlayer) player).getStats();
        statsObject = new StatsObject((ServerPlayer) player, vanillaStats);
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
    public PlayerData(String name, UUID UUID, boolean OP, boolean fakePlayer, long playTime, List<String> OPCommandUsed, List<MessageObject> messageSent) {
        this.name = name;
        this.uuid = UUID;
        this.OP = OP;
        this.fakePlayer = fakePlayer;
        this.playTime = playTime;
        this.OPCommandUsed = OPCommandUsed;
        this.messageSent = new LinkedBlockingDeque<>();
        this.messageSent.addAll(messageSent);
        playerBuilder();
    }

    public void playerBuilder() {
        assert Utils.getSERVER() != null;
        try {
            if (player == null)
                player = Utils.getSERVER().getPlayerList().getPlayer(uuid);
            if (vanillaStats == null && player != null)
                vanillaStats = ((ServerPlayer) player).getStats();
        } catch (Exception e) {
            LogUtils.LOGGER.error(String.format("恢复玩家 %s 失败。", uuid), e);
        }
    }

    public void playerBuilder(ServerPlayer player) {
        try {
            this.player = player;
            if (player != null)
                vanillaStats = player.getStats();
        } catch (Exception e) {
            LogUtils.LOGGER.error(String.format("恢复玩家 %s 失败。", uuid), e);
        }
    }

    /**
     * 更新该玩家的数据（每tick）
     */
    public void update() {
        playerBuilder();
        if (player == null) return;

        OP = player.hasPermissions(2);
        if (fakePlayer) fakePlayer = CarpetCompat.isFakePlayer(player);
        playTime += 1;
    }

    /**
     * 该玩家发送了一条聊天
     */
    public void onChat(int messageId, String message) {
        if (messageSent.size() > ModConfig.INSTANCE.getAddon().getMaxMessageHistory()) {
            messageSent.remove();
            onChat(messageId, message);
        } else {
            messageSent.add(new MessageObject(messageId, message));
        }
    }

    /**
     * 获取活跃度
     */
    public float getActive() {
        return (float) (
                statsObject.getMoveDistance(vanillaStats) * ModConfig.INSTANCE.getAddon().getMoveDistanceWeight() +
                ((double) statsObject.getItemPick(vanillaStats)) * ModConfig.INSTANCE.getAddon().getItemPickWeight() +
                ((double) statsObject.getBlockBreak(vanillaStats)) * ModConfig.INSTANCE.getAddon().getBlockBreakWeight() +
                ((double) statsObject.getBlockPlace(vanillaStats)) * ModConfig.INSTANCE.getAddon().getBlockPlaceWeight() +
                ((double) statsObject.getEntityKilled(vanillaStats)) * ModConfig.INSTANCE.getAddon().getEntityKilledWeight()
        ) / playTime;

    }
}
