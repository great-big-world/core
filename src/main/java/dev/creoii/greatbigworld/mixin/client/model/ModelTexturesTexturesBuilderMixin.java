package dev.creoii.greatbigworld.mixin.client.model;

import dev.creoii.greatbigworld.client.DimensionSpriteEntry;
import dev.creoii.greatbigworld.util.ExtendedTexturesBuilder;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ModelTextures.Textures.Builder.class)
public class ModelTexturesTexturesBuilderMixin implements ExtendedTexturesBuilder {
    @Shadow @Final private Map<String, ModelTextures.Entry> entries;

    @Override
    public void gbw$addDimensionSprite(String textureId, SpriteIdentifier spriteId, Identifier dimensionId) {
        entries.put(textureId, new DimensionSpriteEntry(spriteId, dimensionId));
    }
}
