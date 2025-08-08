package dev.creoii.greatbigworld.world.structuretrigger;

import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class StructureTriggerGroup implements StructureTriggerContainer {
    private final List<StructureTrigger> triggers;
    private final DataType dataType;
    private final Mutable<?> data;

    public StructureTriggerGroup(DataType dataType) {
        triggers = new ArrayList<>();
        this.dataType = dataType;
        data = dataType.getData();
    }

    @Override
    public void gbw$addStructureTrigger(StructureTrigger trigger) {
        triggers.add(trigger);
    }

    @Override
    public void gbw$removeStructureTrigger(StructureTrigger trigger) {
        triggers.remove(trigger);
    }

    @Override
    public List<StructureTrigger> gbw$getStructureTriggers(Identifier group) {
        return triggers;
    }

    public DataType getDataType() {
        return dataType;
    }

    public Mutable<?> getData() {
        return data;
    }

    public int size() {
        return triggers.size();
    }

    public enum DataType {
        INT(MutableInt::new);

        private final Supplier<Mutable<?>> data;

        DataType(Supplier<Mutable<?>> data) {
            this.data = data;
        }

        public Mutable<?> getData() {
            return data.get();
        }

        public void setData(Object o) {
            if (data.get() instanceof MutableInt mutableInt && o instanceof Integer i) {
                mutableInt.setValue(i);
            }
        }
    }
}
