package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.util.RegistryHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class GBWItems {
    public static Item STRUCTURE_TRIGGER;

    public static void register() {
        STRUCTURE_TRIGGER = RegistryHelper.registerBlockItem(Identifier.of(GreatBigWorld.NAMESPACE, "structure_trigger"), GBWBlocks.STRUCTURE_TRIGGER, new Item.Settings().rarity(Rarity.EPIC));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(entries -> {
            entries.addAfter(Items.JIGSAW, STRUCTURE_TRIGGER);
        });
    }
}
