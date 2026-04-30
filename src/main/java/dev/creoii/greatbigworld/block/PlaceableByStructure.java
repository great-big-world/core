package dev.creoii.greatbigworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface PlaceableByStructure {
    void onPlaceByStructure(ServerLevelAccessor level, BlockState state, BlockPos pos);
}
