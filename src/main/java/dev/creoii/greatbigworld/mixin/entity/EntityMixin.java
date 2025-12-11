package dev.creoii.greatbigworld.mixin.entity;

import dev.creoii.greatbigworld.block.AdjacentCollision;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow private Level level;
    @Shadow public abstract UUID getUUID();
    @Shadow public abstract boolean isAlwaysTicking();

    @Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 2))
    private void gbw$collideAdjacentBlock(MoverType movementType, Vec3 movement, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        Optional<BlockPos> optionalPos = BlockPos.findClosestMatch(entity.blockPosition(), (int) (entity.getBbWidth() + .5f), (int) (entity.getBbHeight() + .5f), pos -> {
            return entity.level().getBlockState(pos).getBlock() instanceof AdjacentCollision;
        });
        if (optionalPos.isPresent()) {
            BlockPos pos = optionalPos.get();
            WorldBorder worldBorder = entity.level().getWorldBorder();
            if (!worldBorder.isWithinBounds(pos))
                return;
            BlockState state = entity.level().getBlockState(pos);
            if (state.getBlock() instanceof AdjacentCollision adjacentCollision) {
                adjacentCollision.onAdjacentEntityCollision(entity, state, pos);
            }
        }
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void gbw$cleanPreviousDimensions(Entity.RemovalReason reason, CallbackInfo ci) {
        if (level.getServer() != null) {
            PreviousDimensionManager manager = PreviousDimensionManager.getServerState(level.getServer());
            if (reason.shouldDestroy() && !isAlwaysTicking()) {
                manager.removePrev(getUUID());
                manager.removeTo(getUUID());
            }
        }
    }

    @Inject(method = "teleportCrossDimension", at = @At("HEAD"))
    private void gbw$updatePrevDimensionManager(ServerLevel from, ServerLevel to, TeleportTransition teleportTarget, CallbackInfoReturnable<Entity> cir) {
        PreviousDimensionManager manager = PreviousDimensionManager.getServerState(to.getServer());
        manager.setPrevDimension(getUUID(), from.dimension());
        manager.setToDimension(getUUID(), to.dimension());
    }
}
