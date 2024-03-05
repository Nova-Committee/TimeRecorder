package top.infsky.timerecorder.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.val;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import top.infsky.timerecorder.TimeRecorder;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.log.LogUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class StatsDump {
    public static int save(final @NotNull JsonObject dump) {
        Path path = TimeRecorder.CONFIG_FOLDER.resolve("dump.json");
        try {
            if (Files.exists(path))
                Files.delete(path);
            Files.createFile(path);
            Files.write(path, dump.toString().getBytes());
        } catch (IOException e) {
            LogUtils.LOGGER.error("尝试dump统计数据时遇到异常");
            LogUtils.LOGGER.error(e.getMessage());
            return -1;
        }
        return 1;
    }

    public static int load() throws RuntimeException {
        Path path = TimeRecorder.CONFIG_FOLDER.resolve("dump.json");
        try {
            if (!Files.exists(path)) {
                LogUtils.LOGGER.error("尝试还原统计数据时遇到异常：文件不存在");
                throw new RuntimeException("文件不存在！");
            }
            JsonObject dump = new Gson().fromJson(Files.readString(path), JsonObject.class);
            return fromDump(dump);
        } catch (IOException e) {
            LogUtils.LOGGER.error("尝试还原统计数据时遇到异常");
            LogUtils.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    /**
     * 由StatsData实例生成它的dump
     * @param stats StatsData实例
     * @return Json对象，用于存储StatsData状态
     */
    public static @NotNull JsonObject getDump(final StatsData stats) {
        JsonObject dump = new JsonObject();
        // 一致性检查
        if (FabricLoader.getInstance().getModContainer(TimeRecorder.getMOD_ID()).isPresent())
            dump.addProperty("version", FabricLoader.getInstance().getModContainer(TimeRecorder.getMOD_ID()).get().getMetadata().getVersion().toString());
        else
            throw new RuntimeException(String.format("模组'%s'意外不存在！联系模组制作者或检查你的模组列表。", TimeRecorder.getMOD_ID()));
        dump.addProperty("hash", stats.REPORT_TIME.hashCode());
        dump.addProperty("time", System.currentTimeMillis());

        // 存储数据
        JsonObject onlineMap = new JsonObject();
        stats.getOnlineMap().forEach(((uuid, status) -> onlineMap.addProperty(uuid.toString(), status)));
        dump.add("onlineMap", onlineMap);

        JsonObject playerDataMap = new JsonObject();
        for (UUID uuid : stats.getPlayerDataMap().keySet()) {
            PlayerData data = stats.getPlayerDataMap().get(uuid);
            JsonObject singlePlayerData = new JsonObject();
            singlePlayerData.addProperty("name", data.getName());
            singlePlayerData.addProperty("UUID", data.getUUID().toString());
            singlePlayerData.addProperty("OP", data.isOP());
            singlePlayerData.addProperty("fakePlayer", data.isFakePlayer());
            singlePlayerData.addProperty("playTime", data.getPlayTime());
            JsonArray OPCommandUsed = new JsonArray();
            data.getOPCommandUsed().forEach(OPCommandUsed::add);
            singlePlayerData.add("OPCommandUsed", OPCommandUsed);  // from list to json element.
            playerDataMap.add(uuid.toString(), singlePlayerData);
        }
        dump.add("playerDataMap", playerDataMap);

        JsonObject botDataMap = new JsonObject();
        for (UUID uuid : stats.getBotDataMap().keySet()) {
            PlayerData data = stats.getBotDataMap().get(uuid);
            JsonObject singleBotData = new JsonObject();
            singleBotData.addProperty("name", data.getName());
            singleBotData.addProperty("UUID", data.getUUID().toString());
            singleBotData.addProperty("OP", data.isOP());
            singleBotData.addProperty("fakePlayer", data.isFakePlayer());
            singleBotData.addProperty("playTime", data.getPlayTime());
            JsonArray OPCommandUsed = new JsonArray();
            data.getOPCommandUsed().forEach(OPCommandUsed::add);
            singleBotData.add("OPCommandUsed", OPCommandUsed);  // from list to json element.
            botDataMap.add(uuid.toString(), singleBotData);
        }
        dump.add("botDataMap", botDataMap);

        return dump;
    }

    /**
     * 由dump还原StatsData实例
     * @param dump Json对象，用于存储StatsData状态
     * @return 成功与否
     * @throws IOException 当遇到任何问题
     */
    public static int fromDump(final JsonObject dump) throws IOException {
        // 一致性检查
        try {
            // mod版本
            if (FabricLoader.getInstance().getModContainer(TimeRecorder.getMOD_ID()).isPresent()) {
                if (!dump.get("version").getAsString().equals(
                        FabricLoader.getInstance().getModContainer(TimeRecorder.getMOD_ID()).get().getMetadata().getVersion().toString()))
                    throw new RuntimeException("从dump恢复统计数据状态失败！一致性检查出错: 模组版本错误。");
            } else {
                throw new RuntimeException(String.format("模组'%s'意外不存在！联系模组制作者或检查你的模组列表。", TimeRecorder.getMOD_ID()));
            }
            // 配置文件哈希
            if (dump.get("hash").getAsInt() != TimeRecorder.getStatsData().REPORT_TIME.hashCode())
                throw new RuntimeException("从dump恢复统计数据状态失败！一致性检查出错: 配置文件错误。");
            // 时间
            val baseReportTime = ModConfig.INSTANCE.getCommon().getTime().split(":");
            Calendar dumpTime = Calendar.getInstance();
            Calendar reportTime = Calendar.getInstance();
            dumpTime.setTime(new Date(dump.get("time").getAsLong()));
            reportTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(baseReportTime[0]));
            reportTime.set(Calendar.MINUTE, Integer.parseInt(baseReportTime[1]));
            reportTime.set(Calendar.SECOND, Integer.parseInt(baseReportTime[2]));
            if (Calendar.getInstance().after(reportTime)) reportTime.add(Calendar.DATE, 1);  // fix bugs
            if (dumpTime.after(reportTime))
                throw new RuntimeException("从dump恢复统计数据状态失败！一致性检查出错: 数据已失效。");
        } catch (NullPointerException e) {
            LogUtils.LOGGER.error("从dump恢复统计数据状态失败！一致性检查出错: null");
            LogUtils.LOGGER.error(e.getMessage());
        }

        try {
            // onlineMap
            HashMap<UUID, Boolean> onlineMap = new HashMap<>();
            dump.get("onlineMap").getAsJsonObject().asMap().forEach(
                    (string, jsonElement) -> onlineMap.put(UUID.fromString(string), jsonElement.getAsBoolean()));

            // playerDataMap
            HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : dump.get("playerDataMap").getAsJsonObject().entrySet()) {
                JsonObject singlePlayerData = entry.getValue().getAsJsonObject();
                List<String> OPCommandUsed = new LinkedList<>();
                singlePlayerData.get("OPCommandUsed").getAsJsonArray().asList().forEach(jsonElement -> OPCommandUsed.add(jsonElement.getAsString()));
                playerDataMap.put(UUID.fromString(entry.getKey()), new PlayerData(
                        singlePlayerData.get("name").getAsString(),
                        UUID.fromString(singlePlayerData.get("UUID").getAsString()),
                        singlePlayerData.get("OP").getAsBoolean(),
                        singlePlayerData.get("fakePlayer").getAsBoolean(),
                        singlePlayerData.get("playTime").getAsLong(),
                        OPCommandUsed
                ));
            }

            // botDataMap
            HashMap<UUID, PlayerData> botDataMap = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : dump.get("botDataMap").getAsJsonObject().entrySet()) {
                JsonObject singlePlayerData = entry.getValue().getAsJsonObject();
                List<String> OPCommandUsed = new LinkedList<>();
                singlePlayerData.get("OPCommandUsed").getAsJsonArray().asList().forEach(jsonElement -> OPCommandUsed.add(jsonElement.getAsString()));
                botDataMap.put(UUID.fromString(entry.getKey()), new PlayerData(
                        singlePlayerData.get("name").getAsString(),
                        UUID.fromString(singlePlayerData.get("UUID").getAsString()),
                        singlePlayerData.get("OP").getAsBoolean(),
                        singlePlayerData.get("fakePlayer").getAsBoolean(),
                        singlePlayerData.get("playTime").getAsLong(),
                        OPCommandUsed
                ));
            }
            TimeRecorder.getStatsData().revert(playerDataMap, botDataMap, onlineMap);
        } catch (Exception e) {
            throw new IOException(e);
        }
        return 1;
    }
}