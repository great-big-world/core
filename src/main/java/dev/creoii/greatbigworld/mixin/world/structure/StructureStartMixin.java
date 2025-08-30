package dev.creoii.greatbigworld.mixin.world.structure;

import dev.creoii.greatbigworld.world.structuretrigger.*;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiecesList;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureStart.class)
public class StructureStartMixin {
    @Shadow @Final private StructurePiecesList children;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$initStructurePieceStartRef(Structure structure, ChunkPos pos, int references, StructurePiecesList children, CallbackInfo ci) {
        this.children.pieces().forEach(piece -> {
            if (piece instanceof PoolStructurePiece poolStructurePiece) {
                ((StructureTriggerStart) poolStructurePiece).gbw$setStructureStart((StructureStart) (Object) this);
            }
        });
    }
}
