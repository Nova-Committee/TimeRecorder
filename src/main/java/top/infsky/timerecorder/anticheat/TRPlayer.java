package top.infsky.timerecorder.anticheat;

import lombok.Getter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import top.infsky.timerecorder.Utils;
import top.infsky.timerecorder.anticheat.utils.TimeTaskManager;
import top.infsky.timerecorder.config.AntiCheatConfig;
import top.infsky.timerecorder.config.ModConfig;
import top.infsky.timerecorder.data.PlayerData;

import java.util.LinkedList;
import java.util.List;

/**
 * 管理玩家信息的类。每个玩家都应有一个TRPlayer实例。
 */
@Getter
public class TRPlayer {
    public ServerPlayer fabricPlayer;
    public CheckManager manager;
    public PlayerData playerData;
    public static MinecraftServer SERVER = Utils.getSERVER();

    public static AntiCheatConfig CONFIG() { return ModConfig.INSTANCE.getAntiCheat(); }
    public Vec3 currentPos;
    public Vec3 lastPos;
    @Range(from = 0, to = 19) public List<Vec3> posHistory;
    public Vec3 lastOnGroundPos;
    public Vec3 lastInLiquidPos;
    public boolean lastOnGround;
    public boolean hasSetback = false;
    public boolean jumping = false;

    public TimeTaskManager timeTask = new TimeTaskManager();

    public TRPlayer(@NotNull PlayerData playerData) {
        this.fabricPlayer = (ServerPlayer) playerData.player;
        this.manager = CheckManager.create(this);
        this.playerData = playerData;
        currentPos = fabricPlayer.position();
        lastOnGround = fabricPlayer.onGround();
        posHistory = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            posHistory.add(currentPos);
        }
    }

    public void update() {
        fabricPlayer = (ServerPlayer) playerData.player;
        if (fabricPlayer == null) return;

        currentPos = fabricPlayer.position();
        updatePoses();
        if (fabricPlayer.onGround()) {
            lastOnGroundPos = currentPos;
            jumping = false;
        }
        if (fabricPlayer.isInWater() || fabricPlayer.isInLava()) {
            lastInLiquidPos = currentPos;
        }
        timeTask.onTick();

        manager.update();

        lastPos = currentPos;
        lastOnGround = fabricPlayer.onGround();
    }

    private void updatePoses() {
        if (posHistory.size() >= 20) {
            posHistory.remove(posHistory.size() - 1);
            updatePoses();
        }
        posHistory.add(0, currentPos);
    }
}
