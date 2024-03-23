package top.infsky.timerecorder.log;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class LogUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger("TimeRecorder");  // 抄代码抄的

    public static void alert(String player, String module, String extraMsg) {
//        LOGGER.info(String.format("§b§lTR§r§l> §r%s 触发了 %s | %s", player, module, extraMsg));
        LOGGER.info(String.format("TR> %s 触发了 %s | %s", player, module, extraMsg));
    }

    public static void alert(String player, String module) {
//        LOGGER.info(String.format("§b§lTR§r§l> §r%s 触发了 %s", player, module));
        LOGGER.info(String.format("TR> %s 触发了 %s", player, module));
    }
}
