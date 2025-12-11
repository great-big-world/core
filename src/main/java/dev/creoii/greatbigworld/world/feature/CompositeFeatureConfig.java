package dev.creoii.greatbigworld.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record CompositeFeatureConfig(List<Holder<PlacedFeature>> features, CompositeFeature.Type type) implements FeatureConfiguration {
    public static final Codec<CompositeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(PlacedFeature.CODEC.listOf().fieldOf("features").forGetter(config -> {
            return config.features;
        }), CompositeFeature.Type.CODEC.fieldOf("type").orElse(CompositeFeature.Type.FREE).forGetter(config -> {
            return config.type;
        })).apply(instance, CompositeFeatureConfig::new);
    });
}
