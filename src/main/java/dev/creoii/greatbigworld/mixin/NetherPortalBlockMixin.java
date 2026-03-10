package dev.creoii.greatbigworld.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {
    @WrapOperation(method = "getPortalDestination", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getLevel(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/server/level/ServerLevel;"))
    private ServerLevel gbw$fixNetherPortalTargetDimension(MinecraftServer instance, ResourceKey<Level> key, Operation<ServerLevel> original, @Local(argsOnly = true) Entity entity) {
        if (key == Level.OVERWORLD) {
            PreviousDimensionManager manager = PreviousDimensionManager.getServerState(instance);
            return original.call(instance, manager.getPrevDimension(entity.getUUID()));
        } else return original.call(instance, key);
    }
}
