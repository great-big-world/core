package dev.creoii.greatbigworld.mixin.client.model;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.client.DimensionSpriteEntry;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.function.BiConsumer;

@Mixin(ModelTextures.Builder.class)
public class ModelTexturesBuilderMixin {
    @Redirect(method = "build", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"))
    private <K extends String, V extends ModelTextures.Entry> void gbw$buildWithDimensionEntry(Map<String, ModelTextures.Entry> instance, BiConsumer<? super K, ? super V> v, @Local(ordinal = 0) Object2ObjectMap<String, SpriteIdentifier> object2ObjectMap, @Local(ordinal = 1) Object2ObjectMap<String, ModelTextures.TextureReferenceEntry> object2ObjectMap2) {
        instance.forEach((textureId, entryx) -> {
            switch (entryx) {
                case ModelTextures.SpriteEntry spriteEntry:
                    object2ObjectMap2.remove(textureId);
                    object2ObjectMap.put(textureId, spriteEntry.material());
                    break;
                case ModelTextures.TextureReferenceEntry textureReferenceEntry:
                    object2ObjectMap.remove(textureId);
                    object2ObjectMap2.put(textureId, textureReferenceEntry);
                    break;
                case DimensionSpriteEntry spriteEntry:
                    String newTextureId = spriteEntry.dimensionId().toString() + "/" + textureId;
                    object2ObjectMap2.remove(newTextureId);
                    object2ObjectMap.put(newTextureId, spriteEntry.material());
                    break;
                case null, default:
                    break;
            }
        });
    }
}
