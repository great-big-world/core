package dev.creoii.greatbigworld.mixin.client;

import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(stringValue = "saves"))
    private String gbw$changeSavesDirectoryClient(String constant) {
        return constant.concat("/gbw");
    }

    @ModifyConstant(method = "<init>", constant = @Constant(stringValue = "backups"))
    private String gbw$changeBackupsDirectoryClient(String constant) {
        return constant.concat("/gbw");
    }

    @Inject(method = "setLevel", at = @At("HEAD"))
    private void gbw$initToDimension(ClientLevel world, CallbackInfo ci) {
        GreatBigWorldClient.setToDimension(world.dimension().identifier());
        GreatBigWorldClient.setPreviousDimension(world.dimension().identifier());
    }
}