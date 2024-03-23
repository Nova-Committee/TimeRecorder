package top.infsky.timerecorder.config;

import cn.evole.config.toml.AutoLoadTomlConfig;
import cn.evole.config.toml.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import org.tomlj.TomlTable;

import java.util.List;

@Getter
@Setter
public class AddonConfig extends AutoLoadTomlConfig {

    @TableField(rightComment = "允许定时消息")
    private boolean allowGoodMorning = true;
    @TableField(rightComment = "允许撤回消息")
    private boolean allowRecall = true;
    @TableField(rightComment = "消息记录上限")
    private long maxMessageHistory = 10;  // 目前用于撤回
    @TableField(rightComment = "允许消息过滤器")
    private boolean allowFilter = true;
    @TableField(rightComment = "允许OP绕过过滤器")
    private boolean allowOPBypassFilter = false;
    @TableField(rightComment = "过滤消息部分")
    private List<String> filterWords = List.of("fuck");

    public AddonConfig() {
        super(null);
    }

    public AddonConfig(TomlTable source) {
        super(source);
        this.load(CommonConfig.class);
    }
}