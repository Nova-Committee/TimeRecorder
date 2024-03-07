package top.infsky.timerecorder.config;

import cn.evole.config.toml.AutoReloadToml;
import cn.evole.config.toml.TomlUtil;
import cn.evole.config.toml.annotation.Reload;
import cn.evole.config.toml.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import org.tomlj.TomlTable;
import top.infsky.timerecorder.Utils;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/2 13:44
 * Version: 1.0
 */

@Getter
@Setter
public class ModConfig extends AutoReloadToml {
    @Reload(autoReload = true)
    public static ModConfig INSTANCE = TomlUtil.readConfig(Utils.CONFIG_FILE, ModConfig.class, true);

    @TableField(value = "common", topComment = "通用")
    private CommonConfig common = new CommonConfig();
    @TableField(value = "bot_config", topComment = "机器人")
    private BotConfig botConfig = new BotConfig();
    @TableField(value = "anticheat", topComment = "反作弊(实验性)")
    private AntiCheatConfig antiCheatConfig = new AntiCheatConfig();


    public ModConfig() {
        super(null, Utils.CONFIG_FILE);
    }

    public ModConfig(TomlTable source) {
        super(source, Utils.CONFIG_FILE);
        this.load(ModConfig.class);
    }

    public void save(){
        TomlUtil.writeConfig(Utils.CONFIG_FILE,INSTANCE);
    }

}
