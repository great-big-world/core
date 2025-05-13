package dev.creoii.greatbigworld.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.VertexSorter;
import dev.creoii.greatbigworld.block.OverlayState;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferAllocatorStorage;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.render.chunk.SectionBuilder;
import net.minecraft.client.render.model.BlockModelPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SectionBuilder.class)
public class SectionBuilderMixin {
    @Shadow @Final private BlockRenderManager blockRenderManager;

    @Inject(method = "build", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLjava/util/List;)V"))
    private void gbw$renderBlockOverlays(ChunkSectionPos sectionPos, ChunkRendererRegion renderRegion, VertexSorter vertexSorter, BlockBufferAllocatorStorage allocatorStorage, CallbackInfoReturnable<SectionBuilder.RenderData> cir, @Local MatrixStack matrixStack, @Local List<BlockModelPart> list, @Local Random random, @Local(ordinal = 2) BlockPos blockPos3, @Local BufferBuilder bufferBuilder, @Local BlockState blockState) {
        if (blockState.getBlock() instanceof OverlayState overlayState) {
            BlockState overlay = overlayState.getOverlayState(blockState, blockPos3, random);
            if (overlay.getBlock() != Blocks.AIR) {
                blockRenderManager.getModel(blockState).addParts(random, list);
                list.clear();
                blockRenderManager.renderBlock(overlay, blockPos3, renderRegion, matrixStack, bufferBuilder, true, list);
            }
        }
    }
}
