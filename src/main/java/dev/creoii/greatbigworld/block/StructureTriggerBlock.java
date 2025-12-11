package dev.creoii.greatbigworld.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

public class StructureTriggerBlock extends Block implements EntityBlock, GameMasterBlock {
    public static final MapCodec<StructureTriggerBlock> CODEC = simpleCodec(StructureTriggerBlock::new);
    public static final EnumProperty<TriggerType> TRIGGER_TYPE = EnumProperty.create("trigger_type", TriggerType.class);

    public MapCodec<StructureTriggerBlock> codec() {
        return CODEC;
    }

    public StructureTriggerBlock(BlockBehaviour.Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(TRIGGER_TYPE, TriggerType.INIT));
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StructureTriggerBlockEntity(pos, state);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.canUseGameMasterBlocks() && world.getBlockEntity(pos) instanceof StructureTriggerBlockEntity) {
            if (!world.isClientSide()) {
                ServerPlayNetworking.send((ServerPlayer) player, new OpenStructureTriggerScreenS2C(pos));
            }
            return InteractionResult.SUCCESS;
        } else return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TRIGGER_TYPE);
    }

    public record OpenStructureTriggerScreenS2C(BlockPos pos) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<OpenStructureTriggerScreenS2C> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "open_structure_trigger_screen"));
        public static final StreamCodec<RegistryFriendlyByteBuf, OpenStructureTriggerScreenS2C> PACKET_CODEC = StreamCodec.ofMember(OpenStructureTriggerScreenS2C::write, OpenStructureTriggerScreenS2C::new);

        public OpenStructureTriggerScreenS2C(RegistryFriendlyByteBuf buf) {
            this(buf.readBlockPos());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public enum TriggerType implements StringRepresentable {
        INIT,
        TICK;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
