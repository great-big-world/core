package dev.creoii.greatbigworld.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class InventoryEvents {
    public static final Event<InventoryChanged> SLOTS_CHANGED = EventFactory.createArrayBacked(InventoryChanged.class, (callbacks) -> (menu,  inventory, slotIndex, itemStack) -> {
        for (InventoryChanged callback : callbacks) {
            callback.slotsChanged(menu,  inventory, slotIndex, itemStack);
        }
    });

    @FunctionalInterface
    public interface InventoryChanged {
        void slotsChanged(AbstractContainerMenu abstractContainerMenu, Inventory inventory, int slotIndex, ItemStack itemStack);
    }
}
