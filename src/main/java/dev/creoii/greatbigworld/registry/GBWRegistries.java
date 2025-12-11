package dev.creoii.greatbigworld.registry;

import com.mojang.serialization.Lifecycle;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.data.Mappings;
import dev.creoii.greatbigworld.world.fastnoise.FastNoiseParameters;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public final class GBWRegistries {
    public static final ResourceKey<Registry<StructureTrigger>> STRUCTURE_TRIGGERS_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "structure_triggers"));
    public static final Registry<StructureTrigger> STRUCTURE_TRIGGERS = new DefaultedMappedRegistry<>("great_big_world:empty", STRUCTURE_TRIGGERS_KEY, Lifecycle.stable(), false);

    public static final ResourceKey<Registry<StructureTriggerDataType<?>>> STRUCTURE_TRIGGER_DATA_TYPES_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "structure_trigger_data_types"));
    public static final Registry<StructureTriggerDataType<?>> STRUCTURE_TRIGGER_DATA_TYPES = new DefaultedMappedRegistry<>("great_big_world:empty", STRUCTURE_TRIGGER_DATA_TYPES_KEY, Lifecycle.stable(), false);

    public static final ResourceKey<Registry<FastNoiseParameters>> FAST_NOISE_PARAMETERS_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "worldgen/fast_noise"));

    public static final ResourceKey<Registry<Mappings>> MAPPINGS_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "mappings"));

    public static void register() {
        DynamicRegistries.register(FAST_NOISE_PARAMETERS_KEY, FastNoiseParameters.CODEC);
        DynamicRegistries.register(MAPPINGS_KEY, Mappings.CODEC);
    }
}
