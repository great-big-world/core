package dev.creoii.greatbigworld.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;

public class VeinOreFeature extends Feature<OreFeatureConfig> {
    public VeinOreFeature(Codec<OreFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<OreFeatureConfig> context) {
        final StructureWorldAccess world = context.getWorld();
        final Random random = context.getRandom();
        final BlockPos origin = context.getOrigin();
        final OreFeatureConfig config = context.getConfig();

        Direction primary = pickPrimaryDirection(random);
        Direction secondary = pickSecondaryDirection(random, primary);

        Direction current = primary;
        final float turnChance = 0.20f;
        final float jitterChance = 0.10f;

        BlockPos.Mutable cursor = origin.mutableCopy();
        java.util.ArrayList<BlockPos> veinPath = new java.util.ArrayList<>(config.size);

        veinPath.add(cursor.toImmutable());

        for (int i = 1; i < config.size; i++) {
            if (random.nextFloat() < turnChance) {
                current = (current == primary) ? secondary : primary;
            }

            cursor.move(current);
            BlockPos stepped = cursor.toImmutable();
            veinPath.add(stepped);

            if (random.nextFloat() < jitterChance) {
                Direction nudge = pickPerpendicularNudge(random, current);
                if (nudge != null) {
                    cursor.move(nudge);
                    veinPath.add(cursor.toImmutable());
                }
            }
        }

        boolean placed = false;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (BlockPos pos : veinPath) {
            if (!world.isValidForSetBlock(pos))
                continue;

            mutable.set(pos);
            BlockState blockState = world.getBlockState(mutable);

            for (OreFeatureConfig.Target target : config.targets) {
                if (OreFeature.shouldPlace(blockState, world::getBlockState, random, config, target, mutable) && world.setBlockState(mutable, target.state, 3)) {
                    placed = true;
                    break;
                }
            }
        }

        return placed;
    }

    private static Direction pickPrimaryDirection(Random random) {
        if (random.nextFloat() < .80f) {
            return Direction.Type.HORIZONTAL.random(random);
        } else {
            return random.nextBoolean() ? Direction.UP : Direction.DOWN;
        }
    }

    private static Direction pickSecondaryDirection(Random random, Direction primary) {
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

    private static Direction pickPerpendicularNudge(Random random, Direction current) {
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
