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
    @TableField(rightComment = "允许计算OP活跃度 (Vanish状态下也会计算活跃度！)")
    private boolean allowOPActiveCount = true;
    @TableField(rightComment = "活跃度权重：移动距离")
    private double moveDistanceWeight = 140;
    @TableField(rightComment = "活跃度权重：拾起物品")
    private double itemPickWeight = 375;
    @TableField(rightComment = "活跃度权重：破坏方块")
    private double blockBreakWeight = 450;
    @TableField(rightComment = "活跃度权重：放置方块")
    private double blockPlaceWeight = 500;
    @TableField(rightComment = "活跃度权重：击杀生物")
    private double entityKilledWeight = 600;

    public AddonConfig() {
        super(null);
    }

    public AddonConfig(TomlTable source) {
        super(source);
        this.load(CommonConfig.class);
    }
}