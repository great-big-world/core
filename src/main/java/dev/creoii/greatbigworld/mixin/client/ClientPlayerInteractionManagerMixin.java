package dev.creoii.greatbigworld.mixin.client;

import dev.creoii.greatbigworld.item.UseThroughBlock;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "attackBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameMode;isCreative()Z"))
    private void creo$tryAttackThroughBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (client.player != null && client.getCameraEntity() != null) {
            double d = client.player.getEntityInteractionRange();
            Vec3d vec3d = client.getCameraEntity().getCameraPosVec(1f);
            Vec3d vec3d2 = client.getCameraEntity().getRotationVec(1f);
            EntityHitResult xrayResult = ProjectileUtil.raycast(client.getCameraEntity(), vec3d, vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d), client.getCameraEntity().getBoundingBox().stretch(vec3d2.multiply(d)).expand(1d, 1d, 1d), (entityx) -> {
                return !entityx.isSpectator() && entityx.canHit();
            }, d);
            if (client.player != null && xrayResult != null) {
                ItemStack stack = client.player.getStackInHand(client.player.getActiveHand());
                if (stack.getItem() instanceof UseThroughBlock useThroughBlock && useThroughBlock.canAttackThroughBlock(client.player, stack, xrayResult.getEntity())) {
                    useThroughBlock.onAttackThroughBlock(client.player, stack, xrayResult.getEntity());
                    ClientPlayNetworking.send(new UseThroughBlock.AttackThroughBlock(xrayResult.getEntity().getId()));
                }
            }
        }
    }

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void creo$doNotAttackItemEntities(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (target instanceof ItemEntity)
            ci.cancel();
    }
}