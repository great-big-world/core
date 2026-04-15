package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.feature.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public final class GBWFeatures {
    public static void register() {
        Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ore_vein"), new VeinOreFeature(OreConfiguration.CODEC));
        Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "composite"), new CompositeFeature(CompositeFeatureConfig.CODEC));
        Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "pool"), new PoolFeature(PoolFeatureConfig.CODEC));
    }
}
