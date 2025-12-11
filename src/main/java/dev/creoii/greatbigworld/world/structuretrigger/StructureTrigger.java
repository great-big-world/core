package dev.creoii.greatbigworld.world.structuretrigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.util.Predicate5;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructureStart;

public record StructureTrigger(Identifier id, StructureTriggerDataType<?> dataType, Mutable<StructureStart> structureStart, Predicate5<ServerLevel, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> trigger) {
    public StructureTrigger(Identifier id, StructureTriggerDataType<?> dataType, Predicate5<ServerLevel, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> trigger) {
        this(id, dataType, new MutableObject<>(), trigger);
    }

    public StructureTrigger(Identifier id, Predicate5<ServerLevel, BlockPos, BlockState, Optional<StructureStart>, StructureTriggerGroup> trigger) {
        this(id, StructureTriggerDataType.EMPTY, new MutableObject<>(), trigger);
    }

    public static Built build(StructureTrigger trigger, BlockPos pos, BlockState state, int tickRate) {
        return new Built(trigger, pos, state, tickRate);
    }

    public boolean trigger(ServerLevel world, BlockPos pos, BlockState state, StructureTriggerGroup group) {
        return trigger.test(world, pos, state, Optional.ofNullable(structureStart.getValue()), group);
    }

    public record Built(StructureTrigger trigger, BlockPos pos, BlockState state, int tickRate) {
        public static final Codec<Built> CODEC = RecordCodecBuilder.create(instance -> {
            return instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(built -> built.trigger().id),
                    BlockPos.CODEC.fieldOf("pos").forGetter(Built::pos),
                    BlockState.CODEC.fieldOf("state").forGetter(Built::state),
                    Codec.INT.fieldOf("tick_rate").forGetter(Built::tickRate)
            ).apply(instance, (id, pos, state, tickRate) -> new Built(GBWRegistries.STRUCTURE_TRIGGERS.getValue(id), pos, state, tickRate));
        });
    }
}
