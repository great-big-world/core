package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.MutableInt;

public final class GBWStructureTriggers {
    public static void register() {
        Registry.register(GreatBigWorld.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "empty"), new StructureTrigger((world, pos, state, structureStart, group) -> false));
        Registry.register(GreatBigWorld.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "debug"), new StructureTrigger((world, pos, state, structureStart, group) -> {
            if (world.getBlockState(pos).isOf(state.getBlock())) {
                String structureString = "";
                if (structureStart.isPresent()) {
                    structureString = Registries.STRUCTURE_TYPE.getId(structureStart.get().getStructure().getType()).toString();
                }
                GreatBigWorld.LOGGER.info("Structure Trigger as {} at: {} from structure {}", state.getRegistryEntry().getIdAsString(), pos.toShortString(), structureString);
                return true;
            }
            return false;
        }));
        Registry.register(GreatBigWorld.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "debug_group"), new StructureTrigger(Identifier.of("great_big_world:test"), (world, pos, state, structureStart, group) -> {
            if (group != null && group.getDataType() == StructureTriggerGroup.DataType.INT) {
                MutableInt mutableInt = (MutableInt) group.getData();
                mutableInt.increment();
                GreatBigWorld.LOGGER.info("Structure Trigger group with value {}", mutableInt.getValue());
                return mutableInt.getValue() < 4;
            }
            return false;
        }));
    }
}
