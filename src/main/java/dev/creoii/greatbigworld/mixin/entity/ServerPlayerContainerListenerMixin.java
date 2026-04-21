package dev.creoii.greatbigworld.mixin.entity;

import dev.creoii.greatbigworld.event.InventoryEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.server.level.ServerPlayer$2")
public abstract class ServerPlayerContainerListenerMixin {
    @Inject(method = "slotChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/criterion/InventoryChangeTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V"))
    private void gbw$updatePrevDimensionManager(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack, CallbackInfo ci) {
        Slot slot = abstractContainerMenu.getSlot(i);

        if (slot.container instanceof Inventory inventory) {
            InventoryEvents.SLOTS_CHANGED.invoker().slotsChanged(abstractContainerMenu, inventory, i, itemStack);
        }
    }
}
