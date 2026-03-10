package dev.creoii.greatbigworld.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getLevelData()Lnet/minecraft/world/level/storage/LevelData;"))
    private void gbw$updatePrevDimensionManager(TeleportTransition teleportTransition, CallbackInfoReturnable<ServerPlayer> cir, @Local(ordinal = 0) ServerLevel serverLevel, @Local ResourceKey<Level> resourceKey) {
        PreviousDimensionManager manager = PreviousDimensionManager.getServerState(serverLevel.getServer());
        manager.setPrevDimension(getUUID(), resourceKey);
        manager.setToDimension(getUUID(), serverLevel.dimension());
    }
}
