package dev.creoii.greatbigworld.world.structuretrigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.util.Predicate5;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Optional;

public record StructureTrigger(Identifier id, StructureTriggerDataType<?, ?> dataType, Mutable<StructureStart> structureStart, Predicate5<ServerWorld, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> structureTrigger) {
    public StructureTrigger(Identifier id, StructureTriggerDataType<?, ?> dataType, Predicate5<ServerWorld, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> structureTrigger) {
        this(id, dataType, new MutableObject<>(), structureTrigger);
    }

    public StructureTrigger(Identifier id, Predicate5<ServerWorld, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> structureTrigger) {
        this(id, StructureTriggerDataType.EMPTY, new MutableObject<>(), structureTrigger);
    }

    public static Built build(StructureTrigger trigger, BlockPos pos, BlockState state, int tickRate) {
        return new Built(trigger, pos, state, tickRate);
    }

    public boolean trigger(ServerWorld world, BlockPos pos, BlockState state, StructureTriggerGroup group) {
        return structureTrigger.test(world, pos, state, Optional.ofNullable(structureStart.getValue()), group);
    }

    public record Built(StructureTrigger trigger, BlockPos pos, BlockState state, int tickRate) {
        public static final Codec<Built> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(built -> built.trigger().id),
                    BlockPos.CODEC.fieldOf("pos").forGetter(Built::pos),
                    BlockState.CODEC.fieldOf("state").forGetter(Built::state),
                    Codec.INT.fieldOf("tick_rate").forGetter(Built::tickRate)
            ).apply(instance, (id, pos, state, tickRate) -> {
                return new Built(GBWRegistries.STRUCTURE_TRIGGERS.get(id), pos, state, tickRate);
            });
        });
    }
}
