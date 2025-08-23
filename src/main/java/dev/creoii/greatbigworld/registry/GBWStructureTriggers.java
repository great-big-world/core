package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class GBWStructureTriggers {
    public static void register() {
        Registry.register(GBWRegistries.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "empty"), new StructureTrigger(Identifier.of(GreatBigWorld.NAMESPACE, "empty"), (world, pos, state, structureStart, group) -> false));
    }
}
