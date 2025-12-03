package dev.creoii.greatbigworld.mixin.client;

import dev.creoii.greatbigworld.client.ScreenShakeManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Camera camera;

    @Inject(method = "updateCameraState", at = @At("TAIL"))
    private void gbw$applyScreenShake(float f, CallbackInfo ci) {
        ScreenShakeManager.tick();

        float dx = ScreenShakeManager.getXOffset();
        float dy = ScreenShakeManager.getYOffset();

        Vec3d pos = camera.getPos();
        camera.setPos(new Vec3d(pos.x + dx, pos.y + dy, pos.z));
        camera.setRotation(camera.getYaw() + dx * 2f, camera.getPitch() + dy * 2f);
    }
}
