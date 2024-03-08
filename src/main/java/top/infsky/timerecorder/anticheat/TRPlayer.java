package top.infsky.timerecorder.anticheat;

import com.github.retrooper.packetevents.protocol.player.User;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.log.LogUtils;

import java.util.UUID;

public class TRPlayer {
    public UUID uuid;
    public final User user;
    public @Nullable Player fabricPlayer;
    public boolean disabled;
    public double x;
    public double y;
    public double z;
    public boolean isFlying;
    public boolean canFly;

    public TRPlayer(User user) {
        this.uuid = user.getUUID();
        this.user = user;
    }

    public void update() {
        assert Utils.getSERVER() != null;
        if (uuid != null && fabricPlayer == null) {
            this.fabricPlayer = Utils.getSERVER().getPlayerList().getPlayer(uuid);
        }
    }

    public String sendMessage(String msg) {
        if (fabricPlayer != null) {
            final String message = String.format("§b§lTR> §r%s", msg);
            LogUtils.LOGGER.info(message);
            fabricPlayer.sendSystemMessage(Component.literal(message));
            return message;
        }
        return "";
    }
}
