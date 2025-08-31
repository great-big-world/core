package dev.creoii.greatbigworld.registry;

import com.mojang.serialization.Lifecycle;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.data.Mappings;
import dev.creoii.greatbigworld.world.fastnoise.FastNoiseParameters;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleDefaultedRegistry;
import net.minecraft.util.Identifier;

public final class GBWRegistries {
    public static final RegistryKey<Registry<StructureTrigger>> STRUCTURE_TRIGGERS_KEY = RegistryKey.ofRegistry(Identifier.of(GreatBigWorld.NAMESPACE, "structure_triggers"));
    public static final Registry<StructureTrigger> STRUCTURE_TRIGGERS = new SimpleDefaultedRegistry<>("great_big_world:empty", STRUCTURE_TRIGGERS_KEY, Lifecycle.stable(), false);

    public static final RegistryKey<Registry<StructureTriggerDataType<?>>> STRUCTURE_TRIGGER_DATA_TYPES_KEY = RegistryKey.ofRegistry(Identifier.of(GreatBigWorld.NAMESPACE, "structure_trigger_data_types"));
    public static final Registry<StructureTriggerDataType<?>> STRUCTURE_TRIGGER_DATA_TYPES = new SimpleDefaultedRegistry<>("great_big_world:empty", STRUCTURE_TRIGGER_DATA_TYPES_KEY, Lifecycle.stable(), false);

    public static final RegistryKey<Registry<FastNoiseParameters>> FAST_NOISE_PARAMETERS_KEY = RegistryKey.ofRegistry(Identifier.of(GreatBigWorld.NAMESPACE, "worldgen/fast_noise"));

    public static final RegistryKey<Registry<Mappings>> MAPPINGS_KEY = RegistryKey.ofRegistry(Identifier.of(GreatBigWorld.NAMESPACE, "mappings"));

    public static void register() {
        DynamicRegistries.register(FAST_NOISE_PARAMETERS_KEY, FastNoiseParameters.CODEC);
        DynamicRegistries.register(MAPPINGS_KEY, Mappings.CODEC);
    }
}
