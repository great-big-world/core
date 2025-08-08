package dev.creoii.greatbigworld.world.structuretrigger;

import net.minecraft.util.Identifier;

public interface StructureTriggerGroupContainer {
    void gbw$addStructureTrigger(StructureTrigger trigger);

    void gbw$removeStructureTrigger(StructureTrigger trigger);

    StructureTriggerGroup gbw$getStructureTriggerGroup(Identifier group);
}
