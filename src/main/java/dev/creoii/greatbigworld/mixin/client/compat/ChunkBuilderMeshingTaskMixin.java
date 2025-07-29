package dev.creoii.greatbigworld.mixin.client.compat;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.block.OverlayState;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import net.caffeinemc.mods.sodium.client.util.task.CancellationToken;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkBuilderMeshingTask.class)
public class ChunkBuilderMeshingTaskMixin {
    @Inject(method = "execute(Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lnet/caffeinemc/mods/sodium/client/util/task/CancellationToken;)Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;renderModel(Lnet/minecraft/client/render/model/BlockStateModel;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)V"))
    private void gbw$renderBlockOverlays(ChunkBuildContext buildContext, CancellationToken cancellationToken, CallbackInfoReturnable<ChunkBuildOutput> cir, @Local BlockRenderer blockRenderer, @Local ChunkBuildBuffers buffers, @Local BlockRenderCache cache, @Local(ordinal = 0) BlockPos.Mutable blockPos, @Local(ordinal = 1) BlockPos.Mutable modelOffset, @Local BlockState blockState) {
        if (blockState.getBlock() instanceof OverlayState overlayState) {
            BlockState overlay = overlayState.gbw$getOverlayState(blockState, blockPos, Random.create());
            if (overlay.getBlock() != Blocks.AIR) {
                BlockStateModel model = cache.getBlockModels().getModel(overlay);
                blockRenderer.renderModel(model, overlay, blockPos, modelOffset);
            }
        }
    }
}