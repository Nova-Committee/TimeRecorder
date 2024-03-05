package top.infsky.timerecorder.compat;

import com.mojang.authlib.GameProfile;
import me.drex.vanish.util.VanishManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class VanishAPI {
    public static boolean isVanished(ServerPlayer player) {
        if(!VanishCompat.VANISH) return false;
        // 获取玩家的 GameProfile
        GameProfile gameProfile = player.getGameProfile();
        // 从 GameProfile 中获取 UUID
        UUID uuid = gameProfile.getId();
        // 使用 VanishManager.isVanished 方法检查玩家是否处于隐身状态
        return VanishManager.isVanished(player.getServer(), uuid);
    }

    public static boolean isVanished(Player player) {
        if(!VanishCompat.VANISH) return false;
        // 获取玩家的 GameProfile
        GameProfile gameProfile = player.getGameProfile();
        // 从 GameProfile 中获取 UUID
        UUID uuid = gameProfile.getId();
        // 使用 VanishManager.isVanished 方法检查玩家是否处于隐身状态
        return VanishManager.isVanished(player.getServer(), uuid);
    }
}
