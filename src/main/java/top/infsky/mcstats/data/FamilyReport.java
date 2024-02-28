package top.infsky.mcstats.data;

import com.google.gson.JsonObject;
import lombok.val;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;

public class FamilyReport {
    public static String getString(Map<Player, Boolean> onlineMap, Map<Player, PlayerData> dataMap) {
        val longestOnlinePlayer = getLongestOnlinePlayer(dataMap);
        return String.format(
                """
                ----------服务器日报----------
                 日活：%s
                 上线最久的玩家：%s %s分钟
                 最活跃的玩家：%s
                 感谢各位玩家对服务器做出的贡献！
                """,
                onlineMap.keySet().size(),
                longestOnlinePlayer.get(0),
                (Long) longestOnlinePlayer.get(1) / 1200,
                getMostActivePlayer(dataMap)
        );
    }

    public static String getString(PlayerData data, boolean online) {
        return String.format(
                """
                玩家：%s  %s
                上线时长：%s分钟
                活跃度：%s
                """,
                data.getPlayer().getName().getString(),
                online ? "在线" : "离线",
                data.getPlayTime() / 1200,
                data.getPacketsCount()
        );
    }

    public static JsonObject getJson(Map<Player, Boolean> onlineMap, Map<Player, PlayerData> dataMap) {
        // TODO 写不了一点
        return null;
    }

    public static List<Object> getLongestOnlinePlayer(Map<Player, PlayerData> dataMap) {
        Player player = null;
        long maxTime = 0;

        for (PlayerData data : dataMap.values()) {
            if (data.getPlayTime() > maxTime) {
                player = data.getPlayer();
                maxTime = data.getPlayTime();
            }
        }

        if (player != null) return List.of(player.getName().getString(), maxTime);
        return List.of("无", 0L);
    }

    public static String getMostActivePlayer(Map<Player, PlayerData> dataMap) {
        Player player = null;
        long maxPacket = 0;

        for (PlayerData data : dataMap.values()) {
            if (data.getPacketsCount() > maxPacket) {
                player = data.getPlayer();
                maxPacket = data.getPacketsCount();
            }
        }

        if (player != null) return player.getName().toString();
        return "无";
    }
}
