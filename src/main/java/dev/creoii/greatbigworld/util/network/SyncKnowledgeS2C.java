package dev.creoii.greatbigworld.util.network;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record SyncKnowledgeS2C(Map<Knowledge.Type, Set<Knowledge>> knowledge) implements CustomPayload {
    public static final Id<SyncKnowledgeS2C> PACKET_ID = new Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "sync_knowledge"));
    public static final PacketCodec<RegistryByteBuf, SyncKnowledgeS2C> PACKET_CODEC = PacketCodec.of(SyncKnowledgeS2C::write, SyncKnowledgeS2C::new);

    public SyncKnowledgeS2C(RegistryByteBuf buf) {
        this(readKnowledgeMap(buf));
    }

    private static Map<Knowledge.Type, Set<Knowledge>> readKnowledgeMap(RegistryByteBuf buf) {
        int size = buf.readVarInt();
        Map<Knowledge.Type, Set<Knowledge>> map = new HashMap<>();

        for (int i = 0; i < size; i++) {
            Knowledge.Type type = Knowledge.Type.valueOf(buf.readString());

            int count = buf.readVarInt();
            Set<Knowledge> set = new HashSet<>();

            for (int j = 0; j < count; j++) {
                set.add(new Knowledge(type, buf.readIdentifier()));
            }

            map.put(type, set);
        }

        return map;
    }

    public void write(RegistryByteBuf buf) {
        buf.writeVarInt(knowledge.size());

        for (Map.Entry<Knowledge.Type, Set<Knowledge>> entry : knowledge.entrySet()) {
            buf.writeString(entry.getKey().name());

            Set<Knowledge> set = entry.getValue();
            buf.writeVarInt(set.size());

            for (Knowledge knowledge : set) {
                buf.writeIdentifier(knowledge.data());
            }
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
