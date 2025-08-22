package dev.creoii.greatbigworld.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
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
        if (player.isCreativeLevelTwoOp() && world.getBlockEntity(pos) instanceof StructureTriggerBlockEntity) {
            if (!world.isClient) {
                ServerPlayNetworking.send((ServerPlayerEntity) player, new OpenStructureTriggerScreenS2C(pos));
            }
            return ActionResult.SUCCESS;
        } else return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGER_TYPE);
    }

    public record OpenStructureTriggerScreenS2C(BlockPos pos) implements CustomPayload {
        public static final CustomPayload.Id<OpenStructureTriggerScreenS2C> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "open_structure_trigger_screen"));
        public static final PacketCodec<RegistryByteBuf, OpenStructureTriggerScreenS2C> PACKET_CODEC = PacketCodec.of(OpenStructureTriggerScreenS2C::write, OpenStructureTriggerScreenS2C::new);

        public OpenStructureTriggerScreenS2C(RegistryByteBuf buf) {
            this(buf.readBlockPos());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeBlockPos(pos);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
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
