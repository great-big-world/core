package dev.creoii.greatbigworld.knowledge;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class KnowledgeManager extends SavedData {
    public static final Codec<Map<Knowledge.Type, Set<Knowledge>>> KNOWLEDGE_CODEC = Codec.unboundedMap(Knowledge.Type.CODEC, Knowledge.CODEC.listOf()).xmap(typeListMap -> typeListMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue()))), typeSetMap -> typeSetMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new ArrayList<>(e.getValue()))));
    public static final Codec<KnowledgeManager> CODEC = Codec.unboundedMap(Codec.STRING, KNOWLEDGE_CODEC).xmap(map -> {
        KnowledgeManager knowledgeManager = new KnowledgeManager();
        knowledgeManager.playerKnowledge.putAll(map);
        return knowledgeManager;
    }, knowledgeManager -> new HashMap<>(knowledgeManager.playerKnowledge));
    private static final SavedDataType<KnowledgeManager> STATE_TYPE = new SavedDataType<>("gbw_knowledge", KnowledgeManager::new, CODEC, null);
    private final Map<String, Map<Knowledge.Type, Set<Knowledge>>> playerKnowledge = new HashMap<>();

    public static Map<Knowledge.Type, Set<Knowledge>> createEmptyKnowledge() {
        Map<Knowledge.Type, Set<Knowledge>> empty = new HashMap<>();
        for (Knowledge.Type type : Knowledge.Type.values()) {
            empty.put(type, new HashSet<>());
        }
        return empty;
    }

    public Map<String, Map<Knowledge.Type, Set<Knowledge>>> getAllKnowledge() {
        return playerKnowledge;
    }

    @Nullable
    public Map<Knowledge.Type, Set<Knowledge>> getPlayerKnowledge(Player player) {
        return playerKnowledge.getOrDefault(player.getUUID().toString(), null);
    }

    @Nullable
    public Set<Knowledge> getPlayerKnowledge(Player player, Knowledge.Type type) {
        Map<Knowledge.Type, Set<Knowledge>> map = playerKnowledge.getOrDefault(player.getUUID().toString(), null);
        if (map != null) {
            return map.getOrDefault(type, null);
        }
        return null;
    }

    /**
     * @return true if successfully learnt new knowledge, false if already known
     */
    public boolean learn(Player player, Knowledge knowledge) {
        if (playerKnowledge.containsKey(player.getUUID().toString())) {
            Map<Knowledge.Type, Set<Knowledge>> map = playerKnowledge.get(player.getUUID().toString());

            if (map.containsKey(knowledge.type())) {
                if (map.get(knowledge.type()).add(knowledge)) {
                    emitParticles(player);
                    return true;
                }
            } else {
                Set<Knowledge> knowledges = new HashSet<>();
                knowledges.add(knowledge);
                map.put(knowledge.type(), knowledges);
                emitParticles(player);
                return true;
            }
        } else {
            Map<Knowledge.Type, Set<Knowledge>> map = new HashMap<>();

            Set<Knowledge> knowledges = new HashSet<>();
            knowledges.add(knowledge);
            map.put(knowledge.type(), knowledges);

            playerKnowledge.put(player.getUUID().toString(), map);
            emitParticles(player);
            return true;
        }
        return false;
    }

    private void emitParticles(Player player) {
        Level level = player.level();
        if (level.isClientSide())
            return;

        RandomSource random = level.getRandom();
        int count = random.nextIntBetweenInclusive(20, 30);
        Vec3 min = player.getBoundingBox().getMinPosition();
        Vec3 max = player.getBoundingBox().getMaxPosition();
        for (int i = 0; i < count; ++i) {
            double x = min.x + random.nextFloat() * (max.x - min.x);
            double y = min.y + random.nextFloat() * (max.y - min.y);
            double z = min.z + random.nextFloat() * (max.z - min.z);
            ((ServerLevel) level).sendParticles(ParticleTypes.ENCHANT, x, y, z, 1, 0d, 1d * random.nextFloat(), 0d, 2d);
        }
    }

    public static KnowledgeManager getServerState(MinecraftServer server) {
        KnowledgeManager manager = server.overworld().getDataStorage().computeIfAbsent(STATE_TYPE);
        manager.setDirty();
        return manager;
    }
}
