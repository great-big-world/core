package dev.creoii.greatbigworld.mixin.compat;

import dev.creoii.greatbigworld.util.EntityBlockCollisionSpliterator;
import dev.creoii.greatbigworld.util.compat.LithiumEntityBlockCollisionSpliterator;
import net.caffeinemc.mods.lithium.common.entity.LithiumEntityCollisions;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LithiumEntityCollisions.class)
public class LithiumEntityCollisionsMixin {
    @Inject(method = "getBlockCollisions(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void gbw$lithiumCollisionCompat(World world, Entity entity, Box box, CallbackInfoReturnable<List<VoxelShape>> cir) {
        if (entity != null) {
            EntityBlockCollisionSpliterator.INTERACTIONS.forEach((tagKey, predicate) -> {
                if (entity.getType().isIn(tagKey)) {
                    cir.setReturnValue(new LithiumEntityBlockCollisionSpliterator(world, entity, box, false, predicate).collectAll());
                }
            });
        }
    }

    @Inject(method = "doesBoxCollideWithBlocks", at = @At("HEAD"), cancellable = true)
    private static void gbw$lithiumCollisionCompat1(World world, Entity entity, Box box, CallbackInfoReturnable<Boolean> cir) {
        if (entity != null) {
            EntityBlockCollisionSpliterator.INTERACTIONS.forEach((tagKey, predicate) -> {
                if (entity.getType().isIn(tagKey)) {
                    LithiumEntityBlockCollisionSpliterator spliterator = new LithiumEntityBlockCollisionSpliterator(world, entity, box, false, predicate);
                    VoxelShape shape = spliterator.computeNext();
                    cir.setReturnValue(shape != null && !shape.isEmpty());
                }
            });
        }
    }
}