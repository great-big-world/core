package dev.creoii.greatbigworld.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {
    @WrapOperation(method = "createTeleportTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getWorld(Lnet/minecraft/registry/RegistryKey;)Lnet/minecraft/server/world/ServerWorld;"))
    private ServerWorld gbw$fixNetherPortalTargetDimension(MinecraftServer instance, RegistryKey<World> key, Operation<ServerWorld> original, @Local(argsOnly = true) Entity entity) {
        if (key == World.OVERWORLD) {
            PreviousDimensionManager manager = PreviousDimensionManager.getServerState(instance);
            return original.call(instance, manager.getPrevDimension(entity.getUuid()));
        }
        return original.call(instance, key);
    }
}
