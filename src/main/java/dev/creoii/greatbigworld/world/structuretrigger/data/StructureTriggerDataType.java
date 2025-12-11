package dev.creoii.greatbigworld.world.structuretrigger.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public interface StructureTriggerDataType<T extends StructureTriggerData> {
    Codec<StructureTriggerData> CODEC = GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.byNameCodec().dispatch(StructureTriggerData::getType, StructureTriggerDataType::codec);

    StructureTriggerDataType<EmptyTriggerData> EMPTY = new StructureTriggerDataType<>() {
        @Override
        public MapCodec<EmptyTriggerData> codec() {
            return EmptyTriggerData.CODEC;
        }

        @Override
        public EmptyTriggerData create() {
            return new EmptyTriggerData();
        }
    };

    MapCodec<T> codec();

    T create();

    static void register() {
        Registry.register(GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "empty"), EMPTY);
    }
}
