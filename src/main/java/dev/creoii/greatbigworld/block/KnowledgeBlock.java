package dev.creoii.greatbigworld.block;

import com.google.common.collect.Sets;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.util.network.LearnKnowledgeS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Optional;

public abstract class KnowledgeBlock extends Block {
    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");

    public KnowledgeBlock(Properties settings) {
        super(settings);
        registerDefaultState(defaultBlockState().setValue(NATURAL, true));
    }

    public abstract WeightedList<Knowledge> getKnowledgePool(BlockState state);

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (!level.isClientSide() && blockState.getValue(NATURAL)) {
            KnowledgeManager knowledgeManager = KnowledgeManager.getServerState(level.getServer());
            Optional<Knowledge> knowledge = getKnowledgePool(blockState).getRandom(level.random);
            if (knowledge.isPresent() && knowledgeManager.learn(player, knowledge.get())) {
                ServerPlayNetworking.send((ServerPlayer) player, new LearnKnowledgeS2C(knowledge.get().type(), Sets.newHashSet(knowledge.get())));
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NATURAL);
    }
}