package dev.creoii.greatbigworld.mixin.entity;

import dev.creoii.greatbigworld.event.ItemEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;triggerItemPickedUpByEntityCriteria(Lnet/minecraft/entity/ItemEntity;)V"))
    private void gbw$itemPickupEvent(PlayerEntity player, CallbackInfo ci) {
        ItemEvents.PICKUP.invoker().pickup(player, (ItemEntity) (Object) this);
    }
}
