package dev.creoii.greatbigworld.mixin.world.structure;

import dev.creoii.greatbigworld.util.StructureTriggerStart;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.SinglePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePoolElement.class)
public class SinglePoolElementMixin {
    @Inject(method = "getStructure", at = @At("RETURN"))
    private void gbw$initSinglePoolElementStartRef(StructureTemplateManager structureTemplateManager, CallbackInfoReturnable<StructureTemplate> cir) {
        ((StructureTriggerStart) cir.getReturnValue()).gbw$setStructureStart(((StructureTriggerStart) this).gbw$getStructureStart());
    }
}
