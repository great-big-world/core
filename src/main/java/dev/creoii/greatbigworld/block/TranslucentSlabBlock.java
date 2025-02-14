package dev.creoii.greatbigworld.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.Direction;

public class TranslucentSlabBlock extends SlabBlock {
    public TranslucentSlabBlock(Settings settings) {
        super(settings);
    }

    protected boolean isSideInvisible(BlockState state, BlockState other, Direction direction) {
        if (other.isOf(this)) {
            if (state.get(SlabBlock.TYPE) == SlabType.DOUBLE && direction == Direction.UP && other.get(SlabBlock.TYPE) == SlabType.BOTTOM)
                return true;
            else if (state.get(SlabBlock.TYPE) == SlabType.DOUBLE && direction == Direction.DOWN && other.get(SlabBlock.TYPE) == SlabType.TOP)
                return true;

            return other.get(SlabBlock.TYPE) == state.get(SlabBlock.TYPE) || other.get(SlabBlock.TYPE) == SlabType.DOUBLE;
        }
        return super.isSideInvisible(state, other, direction);
    }
}
