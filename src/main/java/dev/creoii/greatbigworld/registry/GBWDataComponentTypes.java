package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.knowledge.KnowledgeComponent;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class GBWDataComponentTypes {
    public static ComponentType<KnowledgeComponent> KNOWLEDGE;

    public static void register() {
        KNOWLEDGE = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "knowledge"), ComponentType.<KnowledgeComponent>builder().codec(KnowledgeComponent.CODEC).packetCodec(KnowledgeComponent.PACKET_CODEC).build());
    }
}
