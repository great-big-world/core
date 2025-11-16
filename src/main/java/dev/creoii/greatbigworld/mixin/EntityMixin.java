package dev.creoii.greatbigworld.mixin;

import dev.creoii.greatbigworld.block.AdjacentCollision;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow private World world;
    @Shadow public abstract UUID getUuid();
    @Shadow public abstract boolean isPlayer();

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;pop()V", ordinal = 2))
    private void gbw$collideAdjacentBlock(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        Optional<BlockPos> optionalPos = BlockPos.findClosest(entity.getBlockPos(), (int) (entity.getWidth() + .5f), (int) (entity.getHeight() + .5f), pos -> {
            return entity.getEntityWorld().getBlockState(pos).getBlock() instanceof AdjacentCollision;
        });
        if (optionalPos.isPresent()) {
            BlockPos pos = optionalPos.get();
            WorldBorder worldBorder = entity.getEntityWorld().getWorldBorder();
            if (!worldBorder.contains(pos))
                return;
            BlockState state = entity.getEntityWorld().getBlockState(pos);
            if (state.getBlock() instanceof AdjacentCollision adjacentCollision) {
                adjacentCollision.onAdjacentEntityCollision(entity, state, pos);
            }
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void gbw$cleanPreviousDimensions(Entity.RemovalReason reason, CallbackInfo ci) {
        if (world.getServer() != null) {
            PreviousDimensionManager manager = PreviousDimensionManager.getServerState(world.getServer());
            if (reason.shouldDestroy() && !isPlayer()) {
                manager.remove(getUuid());
            } else if (reason == Entity.RemovalReason.CHANGED_DIMENSION) {
                manager.setPrevDimension(getUuid(), world.getRegistryKey());
            }
        }
    }
}
