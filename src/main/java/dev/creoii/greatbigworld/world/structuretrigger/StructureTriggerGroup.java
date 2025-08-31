package dev.creoii.greatbigworld.world.structuretrigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerData;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;

import java.util.List;

public record StructureTriggerGroup(List<StructureTrigger.Built> triggers, StructureTriggerData data) {
    public static final Codec<StructureTriggerGroup> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                StructureTrigger.Built.CODEC.listOf().fieldOf("triggers").forGetter(group -> group.triggers),
                StructureTriggerDataType.CODEC.fieldOf("data").forGetter(group -> group.data)
        ).apply(instance, StructureTriggerGroup::new);
    });

    public void addTrigger(StructureTrigger.Built trigger) {
        triggers.add(trigger);
    }

    public int size() {
        return triggers.size();
    }
}
