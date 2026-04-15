package dev.creoii.greatbigworld.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

import java.util.Arrays;
import java.util.List;

public class PoolFeature extends Feature<PoolFeatureConfig> {
    public PoolFeature(Codec<PoolFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PoolFeatureConfig> featurePlaceContext) {
        WorldGenLevel level = featurePlaceContext.level();
        RandomSource random = featurePlaceContext.random();
        PoolFeatureConfig config = featurePlaceContext.config();
        BlockPos origin = featurePlaceContext.origin().relative(Direction.Axis.Y, config.yOffset());
        PerlinNoise noiseSampler = PerlinNoise.create(random, List.of(1, 2, 3));

        int height = config.height().sample(random);
        int radius = config.startRadius().sample(random);
        int rimSize = config.rimSize().sample(random);
        int innerDepth = Math.min(config.innerDepth().sample(random), height);
        for (int y = 0; y < height; ++y) {
            double scale = (y / (double) height) * 5;
            for (int x = -radius; x <= radius; ++x) {
                for (int z = -radius; z <= radius; ++z) {
                    BlockPos pos = origin.offset(x, y, z);
                    double distance = Math.sqrt(x * x + z * z);
                    double radiusMod = radius + noiseSampler.getValue(x * scale, y, z * scale);
                    if (distance < radiusMod - rimSize && y > 0 && y <= innerDepth) {
                        level.setBlock(pos, config.innerState().getState(random, pos), 3);
                        if (config.solidRim()) {
                            BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
                            Arrays.stream(Direction.values()).filter(direction -> direction != Direction.UP).forEach(direction -> {
                                offset.set(pos.relative(direction));
                                if (level.isEmptyBlock(offset)) {
                                    level.setBlock(offset, config.rimState().getState(random, offset), 3);
                                }
                            });
                        }
                    } else if (distance < radiusMod) {
                        level.setBlock(pos, config.rimState().getState(random, pos), 3);
                    }
                }
            }
            radius += random.nextBoolean() ? 1 : 0;
        }
        return true;
    }
}