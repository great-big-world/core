package dev.creoii.greatbigworld.tag;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public final class GBWBiomeTags {
    public static final TagKey<Biome> IS_WARM_OCEAN = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "is_warm_ocean"));
    public static final TagKey<Biome> IS_DEEP_DARK = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "is_deep_dark"));
}
