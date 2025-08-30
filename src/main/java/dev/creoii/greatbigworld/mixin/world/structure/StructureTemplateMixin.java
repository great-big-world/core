package dev.creoii.greatbigworld.mixin.world.structure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.registry.GBWBlocks;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.world.structuretrigger.*;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin implements StructureTriggerStart {
    @Unique private StructureStart structureStart;
    @Unique private UUID uuid;

    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/structure/StructurePlacementData;getRandomBlockInfos(Ljava/util/List;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/structure/StructureTemplate$PalettedBlockInfoList;"))
    private void gbw$initUUID(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, Random random, int flags, CallbackInfoReturnable<Boolean> cir) {
        uuid = UUID.randomUUID();
    }

    @WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1))
    private boolean gbw$triggerStructureTriggersOnPlace(ServerWorldAccess instance, BlockPos pos, BlockState blockState, int i, Operation<Boolean> original, @Local StructureTemplate.StructureBlockInfo structureBlockInfo) {
        if (structureBlockInfo.state().isOf(GBWBlocks.STRUCTURE_TRIGGER) && structureBlockInfo.nbt() != null) {
            Identifier finalStateId = Identifier.tryParse(structureBlockInfo.nbt().getString("final_state", "minecraft:air"));
            if (finalStateId == null) {
                finalStateId = Identifier.of("air");
            }

            Identifier targetId = structureBlockInfo.nbt().get("target", Identifier.CODEC).orElse(StructureTriggerBlockEntity.DEFAULT_TARGET);
            BlockState finalState = Registries.BLOCK.get(finalStateId).getDefaultState();
            if (GBWRegistries.STRUCTURE_TRIGGERS.containsId(targetId)) {
                StructureTrigger.Built trigger = StructureTrigger.build(GBWRegistries.STRUCTURE_TRIGGERS.get(targetId), pos, finalState, structureBlockInfo.nbt().getInt("tick_rate", 20));
                instance.setBlockState(pos, finalState, i);
                trigger.trigger().structureStart().setValue(structureStart);
                StructureTriggerBlock.TriggerType triggerType = StructureTriggerBlock.TriggerType.valueOf(structureBlockInfo.nbt().getString("trigger_type", "init").toUpperCase());

                ServerWorld serverWorld = instance instanceof ChunkRegion chunkRegion ? chunkRegion.world : (ServerWorld) instance;
                StructureTriggerManager manager = StructureTriggerManager.getServerState(serverWorld);

                StructureTriggerGroup group = manager.getGroup(uuid);
                if (group == null) {
                    group = new StructureTriggerGroup(trigger.trigger().dataType().create());
                    manager.addGroup(uuid, group);
                }

                if (triggerType == StructureTriggerBlock.TriggerType.INIT) {
                    trigger.trigger().trigger(serverWorld, pos, finalState, group);
                } else {
                    group.addTrigger(trigger);
                }
            }
            return true;
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
