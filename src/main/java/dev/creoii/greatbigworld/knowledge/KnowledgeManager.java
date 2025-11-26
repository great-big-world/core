package dev.creoii.greatbigworld.knowledge;

import com.mojang.serialization.Codec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class KnowledgeManager extends PersistentState {
    public static final Codec<Map<Knowledge.Type, Set<Knowledge>>> KNOWLEDGE_CODEC = Codec.unboundedMap(Knowledge.Type.CODEC, Knowledge.CODEC.listOf()).xmap(typeListMap -> typeListMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue()))), typeSetMap -> typeSetMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue()))));
    public static final Codec<KnowledgeManager> CODEC = Codec.unboundedMap(Codec.STRING, KNOWLEDGE_CODEC).xmap(map -> {
        KnowledgeManager knowledgeManager = KnowledgeManager.INSTANCE;
        knowledgeManager.playerKnowledge.putAll(map);
        return knowledgeManager;
    }, knowledgeManager -> new HashMap<>(knowledgeManager.playerKnowledge));
    private static final PersistentStateType<KnowledgeManager> STATE_TYPE = new PersistentStateType<>("gbw_knowledge", KnowledgeManager::getInstance, CODEC, null);
    private static final KnowledgeManager INSTANCE = new KnowledgeManager();
    private final Map<String, Map<Knowledge.Type, Set<Knowledge>>> playerKnowledge = new HashMap<>();

    public static KnowledgeManager getInstance() {
        return INSTANCE;
    }

    public Map<String, Map<Knowledge.Type, Set<Knowledge>>> getAllKnowledge() {
        return playerKnowledge;
    }

    @Nullable
    public Map<Knowledge.Type, Set<Knowledge>> getPlayerKnowledge(PlayerEntity player) {
        return playerKnowledge.getOrDefault(player.getUuid().toString(), null);
    }

    @Nullable
    public Set<Knowledge> getPlayerKnowledge(PlayerEntity player, Knowledge.Type type) {
        Map<Knowledge.Type, Set<Knowledge>> map = playerKnowledge.getOrDefault(player.getUuid().toString(), null);
        if (map != null) {
            return map.getOrDefault(type, null);
        }
        return null;
    }

    /**
     * @return true if successfully learnt new knowledge, false if already known
     */
    public boolean learn(PlayerEntity player, Knowledge knowledge) {
        if (playerKnowledge.containsKey(player.getUuid().toString())) {
            Map<Knowledge.Type, Set<Knowledge>> map = playerKnowledge.get(player.getUuid().toString());

            if (map.containsKey(knowledge.type())) {
                return map.get(knowledge.type()).add(knowledge);
            } else {
                Set<Knowledge> knowledges = new HashSet<>();
                knowledges.add(knowledge);
                map.put(knowledge.type(), knowledges);
                return true;
            }
        } else {
            Map<Knowledge.Type, Set<Knowledge>> map = new HashMap<>();

            Set<Knowledge> knowledges = new HashSet<>();
            knowledges.add(knowledge);
            map.put(knowledge.type(), knowledges);

            playerKnowledge.put(player.getUuid().toString(), map);
            return true;
        }
    }

    public static KnowledgeManager getServerState(MinecraftServer server) {
        KnowledgeManager manager = server.getOverworld().getPersistentStateManager().getOrCreate(STATE_TYPE);
        manager.markDirty();
        return manager;
    }
}
