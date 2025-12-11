package dev.creoii.greatbigworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public interface AdjacentCollision {
    default void onAdjacentEntityCollision(Entity entity, BlockState state, BlockPos pos) {
    }
}
