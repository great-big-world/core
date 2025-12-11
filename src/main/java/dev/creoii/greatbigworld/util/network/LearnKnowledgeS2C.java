package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record LearnKnowledgeS2C(Knowledge.Type knowledgeType, Set<Knowledge> knowledge) implements CustomPacketPayload {
    public static final Type<LearnKnowledgeS2C> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "learn_knowledge"));
    public static final StreamCodec<RegistryFriendlyByteBuf, LearnKnowledgeS2C> PACKET_CODEC = StreamCodec.ofMember(LearnKnowledgeS2C::write, LearnKnowledgeS2C::new);

    public LearnKnowledgeS2C(RegistryFriendlyByteBuf buf) {
        this(Knowledge.Type.values()[buf.readInt()], readKnowledge(buf));
    }

    private static Set<Knowledge> readKnowledge(RegistryFriendlyByteBuf buf) {
        int count = buf.readVarInt();
        Set<Knowledge> set = new HashSet<>();

        for (int j = 0; j < count; j++) {
            set.add(new Knowledge(Knowledge.Type.ENCHANTMENT, buf.readIdentifier()));
        }

        return set;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(knowledgeType.ordinal());
        buf.writeVarInt(knowledge.size());

        for (Knowledge knowledge : knowledge) {
            buf.writeIdentifier(knowledge.data());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
