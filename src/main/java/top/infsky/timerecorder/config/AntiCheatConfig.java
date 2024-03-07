package top.infsky.timerecorder.config;

import cn.evole.config.toml.AutoLoadTomlConfig;
import cn.evole.config.toml.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import org.tomlj.TomlTable;

@Getter
@Setter
public class AntiCheatConfig extends AutoLoadTomlConfig {
    @TableField(rightComment = "启用反作弊")
    private boolean AntiCheat = false;

    public AntiCheatConfig() {
        super(null);
    }

    public AntiCheatConfig(TomlTable source) {
        super(source);
        this.load(AntiCheatConfig.class);
    }
}
