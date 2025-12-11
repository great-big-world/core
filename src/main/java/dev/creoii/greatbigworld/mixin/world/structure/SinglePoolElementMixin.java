package dev.creoii.greatbigworld.mixin.world.structure;

import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerStart;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePoolElement.class)
public class SinglePoolElementMixin {
    @Inject(method = "getTemplate", at = @At("RETURN"))
    private void gbw$initSinglePoolElementStartRef(StructureTemplateManager structureTemplateManager, CallbackInfoReturnable<StructureTemplate> cir) {
        ((StructureTriggerStart) cir.getReturnValue()).gbw$setStructureStart(((StructureTriggerStart) this).gbw$getStructureStart());
    }
}
