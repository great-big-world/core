package dev.creoii.greatbigworld.world.structuretrigger;

import dev.creoii.greatbigworld.util.QuadPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Optional;

public record StructureTrigger(Mutable<StructureStart> structureStart, QuadPredicate<ServerWorld, BlockPos, BlockState, Optional<StructureStart>> structureTrigger) {
    public StructureTrigger(QuadPredicate<ServerWorld, BlockPos, BlockState, Optional<StructureStart>> structureTrigger) {
        this(new MutableObject<>(), structureTrigger);
    }

    public static StructureTrigger copy(StructureTrigger trigger) {
        return new StructureTrigger(trigger.structureStart, trigger.structureTrigger);
    }

    public boolean trigger(ServerWorld world, BlockPos pos, BlockState state) {
        return structureTrigger.test(world, pos, state, Optional.ofNullable(structureStart.getValue()));
    }
}
