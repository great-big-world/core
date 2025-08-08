package dev.creoii.greatbigworld.mixin.world.structure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.registry.GBWBlocks;
import dev.creoii.greatbigworld.world.structuretrigger.*;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin implements StructureTriggerStart {
    @Unique private StructureStart structureStart;

    @WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1))
    private boolean gbw$triggerStructureTriggersOnPlace(ServerWorldAccess instance, BlockPos pos, BlockState blockState, int i, Operation<Boolean> original, @Local StructureTemplate.StructureBlockInfo structureBlockInfo) {
        if (structureBlockInfo.state().isOf(GBWBlocks.STRUCTURE_TRIGGER) && structureBlockInfo.nbt() != null) {
            Identifier finalStateId = Identifier.tryParse(structureBlockInfo.nbt().getString("final_state", "minecraft:air"));
            if (finalStateId != null) {
                Identifier targetId = structureBlockInfo.nbt().get("target", Identifier.CODEC).orElse(StructureTriggerBlockEntity.DEFAULT_TARGET);
                BlockState finalState = Registries.BLOCK.get(finalStateId).getDefaultState();
                if (GreatBigWorld.STRUCTURE_TRIGGERS.containsId(targetId)) {
                    StructureTrigger trigger = StructureTrigger.copy(GreatBigWorld.STRUCTURE_TRIGGERS.get(targetId));
                    instance.setBlockState(pos, finalState, i);
                    trigger.structureStart().setValue(structureStart);
                    StructureTriggerBlock.TriggerType triggerType = StructureTriggerBlock.TriggerType.valueOf(structureBlockInfo.nbt().getString("trigger_type", "init").toUpperCase());
                    if (triggerType == StructureTriggerBlock.TriggerType.INIT) {
                        StructureTriggerGroup group = ((StructureTriggerGroupContainer) (Object) structureStart).gbw$getStructureTriggerGroup(trigger.group());
                        trigger.trigger((ServerWorld) instance, pos, finalState, group);
                    } else {
                        int tickRate = structureBlockInfo.nbt().getInt("tick_rate", 20);
                        StructureTriggerManager manager = StructureTriggerManager.getServerState((ServerWorld) instance);
                        manager.addTrigger(pos, new StructureTriggerManager.StructureTriggerInfo(instance.getRegistryManager().getOptional(RegistryKeys.STRUCTURE).get().getEntry(structureStart.getStructure()).getKey().get(), trigger, finalState, tickRate));
                    }
                }
                return true;
            }
        }
        return original.call(instance, pos, blockState, i);
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
