package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.feature.CompositeFeature;
import dev.creoii.greatbigworld.world.feature.CompositeFeatureConfig;
import dev.creoii.greatbigworld.world.feature.VeinOreFeature;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public final class GBWFeatures {
    public static void register() {
        Registry.register(Registries.FEATURE, Identifier.of(GreatBigWorld.NAMESPACE, "ore_vein"), new VeinOreFeature(OreFeatureConfig.CODEC));
        Registry.register(Registries.FEATURE, Identifier.of(GreatBigWorld.NAMESPACE, "composite"), new CompositeFeature(CompositeFeatureConfig.CODEC));
    }
}
