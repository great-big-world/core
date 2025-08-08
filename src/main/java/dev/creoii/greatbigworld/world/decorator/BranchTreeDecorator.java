package dev.creoii.greatbigworld.world.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.registry.GBWTreeDecoratorTypes;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.apache.commons.lang3.mutable.MutableInt;

public class BranchTreeDecorator extends TreeDecorator {
    public static final MapCodec<BranchTreeDecorator> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(BlockStateProvider.TYPE_CODEC.fieldOf("branch_provider").forGetter(config -> {
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
    protected TreeDecoratorType<?> getType() {
        return GBWTreeDecoratorTypes.BRANCH;
    }

    @Override
    public void generate(Generator generator) {
        MutableInt branches = new MutableInt(branchCount.get(generator.getRandom()));
        TestableWorld world = generator.getWorld();
        generator.getLogPositions().sort((logPos1, logPos2) -> {
            if (logPos1.getY() == logPos2.getY())
                return 0;
            return logPos1.getY() - logPos2.getY();
        });
        for (int i = minHeight + generator.getRandom().nextInt(2); i < generator.getLogPositions().size(); i += 2) {
            BlockPos pos = generator.getLogPositions().get(i);
            Direction direction = Direction.Type.HORIZONTAL.random(generator.getRandom());
            BlockPos offset = pos.offset(direction);
            if (world.testBlockState(offset, state -> state.isAir() || state.isReplaceable()) && generator.getRandom().nextFloat() <= .4f) {
                if (world.testBlockState(offset.offset(direction), state -> state.isAir() || state.isReplaceable())) {
                    BlockState state = branchProvider.get(generator.getRandom(), offset);

                    if (state.contains(Properties.AXIS)) {
                        state = state.with(Properties.AXIS, direction.getAxis());
                    } else if (state.contains(Properties.FACING)) {
                        state = state.with(Properties.FACING, direction);
                    } else if (state.contains(Properties.HORIZONTAL_AXIS) && direction.getAxis().isHorizontal()) {
                        state = state.with(Properties.HORIZONTAL_AXIS, direction.getAxis());
                    } else if (state.contains(Properties.HORIZONTAL_FACING) && direction.getAxis().isHorizontal()) {
                        state = state.with(Properties.HORIZONTAL_FACING, direction);
                    } else if (state.contains(Properties.NORTH) && direction == Direction.NORTH) {
                        state = state.with(Properties.NORTH, true);
                    } else if (state.contains(Properties.SOUTH) && direction == Direction.SOUTH) {
                        state = state.with(Properties.SOUTH, true);
                    } else if (state.contains(Properties.EAST) && direction == Direction.EAST) {
                        state = state.with(Properties.EAST, true);
                    } else if (state.contains(Properties.WEST) && direction == Direction.WEST) {
                        state = state.with(Properties.WEST, true);
                    } else if (state.contains(Properties.UP) && direction == Direction.UP) {
                        state = state.with(Properties.UP, true);
                    } else if (state.contains(Properties.DOWN) && direction == Direction.DOWN) {
                        state = state.with(Properties.DOWN, true);
                    }

                    branches.decrement();
                    generator.replace(offset, state);

                    if (branches.getValue() <= 0)
                        break;
                }
            }
        }
    }
}
