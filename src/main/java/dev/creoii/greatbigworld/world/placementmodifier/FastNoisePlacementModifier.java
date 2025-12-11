package dev.creoii.greatbigworld.world.placementmodifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.registry.GBWPlacementModifierTypes;
import dev.creoii.greatbigworld.world.fastnoise.FastNoiseLite;
import dev.creoii.greatbigworld.world.fastnoise.FastNoiseParameters;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class FastNoisePlacementModifier extends PlacementFilter {
    public static final MapCodec<FastNoisePlacementModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(FastNoiseParameters.REGISTRY_ENTRY_CODEC.fieldOf("noise").forGetter(predicate -> {
            return predicate.noise;
        }), Codec.DOUBLE.optionalFieldOf("min_threshold", -1d).forGetter(predicate -> {
            return predicate.minThreshold;
        }), Codec.DOUBLE.optionalFieldOf("max_threshold", 1d).forGetter(predicate -> {
            return predicate.maxThreshold;
        })).apply(instance, FastNoisePlacementModifier::new);
    });
    private final Holder<FastNoiseParameters> noise;
    private final double minThreshold;
    private final double maxThreshold;

    public FastNoisePlacementModifier(Holder<FastNoiseParameters> noise, double minThreshold, double maxThreshold) {
        this.noise = noise;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
    }

    @Override
    public PlacementModifierType<?> type() {
        return GBWPlacementModifierTypes.FAST_NOISE;
    }

    @Override
    public boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        if (!noise.isBound())
            return false;
        FastNoiseLite fastNoiseLite = new FastNoiseLite(noise.value());
        if (noise.value().seed() == 1337L)
            fastNoiseLite.seed(context.getLevel().getSeed());
        double noiseValue = fastNoiseLite.getNoise(pos.getX(), pos.getY(), pos.getZ());
        return noiseValue >= minThreshold && noiseValue <= maxThreshold;
    }
}