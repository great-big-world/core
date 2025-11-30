package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestKnowledgeC2S() implements CustomPayload {
    public static final RequestKnowledgeC2S INSTANCE = new RequestKnowledgeC2S();
    public static final Id<RequestKnowledgeC2S> PACKET_ID = new Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "request_knowledge"));
    public static final PacketCodec<RegistryByteBuf, RequestKnowledgeC2S> PACKET_CODEC = PacketCodec.of((value, buf) -> {
    }, buf -> INSTANCE);

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
