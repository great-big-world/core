package dev.creoii.greatbigworld.mixin.client;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

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
}