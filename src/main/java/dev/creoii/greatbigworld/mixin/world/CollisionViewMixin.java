package dev.creoii.greatbigworld.mixin.world;

import dev.creoii.greatbigworld.util.EntityBlockCollisionSpliterator;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CollisionGetter.class)
public interface CollisionViewMixin {
    @Inject(method = "getBlockCollisionsFromContext(Lnet/minecraft/world/phys/shapes/CollisionContext;Lnet/minecraft/world/phys/AABB;)Ljava/lang/Iterable;", at = @At("HEAD"), cancellable = true)
    private void gbw$entityBlockCollisions(CollisionContext shapeContext, AABB box, CallbackInfoReturnable<Iterable<VoxelShape>> cir) {
        if (shapeContext instanceof EntityCollisionContext entityShapeContext && entityShapeContext.getEntity() != null && ((CollisionGetter) this) instanceof Level world) {
            EntityBlockCollisionSpliterator.INTERACTIONS.forEach((tagKey, predicate) -> {
                if (entityShapeContext.getEntity().getType().is(tagKey)) {
                    cir.setReturnValue(() -> new EntityBlockCollisionSpliterator(world, entityShapeContext.getEntity(), box, false, predicate));
                }
            });
        }
    }
}
