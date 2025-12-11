package dev.creoii.greatbigworld.world.feature;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class VeinOreFeature extends Feature<OreConfiguration> {
    public VeinOreFeature(Codec<OreConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> context) {
        final WorldGenLevel world = context.level();
        final RandomSource random = context.random();
        final BlockPos origin = context.origin();
        final OreConfiguration config = context.config();

        Direction primary = pickPrimaryDirection(random);
        Direction secondary = pickSecondaryDirection(random, primary);

        Direction current = primary;
        final float turnChance = 0.20f;
        final float jitterChance = 0.10f;

        BlockPos.MutableBlockPos cursor = origin.mutable();
        java.util.ArrayList<BlockPos> veinPath = new java.util.ArrayList<>(config.size);

        veinPath.add(cursor.immutable());

        for (int i = 1; i < config.size; i++) {
            if (random.nextFloat() < turnChance) {
                current = (current == primary) ? secondary : primary;
            }

            cursor.move(current);
            BlockPos stepped = cursor.immutable();
            veinPath.add(stepped);

            if (random.nextFloat() < jitterChance) {
                Direction nudge = pickPerpendicularNudge(random, current);
                if (nudge != null) {
                    cursor.move(nudge);
                    veinPath.add(cursor.immutable());
                }
            }
        }

        boolean placed = false;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (BlockPos pos : veinPath) {
            if (!world.ensureCanWrite(pos))
                continue;

            mutable.set(pos);
            BlockState blockState = world.getBlockState(mutable);

            for (OreConfiguration.TargetBlockState target : config.targetStates) {
                if (OreFeature.canPlaceOre(blockState, world::getBlockState, random, config, target, mutable) && world.setBlock(mutable, target.state, 3)) {
                    placed = true;
                    break;
                }
            }
        }

        return placed;
    }

    private static Direction pickPrimaryDirection(RandomSource random) {
        if (random.nextFloat() < .80f) {
            return Direction.Plane.HORIZONTAL.getRandomDirection(random);
        } else {
            return random.nextBoolean() ? Direction.UP : Direction.DOWN;
        }
    }

    private static Direction pickSecondaryDirection(RandomSource random, Direction primary) {
        if (random.nextFloat() >= .50f)
            return null;

        ArrayList<Direction> candidates = new ArrayList<>(4);
        for (Direction d : Direction.values()) {
            if (d == primary || d == primary.getOpposite())
                continue;
            if (d.getAxis() != primary.getAxis())
                candidates.add(d);
        }

        if (candidates.isEmpty())
            return null;
        return candidates.get(random.nextInt(candidates.size()));
    }

    private static Direction pickPerpendicularNudge(RandomSource random, Direction current) {
        ArrayList<Direction> options = new ArrayList<>(4);
        for (Direction d : Direction.values()) {
            if (d.getAxis() == current.getAxis() || d == current.getOpposite())
                continue;
            options.add(d);
        }

        if (options.isEmpty())
            return null;
        return options.get(random.nextInt(options.size()));
    }
}
