package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public final class GBWStructureTriggers {
    public static void register() {
        Registry.register(GBWRegistries.STRUCTURE_TRIGGERS, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "empty"), new StructureTrigger(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "empty"), (world, pos, state, structureStart, group) -> false));
    }
}
