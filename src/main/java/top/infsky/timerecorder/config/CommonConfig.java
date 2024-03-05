package top.infsky.timerecorder.config;

import cn.evole.config.toml.AutoLoadTomlConfig;
import cn.evole.config.toml.annotation.TableField;
import lombok.Getter;
import lombok.Setter;
import org.tomlj.TomlTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: McBot-fabric / CommonConfig
 * Author: cnlimiter
 * CreateTime: 2023/11/7 20:24
 * Description:
 */
@Getter
@Setter
public class CommonConfig extends AutoLoadTomlConfig {

    @TableField(rightComment = "统计数据输出时间(24小时制)")
    private String time = "00:00:00";
    @TableField(rightComment = "统计指令列表")
    private List<String> commandStatsList = new ArrayList<>();

    @TableField(rightComment = "开启Q群功能")
    private boolean groupOn = true;
    @TableField(rightComment = "Q群列表")
    private List<Long> groupIdList = new ArrayList<>();//支持多个q群
    @TableField(rightComment = "机器人qq")
    private long botId = 0;//机器人qq

    public CommonConfig() {
        super(null);
        init();
    }

    public CommonConfig(TomlTable source) {
        super(source);
        this.load(CommonConfig.class);
        init();
    }

    private void init() {
        commandStatsList.add("gamemode");
        commandStatsList.add("tp");
    }
    public void removeGroupId(long id) {
        groupIdList.remove(id);
    }

    public void addGroupId(long id) {
        if (!groupIdList.contains(id)) groupIdList.add(id);
    }

}
