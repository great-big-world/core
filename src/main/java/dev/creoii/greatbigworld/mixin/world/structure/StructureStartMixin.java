package dev.creoii.greatbigworld.mixin.world.structure;

import dev.creoii.greatbigworld.world.structuretrigger.*;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructurePiecesList;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(StructureStart.class)
public class StructureStartMixin implements StructureTriggerGroupContainer {
    @Shadow @Final private StructurePiecesList children;
    @Unique private Map<Identifier, StructureTriggerGroup> structureTriggers;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$initStructurePieceStartRef(Structure structure, ChunkPos pos, int references, StructurePiecesList children, CallbackInfo ci) {
        this.children.pieces().forEach(piece -> {
            if (piece instanceof PoolStructurePiece poolStructurePiece) {
                ((StructureTriggerStart) poolStructurePiece).gbw$setStructureStart((StructureStart) (Object) this);
            }
        });
        structureTriggers = new HashMap<>();
    }

    @Override
    public void gbw$addStructureTrigger(StructureTrigger trigger) {
        if (structureTriggers.containsKey(trigger.group())) {
            structureTriggers.get(trigger.group()).gbw$addStructureTrigger(trigger);
        } else {
            structureTriggers.put(trigger.group(), new StructureTriggerGroup(StructureTriggerGroup.DataType.INT));
            structureTriggers.get(trigger.group()).gbw$addStructureTrigger(trigger);
        }
    }

    @Override
    public void gbw$removeStructureTrigger(StructureTrigger trigger) {
        structureTriggers.get(trigger.group()).gbw$removeStructureTrigger(trigger);
        if (structureTriggers.get(trigger.group()).size() == 0)
            structureTriggers.remove(trigger.group());
    }

    @Override
    public StructureTriggerGroup gbw$getStructureTriggerGroup(Identifier group) {
        return structureTriggers.get(group);
    }
}
