package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record RequestKnowledgeC2S() implements CustomPacketPayload {
    public static final RequestKnowledgeC2S INSTANCE = new RequestKnowledgeC2S();
    public static final Type<RequestKnowledgeC2S> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "request_knowledge"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestKnowledgeC2S> PACKET_CODEC = StreamCodec.ofMember((value, buf) -> {
    }, buf -> INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
