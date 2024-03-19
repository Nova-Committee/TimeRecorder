package top.infsky.timerecorder.data;

import com.google.gson.*;
import lombok.val;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.log.LogUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class StatsDump {
    public static @NotNull Path getLatestDump() throws IOException {
        Path indexPath = Utils.CONFIG_FOLDER.resolve("index.json");
        JsonObject indexFile = new Gson().fromJson(Files.readString(indexPath), JsonObject.class);
        return Utils.CONFIG_FOLDER.resolve(indexFile.get("latest").getAsString());
    }

    public static int save(final @NotNull JsonObject dump) {
        String fileName = String.format("dump-%s.json", System.currentTimeMillis());
        Path dumpPath = Utils.CONFIG_FOLDER.resolve(fileName);
        Path indexPath = Utils.CONFIG_FOLDER.resolve("index.json");
        try {
            if (Files.exists(dumpPath))
                Files.delete(dumpPath);
            Files.createFile(dumpPath);
            Files.write(dumpPath, dump.toString().getBytes());

            if (!Files.exists(indexPath))
                Files.createFile(indexPath);
            JsonObject indexFile = new JsonObject();
            indexFile.addProperty("latest", fileName);
            Files.write(indexPath, indexFile.toString().getBytes());
        } catch (IOException e) {
            LogUtils.LOGGER.error("尝试dump统计数据时遇到异常");
            LogUtils.LOGGER.error(e.getMessage());
            return -1;
        }
        return 1;
    }

    public static int load() throws RuntimeException {
        try {
            Path path = getLatestDump();
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
        if (FabricLoader.getInstance().getModContainer(Utils.getMOD_ID()).isPresent())
            dump.addProperty("version", FabricLoader.getInstance().getModContainer(Utils.getMOD_ID()).get().getMetadata().getVersion().toString());
        else
            throw new RuntimeException(String.format("模组'%s'意外不存在！联系模组制作者或检查你的模组列表。", Utils.getMOD_ID()));
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
            singlePlayerData.addProperty("UUID", data.getUuid().toString());
            singlePlayerData.addProperty("OP", data.isOP());
            singlePlayerData.addProperty("fakePlayer", data.isFakePlayer());
            singlePlayerData.addProperty("playTime", data.getPlayTime());
            JsonArray OPCommandUsed = new JsonArray();
            JsonArray messageSent = new JsonArray();
            data.getOPCommandUsed().forEach(OPCommandUsed::add);
            data.getMessageSent().forEach(messageObject -> {
                val singleMessageObject = new JsonObject();
                singleMessageObject.addProperty("messageId", messageObject.messageId());
                singleMessageObject.addProperty("message", messageObject.message());
                messageSent.add(singleMessageObject);
            });
            singlePlayerData.add("OPCommandUsed", OPCommandUsed);  // from list to json element.
            singlePlayerData.add("messageSent", messageSent);
            playerDataMap.add(uuid.toString(), singlePlayerData);
        }
        dump.add("playerDataMap", playerDataMap);

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
            if (FabricLoader.getInstance().getModContainer(Utils.getMOD_ID()).isPresent()) {
                if (!dump.get("version").getAsString().equals(
                        FabricLoader.getInstance().getModContainer(Utils.getMOD_ID()).get().getMetadata().getVersion().toString()))
                    throw new RuntimeException("从dump恢复统计数据状态失败！一致性检查出错: 模组版本错误。");
            } else {
                throw new RuntimeException(String.format("模组'%s'意外不存在！联系模组制作者或检查你的模组列表。", Utils.getMOD_ID()));
            }
            // 配置文件哈希
            if (dump.get("hash").getAsInt() != Utils.getStatsData().REPORT_TIME.hashCode())
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
                List<MessageObject> messageSent = new LinkedList<>();
                singlePlayerData.get("OPCommandUsed").getAsJsonArray().asList().forEach(jsonElement -> OPCommandUsed.add(jsonElement.getAsString()));
                singlePlayerData.get("messageSent").getAsJsonArray().asList().forEach(jsonElement -> {
                    val singleMessageObject = jsonElement.getAsJsonObject().asMap();
                    messageSent.add(new MessageObject(
                            singleMessageObject.get("messageId").getAsInt(),
                            singleMessageObject.get("message").getAsString()));
                });
                playerDataMap.put(UUID.fromString(entry.getKey()), new PlayerData(
                        singlePlayerData.get("name").getAsString(),
                        UUID.fromString(singlePlayerData.get("UUID").getAsString()),
                        singlePlayerData.get("OP").getAsBoolean(),
                        singlePlayerData.get("fakePlayer").getAsBoolean(),
                        singlePlayerData.get("playTime").getAsLong(),
                        OPCommandUsed,
                        messageSent
                ));
            }

            Utils.getStatsData().revert(playerDataMap, onlineMap);
        } catch (Exception e) {
            throw new IOException(e);
        }
        return 1;
    }
}
