package dev.creoii.greatbigworld.world.structuretrigger.data;

import com.mojang.serialization.MapCodec;

public record EmptyTriggerData() implements StructureTriggerData {
    private static final EmptyTriggerData INSTANCE = new EmptyTriggerData();
    public static final MapCodec<EmptyTriggerData> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public StructureTriggerDataType<?> getType() {
        return StructureTriggerDataType.EMPTY;
    }
}
