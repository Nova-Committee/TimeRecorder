package top.infsky.timerecorder.compat;

import cn.evole.mods.mcbot.Const;
import top.infsky.timerecorder.config.ModConfig;

public class McBotSupport {
    public static void sendGroupMsg(String message){
        for (long id : ModConfig.INSTANCE.getCommon().getGroupIdList()){
            Const.sendGroupMsg(id, message);
        }
    }
}
