package dev.creoii.greatbigworld.block.entity;

import com.mojang.serialization.Codec;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.registry.GBWBlockEntityTypes;
import dev.creoii.greatbigworld.util.Codecs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class KnowledgeBlockEntity extends BlockEntity {
    @Nullable
    private Knowledge knowledge = null;
    private final Storage storage;

    public KnowledgeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(GBWBlockEntityTypes.KNOWLEDGE_BLOCK, blockPos, blockState);
        storage = new Storage(new HashSet<>());
    }

    public void setPlayerLearned(Player player) {
        storage.learnedPlayers.add(player.getUUID());
    }

    public boolean hasPlayerLearned(Player player) {
        return storage.learnedPlayers.contains(player.getUUID());
    }

    public @Nullable Knowledge getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(@Nullable Knowledge knowledge) {
        this.knowledge = knowledge;
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        storage.learnedPlayers.clear();
        valueInput.read("learned", Storage.CODEC).ifPresent(storage1 -> {
            storage.learnedPlayers.addAll(storage1.learnedPlayers);
        });
        if (valueInput.contains("knowledge")) {
            valueInput.read("knowledge", Knowledge.CODEC).ifPresent(knowledge1 -> {
                knowledge = knowledge1;
            });
        }
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        valueOutput.store("learned", Storage.CODEC, storage);
        if (knowledge != null) {
            valueOutput.store("knowledge", Knowledge.CODEC, knowledge);
        }
    }

    public record Storage(Set<UUID> learnedPlayers) {
        private static final Codec<Storage> CODEC = Codec.list(Codecs.UUID).xmap(uuids -> new Storage(new HashSet<>(uuids)), storage1 -> storage1.learnedPlayers.stream().toList());
    }
}
