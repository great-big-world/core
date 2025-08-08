package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class GBWBlockEntityTypes {
    public static BlockEntityType<StructureTriggerBlockEntity> STRUCTURE_TRIGGER;

    public static void register() {
        STRUCTURE_TRIGGER = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "structure_trigger"), FabricBlockEntityTypeBuilder.create(StructureTriggerBlockEntity::new, GBWBlocks.STRUCTURE_TRIGGER).build());
    }
}
