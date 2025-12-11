package dev.creoii.greatbigworld.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class TranslucentStairsBlock extends StairBlock {
    public TranslucentStairsBlock(BlockState baseBlockState, Properties settings) {
        super(baseBlockState, settings);
    }

    protected boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.is(this)) {
            Direction stateFacing = state.getValue(StairBlock.FACING);
            Half stateHalf = state.getValue(StairBlock.HALF);
            StairsShape stateShape = state.getValue(StairBlock.SHAPE);

            Direction otherFacing = stateFrom.getValue(StairBlock.FACING);
            Half otherHalf = stateFrom.getValue(StairBlock.HALF);
            StairsShape otherShape = stateFrom.getValue(StairBlock.SHAPE);

            if (direction == Direction.UP) {
                return otherHalf == Half.TOP && stateHalf == Half.TOP && otherFacing == stateFacing;
            } else if (direction == Direction.DOWN) {
                return otherHalf == Half.BOTTOM && stateHalf == Half.BOTTOM && otherFacing == stateFacing;
            }

            if (direction.getOpposite() == stateFacing) {
                return otherFacing == stateFacing && otherShape == stateShape && stateHalf == otherHalf;
            }

            if (stateShape == StairsShape.INNER_LEFT || stateShape == StairsShape.INNER_RIGHT || stateShape == StairsShape.OUTER_LEFT || stateShape == StairsShape.OUTER_RIGHT) {
                return stateShape == otherShape && stateFacing == otherFacing && stateHalf == otherHalf;
            }
        }

        return super.skipRendering(state, stateFrom, direction);
    }
}
