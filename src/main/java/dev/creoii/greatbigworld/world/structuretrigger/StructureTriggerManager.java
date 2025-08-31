package dev.creoii.greatbigworld.world.structuretrigger;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.util.Codecs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StructureTriggerManager extends PersistentState {
    public static final Codec<StructureTriggerManager> CODEC = Codec.unboundedMap(Codecs.UUID, StructureTriggerGroup.CODEC).xmap(map -> {
        StructureTriggerManager manager = new StructureTriggerManager();
        manager.structureTriggerGroups.putAll(map);
        return manager;
    }, structureTriggerManager -> new HashMap<>(structureTriggerManager.structureTriggerGroups));
    private static final PersistentStateType<StructureTriggerManager> STATE_TYPE = new PersistentStateType<>("gbw_structure_triggers", StructureTriggerManager::new, CODEC, null);
    private final HashMap<UUID, StructureTriggerGroup> structureTriggerGroups;

    public StructureTriggerManager() {
        structureTriggerGroups = new HashMap<>();
    }

    @Nullable
    public StructureTriggerGroup getGroup(UUID uuid) {
        return structureTriggerGroups.get(uuid);
    }

    public void addGroup(UUID uuid, StructureTriggerGroup group) {
        structureTriggerGroups.put(uuid, group);
    }

    public void tick(ServerWorld world) {
        List<Map.Entry<UUID, StructureTriggerGroup>> entries = new ArrayList<>(structureTriggerGroups.entrySet());
        for (int i = entries.size() - 1; i >= 0; --i) {
            Map.Entry<UUID, StructureTriggerGroup> entry = entries.get(i);
            UUID uuid = entry.getKey();
            StructureTriggerGroup group = entry.getValue();

            for (StructureTrigger.Built trigger : group.triggers()) {
                if (world.getTime() % trigger.tickRate() == 0) {
                    if (!trigger.trigger().trigger(world, trigger.pos(), trigger.state(), group)) {
                        structureTriggerGroups.remove(uuid);
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
