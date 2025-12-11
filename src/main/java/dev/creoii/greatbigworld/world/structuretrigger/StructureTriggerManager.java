package dev.creoii.greatbigworld.world.structuretrigger;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class StructureTriggerManager extends SavedData {
    public static final Codec<StructureTriggerManager> CODEC = Codec.unboundedMap(Codec.STRING, StructureTriggerGroup.CODEC).xmap(map -> {
        StructureTriggerManager manager = new StructureTriggerManager();
        manager.structureTriggerGroups.putAll(map);
        return manager;
    }, structureTriggerManager -> new HashMap<>(structureTriggerManager.structureTriggerGroups));
    private static final SavedDataType<StructureTriggerManager> STATE_TYPE = new SavedDataType<>("gbw_structure_triggers", StructureTriggerManager::new, CODEC, null);
    private final HashMap<String, StructureTriggerGroup> structureTriggerGroups;

    public StructureTriggerManager() {
        structureTriggerGroups = new HashMap<>();
    }

    @Nullable
    public StructureTriggerGroup getGroup(UUID uuid) {
        return structureTriggerGroups.get(uuid.toString());
    }

    public void addGroup(UUID uuid, StructureTriggerGroup group) {
        structureTriggerGroups.put(uuid.toString(), group);
    }

    public void tick(ServerLevel world) {
        Iterator<Map.Entry<String, StructureTriggerGroup>> it = structureTriggerGroups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, StructureTriggerGroup> entry = it.next();
            StructureTriggerGroup group = entry.getValue();

            for (StructureTrigger.Built built : group.triggers()) {
                if (!world.hasChunk(SectionPos.blockToSectionCoord(built.pos().getX()), SectionPos.blockToSectionCoord(built.pos().getZ())))
                    continue;
                if (world.getGameTime() % built.tickRate() == 0) {
                    if (built.trigger().trigger(world, built.pos(), built.state(), group)) {
                        it.remove();
                        break;
                    }
                }
            }
        }
    }

    public static StructureTriggerManager getServerState(ServerLevel world) {
        StructureTriggerManager manager = world.getDataStorage().computeIfAbsent(STATE_TYPE);
        manager.setDirty();
        return manager;
    }
}
