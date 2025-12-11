package dev.creoii.greatbigworld.world.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.registry.GBWTreeDecoratorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.apache.commons.lang3.mutable.MutableInt;

public class BranchTreeDecorator extends TreeDecorator {
    public static final MapCodec<BranchTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(BlockStateProvider.CODEC.fieldOf("branch_provider").forGetter(config -> {
            return config.branchProvider;
        }), IntProvider.NON_NEGATIVE_CODEC.fieldOf("branch_count").forGetter(config -> {
            return config.branchCount;
        }), Codec.INT.fieldOf("min_height").orElse(5).forGetter(config -> {
            return config.minHeight;
        })).apply(instance, BranchTreeDecorator::new);
    });
    private final BlockStateProvider branchProvider;
    private final IntProvider branchCount;
    private final int minHeight;

    public BranchTreeDecorator(BlockStateProvider state, IntProvider branchCount, int minHeight) {
        this.branchProvider = state;
        this.branchCount = branchCount;
        this.minHeight = Math.max(0, minHeight);
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return GBWTreeDecoratorTypes.BRANCH;
    }

    @Override
    public void place(Context generator) {
        MutableInt branches = new MutableInt(branchCount.sample(generator.random()));
        LevelSimulatedReader world = generator.level();
        generator.logs().sort((logPos1, logPos2) -> {
            if (logPos1.getY() == logPos2.getY())
                return 0;
            return logPos1.getY() - logPos2.getY();
        });
        for (int i = minHeight + generator.random().nextInt(2); i < generator.logs().size(); i += 2) {
            BlockPos pos = generator.logs().get(i);
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(generator.random());
            BlockPos offset = pos.relative(direction);
            if (world.isStateAtPosition(offset, state -> state.isAir() || state.canBeReplaced()) && generator.random().nextFloat() <= .4f) {
                if (world.isStateAtPosition(offset.relative(direction), state -> state.isAir() || state.canBeReplaced())) {
                    BlockState state = branchProvider.getState(generator.random(), offset);

                    if (state.hasProperty(BlockStateProperties.AXIS)) {
                        state = state.setValue(BlockStateProperties.AXIS, direction.getAxis());
                    } else if (state.hasProperty(BlockStateProperties.FACING)) {
                        state = state.setValue(BlockStateProperties.FACING, direction);
                    } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) && direction.getAxis().isHorizontal()) {
                        state = state.setValue(BlockStateProperties.HORIZONTAL_AXIS, direction.getAxis());
                    } else if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && direction.getAxis().isHorizontal()) {
                        state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
                    } else if (state.hasProperty(BlockStateProperties.NORTH) && direction == Direction.NORTH) {
                        state = state.setValue(BlockStateProperties.NORTH, true);
                    } else if (state.hasProperty(BlockStateProperties.SOUTH) && direction == Direction.SOUTH) {
                        state = state.setValue(BlockStateProperties.SOUTH, true);
                    } else if (state.hasProperty(BlockStateProperties.EAST) && direction == Direction.EAST) {
                        state = state.setValue(BlockStateProperties.EAST, true);
                    } else if (state.hasProperty(BlockStateProperties.WEST) && direction == Direction.WEST) {
                        state = state.setValue(BlockStateProperties.WEST, true);
                    } else if (state.hasProperty(BlockStateProperties.UP) && direction == Direction.UP) {
                        state = state.setValue(BlockStateProperties.UP, true);
                    } else if (state.hasProperty(BlockStateProperties.DOWN) && direction == Direction.DOWN) {
                        state = state.setValue(BlockStateProperties.DOWN, true);
                    }

                    branches.decrement();
                    generator.setBlock(offset, state);

                    if (branches.getValue() <= 0)
                        break;
                }
            }
        }
    }
}
