package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SyncWorldEventS2C(int eventId, BlockPos pos, int data) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncWorldEventS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "sync_world_event"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncWorldEventS2C> PACKET_CODEC = StreamCodec.ofMember(SyncWorldEventS2C::write, SyncWorldEventS2C::new);

    public SyncWorldEventS2C(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBlockPos(), buf.readInt());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(eventId);
        buf.writeBlockPos(pos);
        buf.writeInt(data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
