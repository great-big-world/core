package dev.creoii.greatbigworld.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public record DimensionSpriteEntry(SpriteIdentifier material, Identifier dimensionId) implements ModelTextures.Entry {
}
