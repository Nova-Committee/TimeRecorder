package top.infsky.timerecorder.config;

import cn.evole.config.toml.AutoLoadTomlConfig;
import cn.evole.config.toml.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import org.tomlj.TomlTable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AntiCheatConfig extends AutoLoadTomlConfig {
    @TableField(rightComment = "启用反作弊")
    private boolean enable = false;
    @TableField(rightComment = "允许展示警报的玩家")
    private List<String> allowAlertPlayers = new ArrayList<>();
    @TableField(rightComment = "最大偏移量")
    private double threshold = 0.001;

    public AntiCheatConfig() {
        super(null);
    }

    public AntiCheatConfig(TomlTable source) {
        super(source);
        this.load(AntiCheatConfig.class);
    }
}
