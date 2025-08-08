package dev.creoii.greatbigworld.mixin.world.structure;

import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerStart;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoolStructurePiece.class)
public class PoolStructurePieceMixin implements StructureTriggerStart {
    @Shadow @Final protected StructurePoolElement poolElement;
    @Unique private StructureStart structureStart;

    @Inject(method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At("HEAD"))
    private void gbw$initStructureTemplateStartRef(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, BlockPos pivot, boolean keepJigsaws, CallbackInfo ci) {
        ((StructureTriggerStart) this.poolElement).gbw$setStructureStart(gbw$getStructureStart());
    }

    @Override
    public StructureStart gbw$getStructureStart() {
        return structureStart;
    }

    @Override
    public void gbw$setStructureStart(StructureStart structureStart) {
        this.structureStart = structureStart;
    }
}
