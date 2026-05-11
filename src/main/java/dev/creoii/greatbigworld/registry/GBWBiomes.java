package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public final class GBWBiomes {
    public static final ResourceKey<Biome> ALTER_TAIGA = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "alter_taiga"));
    public static final ResourceKey<Biome> ALTER_SNOWY_TAIGA = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "alter_snowy_taiga"));
}
