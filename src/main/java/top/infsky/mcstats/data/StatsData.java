package top.infsky.mcstats.data;

import lombok.Getter;
import lombok.val;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import top.infsky.mcstats.McStats;
import top.infsky.mcstats.compat.VanishAPI;
import top.infsky.mcstats.config.ModConfig;
import top.infsky.mcstats.log.LogUtils;
import top.infsky.mcstats.mcbot.McBot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

@Getter
public class StatsData {
    private boolean Reported = false;  // 当天已经report过了

    private LocalDate NextDay;  // 明天

    private static LocalTime REPORT_TIME; // 数据报告时间

    public HashMap<Player, PlayerData> dataMap;  // 玩家统计信息

    public HashMap<Player, Boolean> onlineMap;  // 实时上线列表

    public StatsData() {
        val time = ModConfig.INSTANCE.getCommon().getTime().split(":");  // 00:00:00 24-hour
        REPORT_TIME = LocalTime.of(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));

        if (LocalTime.now().isAfter(REPORT_TIME)) Reported = true;
        NextDay = LocalDate.now().plusDays(1);
        reset();
    }

    /**
     * 每tick更新玩家统计信息
     * @param server 传null也不是不行
     */
    public void update(MinecraftServer server) {
        onlineMap.forEach((Player player, Boolean online) -> onlineMap.replace(player, false));

        // 遍历所有在线玩家
        for (Player player : McStats.getSERVER().getPlayerList().getPlayers()) {
            if (!VanishAPI.isVanished(player)) {  // Vanish 支持
                if (!onlineMap.containsKey(player)) {
                    // 添加不被包含在统计信息中的玩家
                    onlineMap.put(player, true);
                    dataMap.put(player, new PlayerData(player));
                }
                // 更新统计信息
                onlineMap.replace(player, true);
                dataMap.get(player).timeAdd();
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

    public void packetReceived(Player player) {
        if (VanishAPI.isVanished(player)) return;  // Vanish 支持
        if (!onlineMap.containsKey(player)) {
            throw new RuntimeException(String.format("收到玩家 %s 的数据包，但是该玩家不存在！", player.getName().getString()));
        }

        dataMap.get(player).packetAdd();
    }

    public void report() {
        LogUtils.LOGGER.info("输出统计信息...");
        String result = FamilyReport.getString(onlineMap, dataMap);
        LogUtils.LOGGER.info(result);

        McBot.sendGroupMsg(result);
        McStats.getSERVER().getPlayerList().broadcastSystemMessage(Component.literal(result), false);
    }

    public String getReport() {
        return FamilyReport.getString(onlineMap, dataMap);
    }

    public String getFullReport() {
        StringBuilder result = new StringBuilder();
        for (PlayerData playerData : dataMap.values()) {
            result.append(FamilyReport.getString(playerData, onlineMap.get(playerData.getPlayer())));
        }
        return result.toString();
    }

    public void reset() {
        LogUtils.LOGGER.info("重置统计信息...");
        dataMap = new HashMap<>();
        onlineMap = new HashMap<>();
    }
}
