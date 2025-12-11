package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.client.ScreenShakeManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ScreenShakeS2C(float intensity, int duration, ScreenShakeManager.Easing easing) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ScreenShakeS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "screen_shake"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ScreenShakeS2C> PACKET_CODEC = StreamCodec.ofMember(ScreenShakeS2C::write, ScreenShakeS2C::new);

    public ScreenShakeS2C(RegistryFriendlyByteBuf buf) {
        this(buf.readFloat(), buf.readInt(), ScreenShakeManager.Easing.values()[buf.readInt()]);
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeFloat(intensity);
        buf.writeInt(duration);
        buf.writeInt(easing.ordinal());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
