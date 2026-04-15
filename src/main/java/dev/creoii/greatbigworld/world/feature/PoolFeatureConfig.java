package dev.creoii.greatbigworld.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record PoolFeatureConfig(int yOffset, IntProvider rimSize, BlockStateProvider rimState, BlockStateProvider innerState, IntProvider height, IntProvider startRadius, IntProvider innerDepth, TagKey<Block> replaceable, boolean solidRim) implements FeatureConfiguration {
    public static final Codec<PoolFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(Codec.INT.fieldOf("y_offset").orElse(0).forGetter(config -> {
            return config.yOffset;
        }), IntProvider.codec(0, 16).fieldOf("rim_size").forGetter(config -> {
            return config.rimSize;
        }), BlockStateProvider.CODEC.fieldOf("rim_state").forGetter(config -> {
            return config.rimState;
        }), BlockStateProvider.CODEC.fieldOf("inner_state").forGetter(config -> {
            return config.innerState;
        }), IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(config -> {
            return config.height;
        }), IntProvider.POSITIVE_CODEC.fieldOf("start_radius").forGetter(config -> {
            return config.startRadius;
        }), IntProvider.POSITIVE_CODEC.fieldOf("inner_depth").forGetter(config -> {
            return config.innerDepth;
        }), TagKey.codec(Registries.BLOCK).fieldOf("replaceable").orElse(BlockTags.REPLACEABLE).forGetter(config -> {
            return config.replaceable;
        }), Codec.BOOL.fieldOf("solid_rim").orElse(false).forGetter(config -> {
            return config.solidRim;
        })).apply(instance, PoolFeatureConfig::new);
    });
}