package dev.creoii.greatbigworld.world.placementmodifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.registry.GBWPlacementModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class NoisePlacementModifier extends PlacementFilter {
        public static final MapCodec<NoisePlacementModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(predicate -> {
            return predicate.noise;
        }), Codec.DOUBLE.optionalFieldOf("min_threshold", -1d).forGetter(predicate -> {
            return predicate.minThreshold;
        }), Codec.DOUBLE.optionalFieldOf("max_threshold", 1d).forGetter(predicate -> {
            return predicate.maxThreshold;
        })).apply(instance, NoisePlacementModifier::new);
    });
    private final ResourceKey<NormalNoise.NoiseParameters> noise;
    private final double minThreshold;
    private final double maxThreshold;

    public NoisePlacementModifier(ResourceKey<NormalNoise.NoiseParameters> noise, double minThreshold, double maxThreshold) {
        this.noise = noise;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
    }

    @Override
    public PlacementModifierType<?> type() {
        return GBWPlacementModifierTypes.NOISE;
    }

    @Override
    public boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        if (context.getLevel().getChunkSource() instanceof ServerChunkCache chunkManager) {
            NormalNoise sampler = chunkManager.randomState().getOrCreateNoise(noise);
            double noiseValue = sampler.getValue(pos.getX(), pos.getY(), pos.getZ());
            return noiseValue >= minThreshold && noiseValue <= maxThreshold;
        }
        return false;
    }
}