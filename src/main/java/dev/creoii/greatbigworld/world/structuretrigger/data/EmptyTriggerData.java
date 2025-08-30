package dev.creoii.greatbigworld.world.structuretrigger.data;

import com.mojang.serialization.MapCodec;

public class EmptyTriggerData extends StructureTriggerData<Object> {
    private static final EmptyTriggerData INSTANCE = new EmptyTriggerData();
    protected static final MapCodec<EmptyTriggerData> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Object getData() {
        return null;
    }

    @Override
    public StructureTriggerDataType<?, ?> getType() {
        return StructureTriggerDataType.EMPTY;
    }
}
