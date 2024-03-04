package top.infsky.mcstats.data;

import lombok.Getter;
import lombok.val;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.compat.CarpetCompat;
import top.infsky.mcstats.compat.VanishAPI;
import top.infsky.mcstats.config.ModConfig;
import top.infsky.mcstats.log.LogUtils;
import top.infsky.mcstats.mcbot.McBot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class StatsData {
    private boolean Reported = false;  // 当天已经report过了

    private LocalDate NextDay;  // 明天

    public LocalTime REPORT_TIME; // 数据报告时间

    public HashMap<UUID, PlayerData> playerDataMap;  // 玩家统计信息

    public HashMap<UUID, PlayerData> botDataMap;  // 机器人统计信息

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
     * @param botDataMap 机器人数据
     * @param onlineMap 在线列表
     */
    public StatsData(HashMap<UUID, PlayerData> playerDataMap, HashMap<UUID, PlayerData> botDataMap, HashMap<UUID, Boolean> onlineMap) {
        this.playerDataMap = playerDataMap;
        this.botDataMap = botDataMap;
        this.onlineMap = onlineMap;
        init();
    }

    public void init() {
        val time = ModConfig.INSTANCE.getCommon().getTime().split(":");  // 00:00:00 24-hour
        REPORT_TIME = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

        if (LocalTime.now().isAfter(REPORT_TIME)) Reported = true;
        NextDay = LocalDate.now().plusDays(1);

        ServerTickEvents.END_SERVER_TICK.register(this::update);
    }

    /**
     * 每tick更新玩家统计信息
     * @param ignoredServer 传null也不是不行
     */
    public void update(MinecraftServer ignoredServer) {
        onlineMap.forEach((UUID player, Boolean online) -> onlineMap.replace(player, false));

        // 遍历所有在线玩家
        for (Player player : McStats.getSERVER().getPlayerList().getPlayers()) {
            if (!VanishAPI.isVanished(player)) {  // Vanish 支持
                final UUID uuid = player.getUUID();
                if (!onlineMap.containsKey(uuid)) {
                    // 添加不被包含在统计信息中的玩家
                    onlineMap.put(uuid, true);
                    if ((CarpetCompat.isFakePlayer(player))) {
                        botDataMap.put(uuid, new PlayerData(player, true));
                    } else {
                        playerDataMap.put(uuid, new PlayerData(player, false));
                    }
                }
                // 更新统计信息
                update(player, uuid);
            }
        }

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

    private void update(Player player, UUID uuid) {
        onlineMap.replace(uuid, true);
        if (CarpetCompat.isFakePlayer(player)) {
            try {
                botDataMap.get(uuid).timeAdd();
            } catch (NullPointerException e) {
                LogUtils.LOGGER.error(String.format("机器人 %s 的数据不存在！尝试从玩家数据迁移。", player.getName()));
                try {
                    botDataMap.put(uuid, playerDataMap.remove(uuid));
                    update(player, uuid);
                } catch (NullPointerException e2) {
                    LogUtils.LOGGER.error(String.format("机器人 %s 的数据不存在于任何！丢弃机器人。", player.getName()));
                    onlineMap.remove(uuid);
                }
            }
        } else {
            try {
                playerDataMap.get(uuid).timeAdd();
            } catch (NullPointerException e) {
                LogUtils.LOGGER.error(String.format("玩家 %s 的数据不存在！尝试从机器人数据迁移。", player.getName()));
                try {
                    PlayerData tmpData = botDataMap.remove(uuid);
                    tmpData.fakePlayer = false;
                    playerDataMap.put(uuid, tmpData);
                    update(player, uuid);
                } catch (NullPointerException e2) {
                    LogUtils.LOGGER.error(String.format("机器人 %s 的数据不存在于任何！丢弃玩家。", player.getName()));
                    onlineMap.remove(uuid);
                }
            }
        }
    }

    public void report() {
        LogUtils.LOGGER.info("输出统计信息...");
        String result = FamilyReport.getString(playerDataMap, botDataMap);
        LogUtils.LOGGER.info(result);

        McBot.sendGroupMsg(result);
        McStats.getSERVER().getPlayerList().broadcastSystemMessage(Component.literal(result), false);
    }

    public String getReport() {
        return FamilyReport.getString(playerDataMap, botDataMap);
    }

    public String getFullReport() {
        StringBuilder result = new StringBuilder();
        for (PlayerData botData : botDataMap.values()) {
            // bot
            result.append(FamilyReport.getString(botData, onlineMap.get(botData.getUUID()), true, botData.isOP())).append('\n');
        }
        for (PlayerData playerData : playerDataMap.values()) {
            // player or op
            result.append(FamilyReport.getString(playerData, onlineMap.get(playerData.getUUID()), false, playerData.isOP())).append('\n');
        }

        result.delete(result.length() - 3, result.length() - 1);
        return result.toString();
    }

    public void reset() {
        LogUtils.LOGGER.info("重置统计信息...");
        playerDataMap = new HashMap<>();
        botDataMap = new HashMap<>();
        onlineMap = new HashMap<>();
    }
}
