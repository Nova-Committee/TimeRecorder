package top.infsky.timerecorder.compat;

import lombok.Getter;
import me.drex.vanish.api.VanishEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class VanishCompat {
    public static final boolean VANISH = FabricLoader.getInstance().isModLoaded("melius-vanish");

    @Getter
    public static List<ServerPlayer> vanishedPlayers = new ArrayList<>();  // 维护一个列表，包含全部隐身的玩家

    public static void init() {
        if (VANISH) {
            initVanishEvents();
        }
    }

    // 初始化 Vanish 事件监听器
    private static void initVanishEvents() {
        // 注册事件监听器，当玩家解除隐身时调用 loggedIn 方法
        VanishEvents.UN_VANISH_MESSAGE_EVENT.register((serverPlayer) -> {
            // 获取玩家所在的世界信息
            Level world = serverPlayer.getCommandSenderWorld();
            // 维护列表
            vanishedPlayers.remove(serverPlayer);
            // 返回一个空的 Component
            return Component.empty();
        });

        // 注册事件监听器，当玩家隐身时调用 loggedOut 方法
        VanishEvents.VANISH_MESSAGE_EVENT.register((serverPlayer) -> {
            // 获取玩家所在的世界信息
            Level world = serverPlayer.getCommandSenderWorld();
            // 维护列表
            vanishedPlayers.add(serverPlayer);
            // 返回一个空的 Component
            return Component.empty();
        });
    }
}
