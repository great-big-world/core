package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.entity.KnowledgeBlockEntity;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class GBWBlockEntityTypes {
    public static BlockEntityType<StructureTriggerBlockEntity> STRUCTURE_TRIGGER;
    public static BlockEntityType<KnowledgeBlockEntity> KNOWLEDGE_BLOCK;

    public static void register() {
        STRUCTURE_TRIGGER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "structure_trigger"), FabricBlockEntityTypeBuilder.create(StructureTriggerBlockEntity::new, GBWBlocks.STRUCTURE_TRIGGER).build());
        KNOWLEDGE_BLOCK = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "knowledge_block"), FabricBlockEntityTypeBuilder.create(KnowledgeBlockEntity::new).build());
    }
}
