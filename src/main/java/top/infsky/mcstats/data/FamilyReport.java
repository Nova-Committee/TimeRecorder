package top.infsky.mcstats.data;

import com.google.gson.JsonObject;
import lombok.val;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FamilyReport {
    public static String getString(Map<UUID, PlayerData> playerData, Map<UUID, PlayerData> botData) {
        val longestOnlinePlayer = getLongestOnlinePlayer(playerData);
        val longestOnlineBot = getLongestOnlinePlayer(botData);
        return String.format(
                """
                ----------服务器日报----------
                 日活：%s | 机器人日活：%s
                 上线最久的玩家：%s %s
                 上线最久的机器人：%s %s
                 感谢各位玩家对服务器做出的贡献！
                """,
                playerData.keySet().size(),
                botData.keySet().size(),
                longestOnlinePlayer.get(0),
                (Long) longestOnlinePlayer.get(1) < 1200 ? (Long) longestOnlinePlayer.get(1) / 20 + "秒" : (Long) longestOnlinePlayer.get(1) / 1200 + "分钟",
                longestOnlineBot.get(0),
                (Long) longestOnlineBot.get(1) < 1200 ? (Long) longestOnlineBot.get(1) / 20 + "秒" : (Long) longestOnlineBot.get(1) / 1200 + "分钟"
        );
    }

    public static String getString(PlayerData data, boolean online, boolean isFakePlayer) {
        return String.format(
                """
                §r§n§l%s：§7%s  §r%s§r
                 §r上线时长：§7%s§r
                """,
                isFakePlayer ? "机器人" : "玩家",
                data.getPlayer().getName().getString(),
                online ? "§a在线" : "§4离线",
                data.getPlayTime() < 1200 ? data.getPlayTime() / 20 + "秒" : data.getPlayTime() / 1200 + "分钟"
        );
    }

    @SuppressWarnings("unused")
    public static JsonObject getJson(Map<UUID, Boolean> onlineMap, Map<UUID, PlayerData> dataMap) {
        // TODO 写不了一点
        return null;
    }

    public static List<Object> getLongestOnlinePlayer(Map<UUID, PlayerData> dataMap) {
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
}
