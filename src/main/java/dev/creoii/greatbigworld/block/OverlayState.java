package dev.creoii.greatbigworld.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public interface OverlayState {
    @Environment(EnvType.CLIENT)
    default BlockState getOverlayState(BlockState state, BlockPos pos, Random random) {
        return Blocks.AIR.getDefaultState();
    }
}
