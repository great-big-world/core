package dev.creoii.greatbigworld.mixin.world.structure;

import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerStart;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StructurePoolElement.class)
public class StructurePoolElementMixin implements StructureTriggerStart {
    @Unique
    private StructureStart structureStart;

    @Override
    public StructureStart gbw$getStructureStart() {
        return structureStart;
    }

    @Override
    public void gbw$setStructureStart(StructureStart structureStart) {
        this.structureStart = structureStart;
    }
}
