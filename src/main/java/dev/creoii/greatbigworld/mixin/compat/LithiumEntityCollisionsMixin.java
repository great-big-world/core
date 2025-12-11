package dev.creoii.greatbigworld.mixin.compat;

import dev.creoii.greatbigworld.util.EntityBlockCollisionSpliterator;
import dev.creoii.greatbigworld.util.compat.LithiumEntityBlockCollisionSpliterator;
import net.caffeinemc.mods.lithium.common.entity.LithiumEntityCollisions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LithiumEntityCollisions.class)
public class LithiumEntityCollisionsMixin {
    @Inject(method = "getBlockCollisions(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void gbw$lithiumCollisionCompat(Level world, Entity entity, AABB box, CallbackInfoReturnable<List<VoxelShape>> cir) {
        if (entity != null) {
            EntityBlockCollisionSpliterator.INTERACTIONS.forEach((tagKey, predicate) -> {
                if (entity.getType().is(tagKey)) {
                    cir.setReturnValue(new LithiumEntityBlockCollisionSpliterator(world, entity, box, false, predicate).collectAll());
                }
            });
        }
    }

    @Inject(method = "doesBoxCollideWithBlocks", at = @At("HEAD"), cancellable = true)
    private static void gbw$lithiumCollideWithBlocksCompat(Level world, Entity entity, AABB box, CallbackInfoReturnable<Boolean> cir) {
        if (entity != null) {
            EntityBlockCollisionSpliterator.INTERACTIONS.forEach((tagKey, predicate) -> {
                if (entity.getType().is(tagKey)) {
                    LithiumEntityBlockCollisionSpliterator spliterator = new LithiumEntityBlockCollisionSpliterator(world, entity, box, false, predicate);
                    VoxelShape shape = spliterator.computeNext();
                    cir.setReturnValue(shape != null && !shape.isEmpty());
                }
            });
        }
    }
}