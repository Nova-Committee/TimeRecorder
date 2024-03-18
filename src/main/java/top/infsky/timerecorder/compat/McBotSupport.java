package top.infsky.timerecorder.compat;

import cn.evole.mods.mcbot.Const;
import cn.evole.mods.mcbot.McBot;
import top.infsky.timerecorder.config.ModConfig;

public class McBotSupport {
    public static void sendGroupMsg(String message){
        for (long id : ModConfig.INSTANCE.getCommon().getGroupIdList()){
            Const.sendGroupMsg(id, message);
        }
    }
}
