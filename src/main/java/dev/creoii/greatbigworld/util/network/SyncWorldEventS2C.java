package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record SyncWorldEventS2C(int eventId, BlockPos pos, int data) implements CustomPayload {
    public static final CustomPayload.Id<SyncWorldEventS2C> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "sync_world_event"));
    public static final PacketCodec<RegistryByteBuf, SyncWorldEventS2C> PACKET_CODEC = PacketCodec.of(SyncWorldEventS2C::write, SyncWorldEventS2C::new);

    public SyncWorldEventS2C(RegistryByteBuf buf) {
        this(buf.readInt(), buf.readBlockPos(), buf.readInt());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeInt(eventId);
        buf.writeBlockPos(pos);
        buf.writeInt(data);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
