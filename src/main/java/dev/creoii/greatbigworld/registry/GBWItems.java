package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public final class GBWItems {
    public static Item STRUCTURE_TRIGGER;

    public static void register() {
        STRUCTURE_TRIGGER = RegistryHelper.registerBlockItem(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "structure_trigger"), GBWBlocks.STRUCTURE_TRIGGER, new Item.Properties().rarity(Rarity.EPIC));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.OP_BLOCKS).register(entries -> {
            entries.addAfter(Items.JIGSAW, STRUCTURE_TRIGGER);
        });
    }
}
