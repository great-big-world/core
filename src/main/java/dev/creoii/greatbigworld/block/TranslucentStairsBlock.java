package dev.creoii.greatbigworld.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.util.math.Direction;

public class TranslucentStairsBlock extends StairsBlock {
    public TranslucentStairsBlock(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
    }

    protected boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.isOf(this)) {
            Direction stateFacing = state.get(StairsBlock.FACING);
            BlockHalf stateHalf = state.get(StairsBlock.HALF);
            StairShape stateShape = state.get(StairsBlock.SHAPE);

            Direction otherFacing = stateFrom.get(StairsBlock.FACING);
            BlockHalf otherHalf = stateFrom.get(StairsBlock.HALF);
            StairShape otherShape = stateFrom.get(StairsBlock.SHAPE);

            if (direction == Direction.UP) {
                return otherHalf == BlockHalf.TOP && stateHalf == BlockHalf.TOP && otherFacing == stateFacing;
            } else if (direction == Direction.DOWN) {
                return otherHalf == BlockHalf.BOTTOM && stateHalf == BlockHalf.BOTTOM && otherFacing == stateFacing;
            }

            if (direction.getOpposite() == stateFacing) {
                return otherFacing == stateFacing && otherShape == stateShape && stateHalf == otherHalf;
            }

            if (stateShape == StairShape.INNER_LEFT || stateShape == StairShape.INNER_RIGHT || stateShape == StairShape.OUTER_LEFT || stateShape == StairShape.OUTER_RIGHT) {
                return stateShape == otherShape && stateFacing == otherFacing && stateHalf == otherHalf;
            }
        }

        return super.isSideInvisible(state, stateFrom, direction);
    }
}
