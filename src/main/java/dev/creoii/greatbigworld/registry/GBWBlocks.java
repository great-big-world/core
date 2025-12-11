package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class GBWBlocks {
    public static Block STRUCTURE_TRIGGER;

    public static void register() {
        STRUCTURE_TRIGGER = RegistryHelper.registerBlock(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "structure_trigger"), StructureTriggerBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.JIGSAW));
    }
}
