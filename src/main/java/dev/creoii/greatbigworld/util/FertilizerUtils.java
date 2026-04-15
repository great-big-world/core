package dev.creoii.greatbigworld.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FertilizerUtils {
    public static final Map<FertilizerType, List<Block>> FERTILIZERS = new HashMap<>();
    public static final Map<Block, Block> OVERGROWN_CROPS = new HashMap<>();
    public static final BlockPos[] OFFSETS = new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(1, -1, 0), new BlockPos(0, -1, 1), new BlockPos(-1, -1, 0), new BlockPos(0, -1, -1)};

    public static void registerFertilizer(FertilizerType type, Block block) {
        if (FERTILIZERS.containsKey(type)) {
            FERTILIZERS.get(type).add(block);
        } else {
            List<Block> list = new ArrayList<>();
            list.add(block);
            FERTILIZERS.put(type, list);
        }
    }

    public static void registerOvergrownCrop(Block crop, Block overgrown) {
        OVERGROWN_CROPS.put(crop, overgrown);
    }

    public enum FertilizerType {
        STABILITY, // implemented
        SPEED, // implemented
        IMMUNITY,
        GOOD_HARVEST,
        OVERGROWTH, // broken
        EVIL,
        REPLANT // implemented
    }
}
