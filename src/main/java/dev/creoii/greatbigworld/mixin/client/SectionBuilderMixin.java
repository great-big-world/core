package dev.creoii.greatbigworld.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.creoii.greatbigworld.block.OverlayState;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.RenderShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

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
public abstract class SectionBuilderMixin {
    @Shadow @Final private BlockRenderDispatcher blockRenderer;

    @Shadow
    protected abstract BufferBuilder getOrBeginLayer(Map<ChunkSectionLayer, BufferBuilder> map, SectionBufferBuilderPack sectionBufferBuilderPack, ChunkSectionLayer chunkSectionLayer);

    @Inject(method = "compile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isSolidRender()Z"))
    private void gbw$renderBlockOverlays(SectionPos sectionPos, RenderSectionRegion renderRegion, VertexSorting vertexSorter, SectionBufferBuilderPack sectionBufferBuilderPack, CallbackInfoReturnable<SectionCompiler.Results> cir, @Local PoseStack poseStack, @Local List<BlockModelPart> list, @Local RandomSource random, @Local(ordinal = 2) BlockPos blockPos3, @Local BlockState blockState, @Local Map<ChunkSectionLayer, BufferBuilder> map) {
        if (blockState.getBlock() instanceof OverlayState overlayState && blockState.getRenderShape() == RenderShape.MODEL) {
            BlockState overlay = overlayState.gbw$getOverlayState(blockState, blockPos3, random);
            if (overlay.getBlock() != Blocks.AIR) {
                ChunkSectionLayer chunkSectionLayer = ItemBlockRenderTypes.getChunkRenderType(overlay);
                BufferBuilder bufferBuilder = getOrBeginLayer(map, sectionBufferBuilderPack, chunkSectionLayer);
                random.setSeed(overlay.getSeed(blockPos3));

                blockRenderer.getBlockModel(overlay).collectParts(random, list);

                poseStack.pushPose();
                poseStack.translate((float)SectionPos.sectionRelative(blockPos3.getX()), (float)SectionPos.sectionRelative(blockPos3.getY()), (float)SectionPos.sectionRelative(blockPos3.getZ()));
                blockRenderer.renderBatched(overlay, blockPos3, renderRegion, poseStack, bufferBuilder, true, list);
                poseStack.popPose();

                list.clear();
            }
        }
    }
}
