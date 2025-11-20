package dev.creoii.greatbigworld.mixin.client;

import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(stringValue = "saves"))
    private String gbw$changeSavesDirectoryClient(String constant) {
        return constant.concat("/gbw");
    }

    @ModifyConstant(method = "<init>", constant = @Constant(stringValue = "backups"))
    private String gbw$changeBackupsDirectoryClient(String constant) {
        return constant.concat("/gbw");
    }

    @Inject(method = "joinWorld", at = @At("HEAD"))
    private void gbw$initToDimension(ClientWorld world, CallbackInfo ci) {
        GreatBigWorldClient.setToDimension(world.getRegistryKey().getValue());
        GreatBigWorldClient.setPreviousDimension(world.getRegistryKey().getValue());
    }
}