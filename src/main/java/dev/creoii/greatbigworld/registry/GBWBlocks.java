package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

public final class GBWBlocks {
    public static Block STRUCTURE_TRIGGER;

    public static void register() {
        STRUCTURE_TRIGGER = RegistryHelper.registerBlock(Identifier.of(GreatBigWorld.NAMESPACE, "structure_trigger"), StructureTriggerBlock::new, AbstractBlock.Settings.copy(Blocks.JIGSAW));
    }
}
