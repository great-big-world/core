package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.client.ScreenShakeManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ScreenShakeS2C(float intensity, int duration, ScreenShakeManager.Easing easing) implements CustomPayload {
    public static final CustomPayload.Id<ScreenShakeS2C> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "screen_shake"));
    public static final PacketCodec<RegistryByteBuf, ScreenShakeS2C> PACKET_CODEC = PacketCodec.of(ScreenShakeS2C::write, ScreenShakeS2C::new);

    public ScreenShakeS2C(RegistryByteBuf buf) {
        this(buf.readFloat(), buf.readInt(), ScreenShakeManager.Easing.values()[buf.readInt()]);
    }

    public void write(RegistryByteBuf buf) {
        buf.writeFloat(intensity);
        buf.writeInt(duration);
        buf.writeInt(easing.ordinal());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
