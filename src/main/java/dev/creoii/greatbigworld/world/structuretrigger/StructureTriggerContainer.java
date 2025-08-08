package dev.creoii.greatbigworld.world.structuretrigger;

import net.minecraft.util.Identifier;

import java.util.List;

public interface StructureTriggerContainer {
    void gbw$addStructureTrigger(StructureTrigger trigger);

    void gbw$removeStructureTrigger(StructureTrigger trigger);

    List<StructureTrigger> gbw$getStructureTriggers(Identifier group);
}
