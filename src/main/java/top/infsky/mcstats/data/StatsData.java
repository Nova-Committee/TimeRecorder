package top.infsky.mcstats.data;

import lombok.Getter;
import lombok.val;
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

    private static LocalTime REPORT_TIME; // 数据报告时间

    public HashMap<UUID, PlayerData> playerDataMap;  // 玩家统计信息

    public HashMap<UUID, PlayerData> botDataMap;  // 机器人统计信息

    public HashMap<UUID, Boolean> onlineMap;  // 实时上线列表

    public StatsData() {
        val time = ModConfig.INSTANCE.getCommon().getTime().split(":");  // 00:00:00 24-hour
        REPORT_TIME = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

        if (LocalTime.now().isAfter(REPORT_TIME)) Reported = true;
        NextDay = LocalDate.now().plusDays(1);
        reset();
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
                if (!onlineMap.containsKey(player.getUUID())) {
                    // 添加不被包含在统计信息中的玩家
                    onlineMap.put(player.getUUID(), true);
                    if ((CarpetCompat.isFakePlayer(player))) {
                        botDataMap.put(player.getUUID(), new PlayerData(player, true));
                    } else {
                        playerDataMap.put(player.getUUID(), new PlayerData(player, false));
                    }
                }
                // 更新统计信息
                onlineMap.replace(player.getUUID(), true);
                if (CarpetCompat.isFakePlayer(player)) {
                    botDataMap.get(player.getUUID()).timeAdd();
                } else {
                    playerDataMap.get(player.getUUID()).timeAdd();
                }
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
            result.append(FamilyReport.getString(botData, onlineMap.get(botData.getPlayer().getUUID()), true, botData.getPlayer().hasPermissions(2))).append('\n');
        }
        for (PlayerData playerData : playerDataMap.values()) {
            // player or op
            result.append(FamilyReport.getString(playerData, onlineMap.get(playerData.getPlayer().getUUID()), false, playerData.getPlayer().hasPermissions(2))).append('\n');
        }

        result.delete(result.length() - 4, result.length() - 1);
        return result.toString();
    }

    public void reset() {
        LogUtils.LOGGER.info("重置统计信息...");
        playerDataMap = new HashMap<>();
        botDataMap = new HashMap<>();
        onlineMap = new HashMap<>();
    }
}
