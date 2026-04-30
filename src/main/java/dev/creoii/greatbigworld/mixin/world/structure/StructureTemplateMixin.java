package dev.creoii.greatbigworld.mixin.world.structure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.block.PlaceableByStructure;
import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.registry.GBWBlocks;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.world.structuretrigger.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin implements StructureTriggerStart {
    @Unique private StructureStart structureStart;
    @Unique private UUID uuid;

    @Inject(method = "placeInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;getRandomPalette(Ljava/util/List;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$Palette;"))
    private void gbw$initUUID(ServerLevelAccessor world, BlockPos pos, BlockPos pivot, StructurePlaceSettings placementData, RandomSource random, int flags, CallbackInfoReturnable<Boolean> cir) {
        long most = world.getLevel().getSeed() ^ (((long) pos.getX()) << 32 | (pos.getY() & 0xffffffffL));
        long least = ((long) pos.getZ() << 32) ^ world.getLevel().getSeed();
        uuid = new UUID(most, least);
    }

    @WrapOperation(method = "placeInWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerLevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 1))
    private boolean gbw$triggerStructureTriggersOnPlace(ServerLevelAccessor instance, BlockPos pos, BlockState blockState, int i, Operation<Boolean> original, @Local StructureTemplate.StructureBlockInfo structureBlockInfo) {
        if (blockState.getBlock() instanceof PlaceableByStructure placeableByStructure) {
            placeableByStructure.onPlaceByStructure(instance, blockState, pos);
        }

        if (structureBlockInfo.state().is(GBWBlocks.STRUCTURE_TRIGGER) && structureBlockInfo.nbt() != null) {
            Identifier finalStateId = Identifier.tryParse(structureBlockInfo.nbt().getStringOr("final_state", "minecraft:air"));
            if (finalStateId == null) {
                finalStateId = Identifier.parse("air");
            }

            Identifier targetId = structureBlockInfo.nbt().read(StructureTriggerBlockEntity.TARGET_KEY, Identifier.CODEC).orElse(StructureTriggerBlockEntity.DEFAULT_TARGET);
            BlockState finalState = BuiltInRegistries.BLOCK.getValue(finalStateId).defaultBlockState();
            if (GBWRegistries.STRUCTURE_TRIGGERS.containsKey(targetId)) {
                StructureTrigger.Built trigger = StructureTrigger.build(GBWRegistries.STRUCTURE_TRIGGERS.getValue(targetId), pos, finalState, structureBlockInfo.nbt().getIntOr("tick_rate", 20));
                instance.setBlock(pos, finalState, i);
                trigger.trigger().structureStart().setValue(structureStart);
                StructureTriggerBlock.TriggerType triggerType = StructureTriggerBlock.TriggerType.valueOf(structureBlockInfo.nbt().getStringOr("trigger_type", "init").toUpperCase());

                ServerLevel serverWorld = instance instanceof WorldGenRegion chunkRegion ? chunkRegion.getLevel() : (ServerLevel) instance;
                StructureTriggerManager manager = StructureTriggerManager.getServerState(serverWorld);

                StructureTriggerGroup group = manager.getGroup(uuid);
                if (group == null) {
                    group = new StructureTriggerGroup(new ArrayList<>(), trigger.trigger().dataType().create());
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
