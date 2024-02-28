package top.infsky.mcstats.config;

import cn.evole.config.toml.AutoLoadTomlConfig;
import lombok.Getter;
import lombok.Setter;
import org.tomlj.TomlTable;

/**
 * Name: McBot-fabric / BotConfig
 * Author: cnlimiter
 * CreateTime: 2023/11/7 20:28
 * Description:
 */

@Getter
@Setter
public class BotConfig extends AutoLoadTomlConfig {

    private String url = "ws://127.0.0.1:8080";
    private String token = "";
    private long botId = 0L;
    private boolean isAccessToken = false;
    private boolean miraiHttp = false;
    private boolean reconnect = true;
    private int maxReconnectAttempts = 20;
    private String msgType = "string";

    public BotConfig() {
        super(null);
    }

    public BotConfig(TomlTable source) {
        super(source);
        this.load(BotConfig.class);
    }

    public cn.evole.onebot.client.core.BotConfig toBot(){
        return new cn.evole.onebot.client.core.BotConfig(url, token, botId, isAccessToken, miraiHttp, reconnect, maxReconnectAttempts, msgType);
    }
}
