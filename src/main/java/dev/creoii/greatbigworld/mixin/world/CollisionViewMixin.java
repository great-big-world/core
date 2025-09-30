package dev.creoii.greatbigworld.mixin.world;

import dev.creoii.greatbigworld.util.EntityBlockCollisionSpliterator;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.CollisionView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CollisionView.class)
public interface CollisionViewMixin {
    @Inject(method = "getBlockOrFluidCollisions(Lnet/minecraft/block/ShapeContext;Lnet/minecraft/util/math/Box;)Ljava/lang/Iterable;", at = @At("HEAD"), cancellable = true)
    private void gbw$entityBlockCollisions(ShapeContext shapeContext, Box box, CallbackInfoReturnable<Iterable<VoxelShape>> cir) {
        if (shapeContext instanceof EntityShapeContext entityShapeContext && entityShapeContext.getEntity() != null && ((CollisionView) this) instanceof World world) {
            EntityBlockCollisionSpliterator.INTERACTIONS.forEach((tagKey, predicate) -> {
                if (entityShapeContext.getEntity().getType().isIn(tagKey)) {
                    cir.setReturnValue(() -> new EntityBlockCollisionSpliterator(world, entityShapeContext.getEntity(), box, false, predicate));
                }
            });
        }
    }
}
