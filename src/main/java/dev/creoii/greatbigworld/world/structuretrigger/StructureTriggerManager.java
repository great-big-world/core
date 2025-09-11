package dev.creoii.greatbigworld.world.structuretrigger;

import com.mojang.serialization.Codec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StructureTriggerManager extends PersistentState {
    public static final Codec<StructureTriggerManager> CODEC = Codec.unboundedMap(Codec.STRING, StructureTriggerGroup.CODEC).xmap(map -> {
        StructureTriggerManager manager = new StructureTriggerManager();
        manager.structureTriggerGroups.putAll(map);
        return manager;
    }, structureTriggerManager -> new HashMap<>(structureTriggerManager.structureTriggerGroups));
    private static final PersistentStateType<StructureTriggerManager> STATE_TYPE = new PersistentStateType<>("gbw_structure_triggers", StructureTriggerManager::new, CODEC, null);
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

    public void tick(ServerWorld world) {
        Iterator<Map.Entry<String, StructureTriggerGroup>> it = structureTriggerGroups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, StructureTriggerGroup> entry = it.next();
            StructureTriggerGroup group = entry.getValue();

            for (StructureTrigger.Built trigger : group.triggers()) {
                if (!world.isChunkLoaded(ChunkSectionPos.getSectionCoord(trigger.pos().getX()), ChunkSectionPos.getSectionCoord(trigger.pos().getZ())))
                    continue;
                if (world.getTime() % trigger.tickRate() == 0) {
                    //System.out.println("trigger @ " + trigger.pos().toShortString());
                    if (trigger.trigger().trigger(world, trigger.pos(), trigger.state(), group)) {
                        //System.out.println("success, remove trigger");
                        it.remove();
                        break;
                    }
                }
            }
        }
    }

    public static StructureTriggerManager getServerState(ServerWorld world) {
        StructureTriggerManager manager = world.getPersistentStateManager().getOrCreate(STATE_TYPE);
        manager.markDirty();
        return manager;
    }
}
