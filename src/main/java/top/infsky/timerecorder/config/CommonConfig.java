package top.infsky.timerecorder.config;

import cn.evole.config.toml.AutoLoadTomlConfig;
import cn.evole.config.toml.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import org.tomlj.TomlTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: McBotSupport-fabric / CommonConfig
 * Author: cnlimiter
 * CreateTime: 2023/11/7 20:24
 * Description:
 */
@Getter
@Setter
public class CommonConfig extends AutoLoadTomlConfig {

    @TableField(rightComment = "允许定时输出统计数据")
    private boolean allowAutoReport = true;
    @TableField(rightComment = "统计数据输出时间(24小时制)")
    private String time = "00:00:00";
    @TableField(rightComment = "允许统计指令")
    private boolean allowCommandStats = true;
    @TableField(rightComment = "统计指令列表")
    private List<String> commandStatsList = List.of("gamemode", "tp");

    @TableField(rightComment = "开启Q群功能")
    private boolean groupOn = true;
    @TableField(rightComment = "Q群列表")
    private List<Long> groupIdList = new ArrayList<>();  // 支持多个q群

    public CommonConfig() {
        super(null);
    }

    public CommonConfig(TomlTable source) {
        super(source);
        this.load(CommonConfig.class);
    }

    public void removeGroupId(long id) {
        groupIdList.remove(id);
    }

    public void addGroupId(long id) {
        if (!groupIdList.contains(id)) groupIdList.add(id);
    }

}
