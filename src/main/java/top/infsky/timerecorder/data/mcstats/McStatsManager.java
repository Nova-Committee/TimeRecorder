package top.infsky.timerecorder.data.mcstats;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class McStatsManager {
    public static final List<ResourceLocation> moveList = List.of(Stats.WALK_ONE_CM, Stats.SPRINT_ONE_CM, Stats.CROUCH_ONE_CM,
            Stats.SWIM_ONE_CM, Stats.WALK_ON_WATER_ONE_CM, Stats.WALK_UNDER_WATER_ONE_CM);

    public static double getMoveDistance(ServerStatsCounter stats) {
        double distance = 0;
        for (ResourceLocation stat : moveList) {
            distance += stats.getValue(Stats.CUSTOM.get(stat));
        }
        return distance / 100;  // 转换到block
    }

    public static long getItemPick(ServerStatsCounter stats) {
        long count = 0;
        for (Stat<Item> stat : Stats.ITEM_PICKED_UP) {
            count += stats.getValue(stat);
        }
        return count;
    }

    public static long getBlockBreak(ServerStatsCounter stats) {
        long count = 0;
        for (Stat<Block> stat : Stats.BLOCK_MINED) {
            count += stats.getValue(stat);
        }
        return count;
    }

    public static long getBlockPlace(ServerStatsCounter stats) {
        long count = 0;
        for (Stat<Item> stat : Stats.ITEM_USED) {
            if (stat.getValue() instanceof BlockItem)
                count += stats.getValue(stat);
        }
        return count;
    }

    public static long getEntityKilled(ServerStatsCounter stats) {
        long count = 0;
        for (Stat<EntityType<?>> stat : Stats.ENTITY_KILLED) {
            count += stats.getValue(stat);
        }
        return count;
    }
}
