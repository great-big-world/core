package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SyncKnowledgeS2C(Map<Knowledge.Type, Set<Knowledge>> knowledge) implements CustomPacketPayload {
    public static final Type<SyncKnowledgeS2C> PACKET_ID = new Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "sync_knowledge"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncKnowledgeS2C> PACKET_CODEC = StreamCodec.ofMember(SyncKnowledgeS2C::write, SyncKnowledgeS2C::new);

    public SyncKnowledgeS2C(RegistryFriendlyByteBuf buf) {
        this(readKnowledgeMap(buf));
    }

    private static Map<Knowledge.Type, Set<Knowledge>> readKnowledgeMap(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<Knowledge.Type, Set<Knowledge>> map = new HashMap<>();

        for (int i = 0; i < size; i++) {
            Knowledge.Type type = Knowledge.Type.valueOf(buf.readUtf());

            int count = buf.readVarInt();
            Set<Knowledge> set = new HashSet<>();

            for (int j = 0; j < count; j++) {
                set.add(new Knowledge(type, buf.readIdentifier()));
            }

            map.put(type, set);
        }

        return map;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(knowledge.size());

        for (Map.Entry<Knowledge.Type, Set<Knowledge>> entry : knowledge.entrySet()) {
            buf.writeUtf(entry.getKey().name());

            Set<Knowledge> set = entry.getValue();
            buf.writeVarInt(set.size());

            for (Knowledge knowledge : set) {
                buf.writeIdentifier(knowledge.data());
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
