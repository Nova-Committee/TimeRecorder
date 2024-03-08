package top.infsky.timerecorder.data;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import top.infsky.timerecorder.log.LogUtils;

import java.util.*;

public class FamilyReport {
    public static String getString(Map<UUID, PlayerData> dataMap) {
        // 转换（从旧代码迁移）
        Map<UUID, PlayerData> playerData = new HashMap<>();
        Map<UUID, PlayerData> botData = new HashMap<>();
        dataMap.forEach((uuid, data) -> {
            if (data.isFakePlayer()) botData.put(uuid, data);
            else playerData.put(uuid, data);
        });

        val longestOnlinePlayer = getLongestOnlinePlayer(dataMap, false);
        val longestOnlineBot = getLongestOnlinePlayer(dataMap, true);
        return String.format(
                """
                ----------服务器日报----------
                 玩家日活：%s | 机器人日活：%s
                 上线最久的玩家：%s %s
                 上线最久的机器人：%s %s
                 今日活跃玩家：
                %s
                 感谢各位玩家对服务器做出的贡献！
                """,
                playerData.keySet().size(),
                botData.keySet().size(),
                longestOnlinePlayer.get(0),
                (Long) longestOnlinePlayer.get(1) < 1200 ? (Long) longestOnlinePlayer.get(1) / 20 + "秒" : (Long) longestOnlinePlayer.get(1) / 1200 + "分钟",
                longestOnlineBot.get(0),
                (Long) longestOnlineBot.get(1) < 1200 ? (Long) longestOnlineBot.get(1) / 20 + "秒" : (Long) longestOnlineBot.get(1) / 1200 + "分钟",
                getOnlinePlayerList(playerData)
        );
    }

    public static String getString(@NotNull PlayerData data, boolean online, boolean isFakePlayer, boolean isOp) {
        return String.format(
                """
                §r§n§l%s§r: §7%s  §r%s§r
                 §r上线时长：§7%s§r%s%s
                """,
                isFakePlayer ? "机器人" : isOp ? "管理员" : "玩家",
                data.getName(),
                online ? "§a在线" : "§4离线",
                data.getPlayTime() < 1200 ? data.getPlayTime() / 20 + "秒" : data.getPlayTime() / 1200 + "分钟",
                isOp ? "\n §r使用指令：" : "",
                isOp ? getCommandUsed(data) : ""
        );
    }

    public static @NotNull @Unmodifiable List<Object> getLongestOnlinePlayer(@NotNull Map<UUID, PlayerData> dataMap, boolean isFakePlayer) {
        String name = null;
        long maxTime = 0;

        for (PlayerData data : dataMap.values()) {
            if (data.getPlayTime() > maxTime && data.isFakePlayer() == isFakePlayer) {
                name = data.getName();
                maxTime = data.getPlayTime();
            }
        }

        if (name != null) return List.of(name, maxTime);
        return List.of("无", 0L);
    }

    public static @NotNull String getCommandUsed(@NotNull PlayerData data) {
        final List<String> baseCmdUsed = data.getOPCommandUsed();
        LogUtils.LOGGER.debug(baseCmdUsed.toString());
        if (baseCmdUsed.isEmpty()) return "无";

        // 从ModConfig生成map
        Map<String, Integer> commands = new HashMap<>();
        for (String cmd : baseCmdUsed) {
            val count = commands.get(cmd);
            commands.put(cmd, (count == null) ? 1 : count + 1);
        }

        // 格式化字符串
        StringBuilder back = new StringBuilder();
        back.append('\n');
        for (String cmd : commands.keySet()) {
            back.append(String.format("  §f%s : §r%s次\n", cmd, commands.get(cmd)));
        }

        final int i = back.lastIndexOf("\n");
        if (i == back.length() - 1) back.delete(i, i + 1);
        return back.toString();
    }

    public static @NotNull String getOnlinePlayerList(@NotNull Map<UUID, PlayerData> playerDataMap) {
        StringBuilder back = new StringBuilder();
        for (PlayerData player : playerDataMap.values()) {
            back.append("   ").append(player.getName()).append("\n");
        }

        if (back.isEmpty()) return "   无";
        final int i = back.lastIndexOf("\n");
        if (i == back.length() - 1) back.delete(i, i + 1);
        return back.toString();
    }
}
