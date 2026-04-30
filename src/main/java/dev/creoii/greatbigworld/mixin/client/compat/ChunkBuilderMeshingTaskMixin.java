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
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkBuilderMeshingTask.class)
public class ChunkBuilderMeshingTaskMixin {
    @Inject(method = "execute(Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lnet/caffeinemc/mods/sodium/client/util/task/CancellationToken;)Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;renderModel(Lnet/minecraft/client/renderer/block/model/BlockStateModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;)V"))
    private void gbw$renderBlockOverlays(ChunkBuildContext buildContext, CancellationToken cancellationToken, CallbackInfoReturnable<ChunkBuildOutput> cir, @Local(name = "blockRenderer") BlockRenderer blockRenderer, @Local(name = "buffers") ChunkBuildBuffers buffers, @Local(name = "cache") BlockRenderCache cache, @Local(name = "blockPos") BlockPos.MutableBlockPos blockPos, @Local(name = "modelOffset") BlockPos.MutableBlockPos modelOffset, @Local(name = "blockState") BlockState blockState) {
        if (blockState.getBlock() instanceof OverlayState overlayState) {
            BlockState overlay = overlayState.gbw$getOverlayState(blockState, blockPos, RandomSource.create());
            if (overlay.getBlock() != Blocks.AIR) {
                BlockStateModel model = cache.getBlockModels().getBlockModel(overlay);
                blockRenderer.renderModel(model, overlay, blockPos, modelOffset);
            }
        }
    }
}