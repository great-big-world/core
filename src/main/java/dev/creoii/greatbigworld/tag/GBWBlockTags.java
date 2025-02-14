package dev.creoii.greatbigworld.tag;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public final class GBWBlockTags {
    public static final TagKey<Block> WEATHER_RENDER_IGNORES = TagKey.of(RegistryKeys.BLOCK, Identifier.of(GreatBigWorld.NAMESPACE, "weather_render_ignores"));
}
