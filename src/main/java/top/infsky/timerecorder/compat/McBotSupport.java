package top.infsky.timerecorder.compat;

import cn.evole.mods.mcbot.Const;
import cn.evole.onebot.sdk.action.ActionPath;
import cn.evole.onebot.sdk.enums.ActionType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import top.infsky.timerecorder.config.ModConfig;

public class McBotSupport {
    public static void sendAllGroupMsg(String message) {
        for (long id : ModConfig.INSTANCE.getCommon().getGroupIdList()){
            Const.sendGroupMsg(id, message);
        }
    }

    public static void sendAllPlayerMsg(String message) {
        Const.sendAllPlayerMsg(message);
    }

    public static void customRequest(ActionPath action, JsonObject params) {
        Const.customRequest(action, params);
    }

    public static void recallMessage(int message_id) {
        JsonObject json = new Gson().fromJson(
                String.format("{'message_id': %s}", message_id),
                JsonObject.class);
        customRequest(ActionType.DELETE_MSG, json);
    }
}
