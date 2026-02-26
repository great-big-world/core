package dev.creoii.greatbigworld.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.event.ItemEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public class BrushItemMixin {
    @Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V", shift = At.Shift.AFTER))
    private void gbw$brushCavePaintings(Level level, LivingEntity livingEntity, ItemStack itemStack, int i, CallbackInfo ci, @Local Player player, @Local(ordinal = 1) int j, @Local BlockState blockState, @Local BlockPos blockPos) {
        ItemEvents.BRUSH.invoker().brush(level, player, itemStack, blockState, blockPos, j);
    }
}
