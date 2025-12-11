package dev.creoii.greatbigworld.mixin.client;

import dev.creoii.greatbigworld.client.ScreenShakeManager;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Camera mainCamera;

    @Inject(method = "extractCamera", at = @At("TAIL"))
    private void gbw$applyScreenShake(float f, CallbackInfo ci) {
        ScreenShakeManager.tick();

        float dx = ScreenShakeManager.getXOffset();
        float dy = ScreenShakeManager.getYOffset();

        Vec3 pos = mainCamera.position();
        mainCamera.setPosition(new Vec3(pos.x + dx, pos.y + dy, pos.z));
        mainCamera.setRotation(mainCamera.yRot() + dx * 2f, mainCamera.xRot() + dy * 2f);
    }
}
