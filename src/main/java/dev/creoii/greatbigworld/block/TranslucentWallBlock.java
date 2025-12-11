package dev.creoii.greatbigworld.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TranslucentWallBlock extends WallBlock {
    public TranslucentWallBlock(Properties settings) {
        super(settings);
    }

    protected boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.is(this) || super.skipRendering(state, stateFrom, direction);
    }
}
