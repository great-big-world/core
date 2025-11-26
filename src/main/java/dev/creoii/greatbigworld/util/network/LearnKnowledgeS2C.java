package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public record LearnKnowledgeS2C(Knowledge.Type type, Set<Knowledge> knowledge) implements CustomPayload {
    public static final Id<LearnKnowledgeS2C> PACKET_ID = new Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "learn_knowledge"));
    public static final PacketCodec<RegistryByteBuf, LearnKnowledgeS2C> PACKET_CODEC = PacketCodec.of(LearnKnowledgeS2C::write, LearnKnowledgeS2C::new);

    public LearnKnowledgeS2C(RegistryByteBuf buf) {
        this(Knowledge.Type.values()[buf.readInt()], readKnowledge(buf));
    }

    private static Set<Knowledge> readKnowledge(RegistryByteBuf buf) {
        int count = buf.readVarInt();
        Set<Knowledge> set = new HashSet<>();

        for (int j = 0; j < count; j++) {
            set.add(new Knowledge(Knowledge.Type.ENCHANTMENT, buf.readIdentifier()));
        }

        return set;
    }

    public void write(RegistryByteBuf buf) {
        buf.writeInt(type.ordinal());
        buf.writeVarInt(knowledge.size());

        for (Knowledge knowledge : knowledge) {
            buf.writeIdentifier(knowledge.data());
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
