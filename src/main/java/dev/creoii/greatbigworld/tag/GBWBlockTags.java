package dev.creoii.greatbigworld.tag;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class GBWBlockTags {
    public static final TagKey<Block> PRECIPITATION_IGNORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "precipitation_ignores"));
}
