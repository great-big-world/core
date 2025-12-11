package dev.creoii.greatbigworld.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class TranslucentSlabBlock extends SlabBlock {
    public TranslucentSlabBlock(Properties settings) {
        super(settings);
    }

    protected boolean skipRendering(BlockState state, BlockState other, Direction direction) {
        if (other.is(this)) {
            if (state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE && direction == Direction.UP && other.getValue(SlabBlock.TYPE) == SlabType.BOTTOM)
                return true;
            else if (state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE && direction == Direction.DOWN && other.getValue(SlabBlock.TYPE) == SlabType.TOP)
                return true;

            return other.getValue(SlabBlock.TYPE) == state.getValue(SlabBlock.TYPE) || other.getValue(SlabBlock.TYPE) == SlabType.DOUBLE;
        }
        return super.skipRendering(state, other, direction);
    }
}
