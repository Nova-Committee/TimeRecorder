package top.infsky.timerecorder.log;

import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.config.ModConfig;

import java.util.Objects;
import java.util.UUID;

@Getter
public class LogUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("TimeRecorder");  // 抄代码抄的

    public static void alert(String player, String module, String extraMsg) {
        LOGGER.info(String.format("TR> %s 触发了 %s | %s", player, module, extraMsg));
        ModConfig.INSTANCE.getAntiCheat().getAllowAlertPlayers().forEach(string -> {
            assert Utils.getSERVER() != null;
            try {
                Objects.requireNonNull(Utils.getSERVER().getPlayerList().getPlayer(UUID.fromString(string))).sendSystemMessage(Component.literal(
                        String.format("§b§lTR§r§l> §r%s 触发了 %s | %s", player, module, extraMsg)
                ));
            } catch (NullPointerException ignored) {}
        });
    }

    public static void alert(String player, String module) {
        LOGGER.info(String.format("TR> %s 触发了 %s", player, module));
        ModConfig.INSTANCE.getAntiCheat().getAllowAlertPlayers().forEach(string -> {
            assert Utils.getSERVER() != null;
            try {
                Objects.requireNonNull(Utils.getSERVER().getPlayerList().getPlayer(UUID.fromString(string))).sendSystemMessage(Component.literal(
                        String.format("§b§lTR§r§l> §r%s 触发了 %s", player, module)
                ));
            } catch (NullPointerException ignored) {}
        });
    }
}
