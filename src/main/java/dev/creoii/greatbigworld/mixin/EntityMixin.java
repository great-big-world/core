package dev.creoii.greatbigworld.mixin;

import dev.creoii.greatbigworld.block.AdjacentCollision;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 2))
    private void gbw$collideAdjacentBlock(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        Optional<BlockPos> optionalPos = BlockPos.findClosest(entity.getBlockPos(), (int) (entity.getWidth() + .5f), (int) (entity.getHeight() + .5f), pos -> {
            return entity.getWorld().getBlockState(pos).getBlock() instanceof AdjacentCollision;
        });
        if (optionalPos.isPresent()) {
            BlockPos pos = optionalPos.get();
            BlockState state = entity.getWorld().getBlockState(pos);
            if (state.getBlock() instanceof AdjacentCollision adjacentCollision) {
                if (adjacentCollision.canEntityCollideAdjacent(entity, state, pos)) {
                    adjacentCollision.onAdjacentEntityCollision(entity, state, pos);
                }
            }
        }
    }
}
