package dev.creoii.greatbigworld.mixin.client.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.creoii.greatbigworld.util.ExtendedTexturesBuilder;
import net.minecraft.client.render.model.ModelTextures;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelTextures.class)
public class ModelTexturesMixin {
    @Shadow
    private static void add(Identifier atlasTexture, String textureId, String value, ModelTextures.Textures.Builder builder) {
        throw new UnsupportedOperationException();
    }

    @Inject(method = "fromJson", at = @At("HEAD"), cancellable = true)
    private static void gbw$loadDimensionModelTextures(JsonObject json, Identifier atlasTexture, CallbackInfoReturnable<ModelTextures.Textures> cir) {
        ModelTextures.Textures.Builder builder = new ModelTextures.Textures.Builder();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getValue().isJsonObject()) {
                addObj(atlasTexture, entry.getKey(), entry.getValue().getAsJsonObject(), builder);
            } else add(atlasTexture, entry.getKey(), entry.getValue().getAsString(), builder);
        }

        cir.setReturnValue(builder.build());
    }

    @Unique
    private static void addObj(Identifier atlasTexture, String textureId, JsonObject value, ModelTextures.Textures.Builder builder) {
        value.entrySet().forEach(entry -> {
            Identifier dimensionId = Identifier.tryParse(entry.getKey());
            if (dimensionId == null) {
                throw new JsonParseException(entry.getKey() + " is not valid resource location");
            }
            Identifier identifier = Identifier.tryParse(entry.getValue().getAsString());
            if (identifier == null) {
                throw new JsonParseException(entry.getValue().getAsString() + " is not valid resource location");
            }
            ((ExtendedTexturesBuilder) builder).gbw$addDimensionSprite(textureId, new SpriteIdentifier(atlasTexture, identifier), dimensionId);
        });
    }
}
