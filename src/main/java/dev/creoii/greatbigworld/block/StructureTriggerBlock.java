package dev.creoii.greatbigworld.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.client.screen.StructureTriggerScreen;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureTriggerBlock extends Block implements BlockEntityProvider, OperatorBlock {
    public static final MapCodec<StructureTriggerBlock> CODEC = createCodec(StructureTriggerBlock::new);
    public static final EnumProperty<TriggerType> TRIGGER_TYPE = EnumProperty.of("trigger_type", TriggerType.class);

    public MapCodec<StructureTriggerBlock> getCodec() {
        return CODEC;
    }

    public StructureTriggerBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(TRIGGER_TYPE, TriggerType.INIT));
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StructureTriggerBlockEntity(pos, state);
    }

    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof StructureTriggerBlockEntity structureTriggerBlockEntity && player.isCreativeLevelTwoOp()) {
            if (player instanceof ClientPlayerEntity clientPlayer) {
                clientPlayer.clientWorld.client.setScreen(new StructureTriggerScreen(structureTriggerBlockEntity));
            }
            return ActionResult.SUCCESS;
        } else return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGER_TYPE);
    }

    public enum TriggerType implements StringIdentifiable {
        INIT,
        TICK;

        @Override
        public String asString() {
            return name().toLowerCase();
        }
    }
}
