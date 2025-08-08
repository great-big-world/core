package dev.creoii.greatbigworld.world.structuretrigger;

import dev.creoii.greatbigworld.util.Predicate5;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record StructureTrigger(@Nullable Identifier group, Mutable<StructureStart> structureStart, Predicate5<ServerWorld, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> structureTrigger) {
    public StructureTrigger(@Nullable Identifier group, Predicate5<ServerWorld, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> structureTrigger) {
        this(group, new MutableObject<>(), structureTrigger);
    }

    public StructureTrigger(Predicate5<ServerWorld, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> structureTrigger) {
        this(null, new MutableObject<>(), structureTrigger);
    }

    public static StructureTrigger copy(StructureTrigger trigger) {
        return new StructureTrigger(trigger.group, trigger.structureStart, trigger.structureTrigger);
    }

    public boolean trigger(ServerWorld world, BlockPos pos, BlockState state, StructureTriggerGroup group) {
        return structureTrigger.test(world, pos, state, Optional.ofNullable(structureStart.getValue()), group);
    }
}
