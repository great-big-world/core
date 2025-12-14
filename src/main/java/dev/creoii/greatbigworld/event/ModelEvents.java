package dev.creoii.greatbigworld.event;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;

public class ModelEvents {
    public static final Event<RegisterLayerDefinitions> REGISTER_LAYER_DEFINITION = EventFactory.createArrayBacked(RegisterLayerDefinitions.class, (callbacks) -> (builder) -> {
        for (RegisterLayerDefinitions callback : callbacks) {
            callback.register(builder);
        }
    });

    public static ModelLayerLocation registerModelLayer(Identifier id, String layer) {
        ModelLayerLocation modelLayerLocation = new ModelLayerLocation(id, layer);
        if (!ModelLayers.ALL_MODELS.add(modelLayerLocation)) {
            throw new IllegalStateException("Duplicate registration for " + modelLayerLocation);
        } else {
            return modelLayerLocation;
        }
    }

    @FunctionalInterface
    public interface RegisterLayerDefinitions {
        void register(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder);
    }
}
