package dev.creoii.greatbigworld.mixin.world.structure;

import dev.creoii.greatbigworld.world.structuretrigger.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureStart.class)
public class StructureStartMixin {
    @Shadow @Final private PiecesContainer pieceContainer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$initStructurePieceStartRef(Structure structure, ChunkPos pos, int references, PiecesContainer children, CallbackInfo ci) {
        this.pieceContainer.pieces().forEach(piece -> {
            if (piece instanceof PoolElementStructurePiece poolStructurePiece) {
                ((StructureTriggerStart) poolStructurePiece).gbw$setStructureStart((StructureStart) (Object) this);
            }
        });
    }
}
