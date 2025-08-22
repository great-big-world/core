package dev.creoii.greatbigworld.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface AdjacentCollision {
    default void onAdjacentEntityCollision(Entity entity, BlockState state, BlockPos pos) {
    }
}
