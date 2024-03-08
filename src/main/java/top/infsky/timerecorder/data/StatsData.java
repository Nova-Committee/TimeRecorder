package top.infsky.timerecorder.data;

import lombok.Getter;
import lombok.val;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.compat.CarpetCompat;
import top.infsky.timerecorder.compat.VanishAPI;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.log.LogUtils;
import top.infsky.timerecorder.mcbot.McBot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class StatsData {
    private boolean Reported = false;  // 当天已经report过了

    private LocalDate NextDay;  // 明天

    public LocalTime REPORT_TIME; // 数据报告时间

    public HashMap<UUID, PlayerData> playerDataMap;  // 玩家的统计信息

    public HashMap<UUID, Boolean> onlineMap;  // 实时上线列表

    /**
     * 生成一个空的实例
     */
    public StatsData() {
        init();
        reset();
    }

    /**
     * 从已有数据还原实例
     * @param playerDataMap 玩家数据
     * @param onlineMap 在线列表
     */
    public void revert(HashMap<UUID, PlayerData> playerDataMap, HashMap<UUID, Boolean> onlineMap) {
        this.playerDataMap = playerDataMap;
        this.onlineMap = onlineMap;
        init();
    }

    public void init() {
        Reported = false;
        val time = ModConfig.INSTANCE.getCommon().getTime().split(":");  // 00:00:00 24-hour
        REPORT_TIME = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

        if (LocalTime.now().isAfter(REPORT_TIME)) Reported = true;
        NextDay = LocalDate.now().plusDays(1);
    }

    /**
     * 每tick更新玩家统计信息
     * @param ignoredServer 传null也不是不行
     */
    public void update(MinecraftServer ignoredServer) {
        assert Utils.getSERVER() != null;
        onlineMap.forEach((UUID player, Boolean online) -> onlineMap.replace(player, false));

        // 遍历所有在线玩家
        for (Player player : Utils.getSERVER().getPlayerList().getPlayers()) {
            if (!VanishAPI.isVanished(player)) {  // Vanish 支持
                final UUID uuid = player.getUUID();
                if (!onlineMap.containsKey(uuid)) {
                    // 添加不被包含在统计信息中的玩家
                    onlineMap.put(uuid, true);
                    playerDataMap.put(uuid, new PlayerData(player, CarpetCompat.isFakePlayer(player)));
                }
                // 更新统计信息
                update(player, uuid);
            }
        }

        if (ModConfig.INSTANCE.getCommon().isAllowAutoReport()) {
            if (isReported()) {
                if (LocalDate.now().isEqual(NextDay)) Reported = false;
            } else {
                if (LocalTime.now().isAfter(REPORT_TIME)) {
                    NextDay = LocalDate.now().plusDays(1);
                    Reported = true;
                    report();
                    reset();
                }
            }
        }
    }

    private void update(Player player, UUID uuid) {
        onlineMap.replace(uuid, true);
        try {
            playerDataMap.get(uuid).timeAdd();
        } catch (NullPointerException e) {
            LogUtils.LOGGER.error(String.format("玩家 %s 的数据不存在！丢弃玩家。", player.getName()), e);
            onlineMap.remove(uuid);
        }
    }

    public void report() {
        LogUtils.LOGGER.info("输出统计信息...");
        String result = FamilyReport.getString(playerDataMap);
        LogUtils.LOGGER.info(result);

        McBot.sendGroupMsg(result);

        try {
            if (Utils.getSERVER() != null)
                Utils.getSERVER().getPlayerList().broadcastSystemMessage(Component.literal(result), false);
        } catch (NullPointerException e) {
            LogUtils.LOGGER.error("在输出统计信息到游戏时遇到了意外。");
        }
    }

    public String getReport() {
        return FamilyReport.getString(playerDataMap);
    }

    public String getFullReport() {
        StringBuilder result = new StringBuilder();
        for (PlayerData playerData : playerDataMap.values()) {
            // 生成单个玩家的数据文本
            result.append(FamilyReport.getString(playerData, onlineMap.get(playerData.getUuid()), playerData.isFakePlayer(), playerData.isOP())).append('\n');
        }

        if (result.isEmpty()) return "";
        result.delete(result.length() - 3, result.length() - 1);
        return result.toString();
    }

    public void reset() {
        LogUtils.LOGGER.info("重置统计信息...");
        playerDataMap = new HashMap<>();
        onlineMap = new HashMap<>();
    }
}
