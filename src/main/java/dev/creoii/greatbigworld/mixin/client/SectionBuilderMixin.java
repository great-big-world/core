package dev.creoii.greatbigworld.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.creoii.greatbigworld.block.OverlayState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(SectionCompiler.class)
public class SectionBuilderMixin {
    @Shadow @Final private BlockRenderDispatcher blockRenderer;

    @Inject(method = "compile", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;renderBatched(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/BlockAndTintGetter;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLjava/util/List;)V"))
    private void gbw$renderBlockOverlays(SectionPos sectionPos, RenderSectionRegion renderRegion, VertexSorting vertexSorter, SectionBufferBuilderPack allocatorStorage, CallbackInfoReturnable<SectionCompiler.Results> cir, @Local PoseStack matrixStack, @Local List<BlockModelPart> list, @Local RandomSource random, @Local(ordinal = 2) BlockPos blockPos3, @Local BufferBuilder bufferBuilder, @Local BlockState blockState) {
        if (blockState.getBlock() instanceof OverlayState overlayState) {
            BlockState overlay = overlayState.gbw$getOverlayState(blockState, blockPos3, random);
            if (overlay.getBlock() != Blocks.AIR) {
                blockRenderer.getBlockModel(blockState).collectParts(random, list);
                list.clear();
                blockRenderer.renderBatched(overlay, blockPos3, renderRegion, matrixStack, bufferBuilder, true, list);
            }
        }
    }
}
