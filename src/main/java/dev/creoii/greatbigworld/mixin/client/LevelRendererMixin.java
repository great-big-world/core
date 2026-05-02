package dev.creoii.greatbigworld.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.block.OverlayState;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.BlockBreakingRenderState;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    private @Nullable ClientLevel level;

    @Inject(method = "extractBlockDestroyAnimation", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void gbw$addSnowBlockBreakAnimation(Camera camera, LevelRenderState levelRenderState, CallbackInfo ci, @Local BlockPos blockPos, @Local int i) {
        BlockState state = level.getBlockState(blockPos);
        if (state.getBlock() instanceof OverlayState overlayState) {
            BlockBreakingRenderState renderState = new BlockBreakingRenderState(level, blockPos, i);
            ((MovingBlockRenderStateAccessor) renderState).setBlockState(overlayState.gbw$getOverlayState(state, blockPos, level.getRandom()));
            levelRenderState.blockBreakingRenderStates.add(renderState);
        }
    }
}
