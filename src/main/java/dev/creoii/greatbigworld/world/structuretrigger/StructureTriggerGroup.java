package dev.creoii.greatbigworld.world.structuretrigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerData;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;

import java.util.ArrayList;
import java.util.List;

public class StructureTriggerGroup {
    public static final Codec<StructureTriggerGroup> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                StructureTrigger.Built.CODEC.listOf().fieldOf("triggers").forGetter(group -> group.triggers),
                StructureTriggerDataType.CODEC.fieldOf("data").forGetter(group -> group.data)
        ).apply(instance, (triggers, data) -> {
                StructureTriggerGroup group = new StructureTriggerGroup(data);
                group.triggers.addAll(triggers);
                return group;
        });
    });
    private final List<StructureTrigger.Built> triggers;
    private final StructureTriggerData<?> data;

    public StructureTriggerGroup(StructureTriggerData<?> data) {
        triggers = new ArrayList<>();
        this.data = data;
    }

    public void addTrigger(StructureTrigger.Built trigger) {
        triggers.add(trigger);
    }

    public List<StructureTrigger.Built> getTriggers() {
        return triggers;
    }

    public StructureTriggerData<?> getData() {
        return data;
    }

    public int size() {
        return triggers.size();
    }
}
