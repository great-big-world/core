package dev.creoii.greatbigworld.mixin.world.structure;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerStart;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.ListPoolElement;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ListPoolElement.class)
public class ListPoolElementMixin {
    @Inject(method = "generate", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/pool/StructurePoolElement;generate(Lnet/minecraft/structure/StructureTemplateManager;Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/BlockRotation;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/structure/StructureLiquidSettings;Z)Z"))
    private void gbw$initListPoolElementStartRef(StructureTemplateManager structureTemplateManager, StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, BlockPos pos, BlockPos pivot, BlockRotation rotation, BlockBox box, Random random, StructureLiquidSettings liquidSettings, boolean keepJigsaws, CallbackInfoReturnable<Boolean> cir, @Local StructurePoolElement structurePoolElement) {
        ((StructureTriggerStart) structurePoolElement).gbw$setStructureStart(((StructureTriggerStart) this).gbw$getStructureStart());
    }
}
