package dev.creoii.greatbigworld.world.structuretrigger.data;

public abstract class StructureTriggerData<T> {
    public abstract T getData();

    public abstract StructureTriggerDataType<?, ?> getType();
}
