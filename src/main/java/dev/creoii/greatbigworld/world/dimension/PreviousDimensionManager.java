package dev.creoii.greatbigworld.world.dimension;

import com.mojang.serialization.Codec;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PreviousDimensionManager extends PersistentState {
    public static final Codec<PreviousDimensionManager> CODEC = Codec.unboundedMap(Codec.STRING, RegistryKey.createCodec(RegistryKeys.WORLD)).xmap(map -> {
        PreviousDimensionManager manager = new PreviousDimensionManager();
        manager.prevDimensions.putAll(map);
        return manager;
    }, structureTriggerManager -> new HashMap<>(structureTriggerManager.prevDimensions));
    private static final PersistentStateType<PreviousDimensionManager> STATE_TYPE = new PersistentStateType<>("gbw_previous_dimensions", PreviousDimensionManager::new, CODEC, null);
    private final HashMap<String, RegistryKey<World>> prevDimensions;

    public PreviousDimensionManager() {
        prevDimensions = new HashMap<>();
    }

    @Nullable
    public RegistryKey<World> getPrevDimension(UUID uuid) {
        return prevDimensions.get(uuid.toString());
    }

    public void setPrevDimension(UUID uuid, RegistryKey<World> group) {
        prevDimensions.put(uuid.toString(), group);
    }

    public void remove(UUID uuid) {
        prevDimensions.remove(uuid.toString());
    }

    public static PreviousDimensionManager getServerState(MinecraftServer server) {
        PreviousDimensionManager manager = server.getWorld(ServerWorld.OVERWORLD).getPersistentStateManager().getOrCreate(STATE_TYPE);
        manager.markDirty();
        return manager;
    }
}
