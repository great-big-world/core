package dev.creoii.greatbigworld.util;

import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

public interface ExtendedTexturesBuilder {
    void gbw$addDimensionSprite(String textureId, SpriteIdentifier spriteId, Identifier dimensionId);
}
