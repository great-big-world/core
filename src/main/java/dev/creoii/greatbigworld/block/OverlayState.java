package dev.creoii.greatbigworld.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public interface OverlayState {
    @Environment(EnvType.CLIENT)
    default BlockState gbw$getOverlayState(BlockState state, BlockPos pos, RandomSource random) {
        return Blocks.AIR.defaultBlockState();
    }
}
