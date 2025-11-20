package dev.creoii.greatbigworld.world.dimension;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
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
    private MinecraftServer server;
    private final HashMap<String, RegistryKey<World>> prevDimensions;
    private final HashMap<String, RegistryKey<World>> toDimensions;

    public PreviousDimensionManager() {
        prevDimensions = new HashMap<>();
        toDimensions = new HashMap<>();
    }

    public void init(MinecraftServer server) {
        this.server = server;
    }

    @Nullable
    public RegistryKey<World> getPrevDimension(UUID uuid) {
        return prevDimensions.get(uuid.toString());
    }

    @Nullable
    public RegistryKey<World> getToDimension(UUID uuid) {
        return toDimensions.get(uuid.toString());
    }

    public void setPrevDimension(UUID uuid, RegistryKey<World> group) {
        prevDimensions.put(uuid.toString(), group);
        ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(uuid);
        if (serverPlayer != null)
            ServerPlayNetworking.send(serverPlayer, new PreviousDimensionS2C(group.getValue(), true));
    }

    public void setToDimension(UUID uuid, RegistryKey<World> group) {
        toDimensions.put(uuid.toString(), group);
        ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(uuid);
        if (serverPlayer != null)
            ServerPlayNetworking.send(serverPlayer, new PreviousDimensionS2C(group.getValue(), false));
    }

    public void removePrev(UUID uuid) {
        prevDimensions.remove(uuid.toString());
    }

    public void removeTo(UUID uuid) {
        toDimensions.remove(uuid.toString());
    }

    public static PreviousDimensionManager getServerState(MinecraftServer server) {
        PreviousDimensionManager manager = server.getWorld(ServerWorld.OVERWORLD).getPersistentStateManager().getOrCreate(STATE_TYPE);
        manager.markDirty();
        return manager;
    }

    public record PreviousDimensionS2C(Identifier id, boolean prev) implements CustomPayload {
        public static final CustomPayload.Id<PreviousDimensionS2C> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "previous_dimension"));
        public static final PacketCodec<RegistryByteBuf, PreviousDimensionS2C> PACKET_CODEC = PacketCodec.of(PreviousDimensionS2C::write, PreviousDimensionS2C::new);

        public PreviousDimensionS2C(RegistryByteBuf buf) {
            this(buf.readIdentifier(), buf.readBoolean());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeIdentifier(id);
            buf.writeBoolean(prev);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
