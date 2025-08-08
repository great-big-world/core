package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class GBWStructureTriggers {
    public static void register() {
        Registry.register(GreatBigWorld.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "empty"), new StructureTrigger((world, pos, state, structureStart) -> false));
        Registry.register(GreatBigWorld.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "debug"), new StructureTrigger((world, pos, state, structureStart) -> {
            if (world.getBlockState(pos).isOf(state.getBlock())) {
                String structureString = "";
                if (structureStart.isPresent()) {
                    structureString = Registries.STRUCTURE_TYPE.getId(structureStart.get().getStructure().getType()).toString();
                }
                GreatBigWorld.LOGGER.debug("Structure Trigger as {} at: {} from structure {}", state.getRegistryEntry().getIdAsString(), pos.toShortString(), structureString);
                return true;
            }
            return false;
        }));
    }
}
