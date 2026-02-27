package dev.creoii.greatbigworld.world.dimension;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PreviousDimensionManager extends SavedData {
    public static final Codec<PreviousDimensionManager> CODEC = Codec.unboundedMap(Codec.STRING, ResourceKey.codec(Registries.DIMENSION)).xmap(map -> {
        PreviousDimensionManager manager = new PreviousDimensionManager();
        manager.prevDimensions.putAll(map);
        return manager;
    }, structureTriggerManager -> new HashMap<>(structureTriggerManager.prevDimensions));
    private static final SavedDataType<PreviousDimensionManager> STATE_TYPE = new SavedDataType<>("gbw_previous_dimensions", PreviousDimensionManager::new, CODEC, null);
    private MinecraftServer server;
    private final HashMap<String, ResourceKey<Level>> prevDimensions;
    private final HashMap<String, ResourceKey<Level>> toDimensions;

    public PreviousDimensionManager() {
        prevDimensions = new HashMap<>();
        toDimensions = new HashMap<>();
    }

    public void init(MinecraftServer server) {
        this.server = server;
    }

    @Nullable
    public ResourceKey<Level> getPrevDimension(UUID uuid) {
        return prevDimensions.get(uuid.toString());
    }

    @Nullable
    public ResourceKey<Level> getToDimension(UUID uuid) {
        return toDimensions.get(uuid.toString());
    }

    public void setPrevDimension(UUID uuid, ResourceKey<Level> group) {
        prevDimensions.put(uuid.toString(), group);
        ServerPlayer serverPlayer = server.getPlayerList().getPlayer(uuid);
        if (serverPlayer != null)
            ServerPlayNetworking.send(serverPlayer, new PreviousDimensionS2C(group.identifier(), true));
    }

    public void setToDimension(UUID uuid, ResourceKey<Level> group) {
        toDimensions.put(uuid.toString(), group);
        ServerPlayer serverPlayer = server.getPlayerList().getPlayer(uuid);
        if (serverPlayer != null)
            ServerPlayNetworking.send(serverPlayer, new PreviousDimensionS2C(group.identifier(), false));
    }

    public void removePrev(UUID uuid) {
        prevDimensions.remove(uuid.toString());
    }

    public void removeTo(UUID uuid) {
        toDimensions.remove(uuid.toString());
    }

    public static PreviousDimensionManager getServerState(MinecraftServer server) {
        PreviousDimensionManager manager = server.getLevel(ServerLevel.OVERWORLD).getDataStorage().computeIfAbsent(STATE_TYPE);
        manager.init(server);
        manager.setDirty();
        return manager;
    }

    public record PreviousDimensionS2C(Identifier id, boolean prev) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<PreviousDimensionS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "previous_dimension"));
        public static final StreamCodec<RegistryFriendlyByteBuf, PreviousDimensionS2C> PACKET_CODEC = StreamCodec.ofMember(PreviousDimensionS2C::write, PreviousDimensionS2C::new);

        public PreviousDimensionS2C(RegistryFriendlyByteBuf buf) {
            this(buf.readIdentifier(), buf.readBoolean());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeIdentifier(id);
            buf.writeBoolean(prev);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
