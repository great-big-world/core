package dev.creoii.greatbigworld.mixin.entity;

import dev.creoii.greatbigworld.event.ItemEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "playerTouch", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;onItemPickup(Lnet/minecraft/world/entity/item/ItemEntity;)V"))
    private void gbw$itemPickupEvent(Player player, CallbackInfo ci) {
        ItemEvents.PICKUP.invoker().pickup(player, (ItemEntity) (Object) this);
    }
}
