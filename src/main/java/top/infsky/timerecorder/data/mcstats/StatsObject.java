package top.infsky.timerecorder.data.mcstats;

import lombok.Getter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import top.infsky.timerecorder.config.ModConfig;

@Getter
public class StatsObject {
    private final ServerPlayer player;
    private final double startMoveDistance;
    private double cachedMoveDistance = 0;
    private final long startItemPick;
    private long cachedItemPick = 0;
    private final long startBlockBreak;
    private long cachedBlockBreak = 0;
    private final long startBlockPlace;
    private long cachedBlockPlace = 0;
    private final long startEntityKilled;
    private long cachedEntityKilled = 0;

    public StatsObject(ServerPlayer player, double startMoveDistance, long startItemPick, long startBlockBreak, long startBlockPlace, long startEntityKilled) {
        this.player = player;
        this.startMoveDistance = startMoveDistance;
        this.startItemPick = startItemPick;
        this.startBlockBreak = startBlockBreak;
        this.startBlockPlace = startBlockPlace;
        this.startEntityKilled = startEntityKilled;
    }

    public StatsObject(ServerPlayer player, ServerStatsCounter statsCounter) {
        this(player, McStatsManager.getMoveDistance(statsCounter),
                McStatsManager.getItemPick(statsCounter),
                McStatsManager.getBlockBreak(statsCounter),
                McStatsManager.getBlockPlace(statsCounter),
                McStatsManager.getEntityKilled(statsCounter)
        );
    }

    private boolean disabled() {
        return !ModConfig.INSTANCE.getAddon().isAllowOPActiveCount() && player.hasPermissions(2);
    }

    public double getMoveDistance(ServerStatsCounter statsCounter) {
        if (disabled()) return 0;
        cachedMoveDistance = McStatsManager.getMoveDistance(statsCounter) - startMoveDistance;
        return cachedMoveDistance;
    }

    public long getItemPick(ServerStatsCounter statsCounter) {
        if (disabled()) return 0;
        cachedItemPick = McStatsManager.getItemPick(statsCounter) - startItemPick;
        return cachedItemPick;
    }

    public long getBlockBreak(ServerStatsCounter statsCounter) {
        if (disabled()) return 0;
        cachedBlockBreak = McStatsManager.getBlockBreak(statsCounter) - startBlockBreak;
        return cachedBlockBreak;
    }

    public long getBlockPlace(ServerStatsCounter statsCounter) {
        if (disabled()) return 0;
        cachedBlockPlace = McStatsManager.getBlockPlace(statsCounter) - startBlockPlace;
        return cachedBlockPlace;
    }

    public long getEntityKilled(ServerStatsCounter statsCounter) {
        if (disabled()) return 0;
        cachedEntityKilled = McStatsManager.getEntityKilled(statsCounter) - startEntityKilled;
        return cachedEntityKilled;
    }
}
