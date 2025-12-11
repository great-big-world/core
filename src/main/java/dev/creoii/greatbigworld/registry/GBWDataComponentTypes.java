package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.knowledge.KnowledgeComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class GBWDataComponentTypes {
    public static DataComponentType<KnowledgeComponent> KNOWLEDGE;

    public static void register() {
        KNOWLEDGE = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "knowledge"), DataComponentType.<KnowledgeComponent>builder().persistent(KnowledgeComponent.CODEC).networkSynchronized(KnowledgeComponent.PACKET_CODEC).build());
    }
}
