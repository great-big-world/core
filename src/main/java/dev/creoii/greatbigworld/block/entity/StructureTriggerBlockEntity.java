package dev.creoii.greatbigworld.block.entity;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.registry.GBWBlockEntityTypes;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerGroup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StructureTriggerBlockEntity extends BlockEntity {
    public static final Identifier DEFAULT_TARGET = Identifier.of(GreatBigWorld.NAMESPACE, "empty");
    public static final StructureTriggerBlock.TriggerType DEFAULT_TRIGGER_TYPE = StructureTriggerBlock.TriggerType.INIT;
    public static final int DEFAULT_TICK_RATE = 20;
    public static final String DEFAULT_FINAL_STATE = "minecraft:air";
    public static final String GROUP_KEY = "group";
    public static final String GROUP_DATA_TYPE_KEY = "group_data_type";
    public static final String TARGET_KEY = "target";
    public static final String TRIGGER_TYPE_KEY = "trigger_type";
    public static final String TICK_RATE_KEY = "tick_rate";
    public static final String FINAL_STATE_KEY = "final_state";
    @Nullable private Identifier group;
    private StructureTriggerGroup.DataType groupDataType;
    private Identifier target;
    private StructureTriggerBlock.TriggerType triggerType;
    private int tickRate;
    private String finalState;

    public StructureTriggerBlockEntity(BlockPos pos, BlockState state) {
        super(GBWBlockEntityTypes.STRUCTURE_TRIGGER, pos, state);
        group = null;
        groupDataType = StructureTriggerGroup.DataType.INT;
        target = DEFAULT_TARGET;
        triggerType = DEFAULT_TRIGGER_TYPE;
        tickRate = DEFAULT_TICK_RATE;
        finalState = DEFAULT_FINAL_STATE;
    }

    public @Nullable Identifier getGroup() {
        return group;
    }

    public StructureTriggerGroup.DataType getGroupDataType() {
        return groupDataType;
    }

    public Identifier getTarget() {
        return target;
    }

    public StructureTriggerBlock.TriggerType getTriggerType() {
        return triggerType;
    }

    public int getTickRate() {
        return tickRate;
    }

    public String getFinalState() {
        return finalState;
    }

    public void setGroup(@Nullable Identifier group) {
        this.group = group;
    }

    public void setGroupDataType(@Nullable StructureTriggerGroup.DataType groupDataType) {
        this.groupDataType = groupDataType;
    }

    public void setTarget(Identifier target) {
        this.target = target;
    }

    public void setTriggerType(StructureTriggerBlock.TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public void setTickRate(int tickRate) {
        this.tickRate = tickRate;
    }

    public void setFinalState(String finalState) {
        this.finalState = finalState;
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putString(GROUP_KEY, group == null ? "" : group.toString());
        nbt.putString(GROUP_DATA_TYPE_KEY, groupDataType.name().toLowerCase());
        nbt.put(TARGET_KEY, Identifier.CODEC, target);
        nbt.putString(TRIGGER_TYPE_KEY, triggerType.asString().toLowerCase());
        nbt.putInt(TICK_RATE_KEY, tickRate);
        nbt.putString(FINAL_STATE_KEY, finalState);
    }

    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        String nbtGroup = nbt.getString(GROUP_KEY, "");
        group = nbtGroup == null || nbtGroup.isEmpty() ? null : Identifier.of(nbtGroup);
        groupDataType = StructureTriggerGroup.DataType.valueOf(nbt.getString(GROUP_DATA_TYPE_KEY, "int").toUpperCase());
        target = nbt.get(TARGET_KEY, Identifier.CODEC).orElse(DEFAULT_TARGET);
        Optional<String> triggerTypeString = nbt.getString(TRIGGER_TYPE_KEY);
        triggerType = triggerTypeString.map(s -> StructureTriggerBlock.TriggerType.valueOf(s.toUpperCase())).orElse(DEFAULT_TRIGGER_TYPE);
        tickRate = nbt.getInt(TICK_RATE_KEY, DEFAULT_TICK_RATE);
        finalState = nbt.getString(FINAL_STATE_KEY, DEFAULT_FINAL_STATE);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createComponentlessNbt(registries);
    }

    public record UpdateStructureTriggerC2S(BlockPos pos, @Nullable String group, StructureTriggerGroup.DataType groupDataType, Identifier target, String triggerType, int tickRate, String finalState) implements CustomPayload {
        public static final CustomPayload.Id<UpdateStructureTriggerC2S> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "update_structure_trigger"));
        public static final PacketCodec<RegistryByteBuf, UpdateStructureTriggerC2S> PACKET_CODEC = PacketCodec.of(UpdateStructureTriggerC2S::write, UpdateStructureTriggerC2S::new);

        public UpdateStructureTriggerC2S(RegistryByteBuf buf) {
            this(buf.readBlockPos(), buf.readString(), StructureTriggerGroup.DataType.valueOf(buf.readString().toUpperCase()), buf.readIdentifier(), buf.readString(), buf.readInt(), buf.readString());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeString(group == null ? "" : group);
            buf.writeString(groupDataType == null ? "" : groupDataType.name().toLowerCase());
            buf.writeIdentifier(target);
            buf.writeString(triggerType);
            buf.writeInt(tickRate);
            buf.writeString(finalState);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }

    public record StructureTriggerC2S(BlockPos pos, Identifier target, String triggerType, int tickRate) implements CustomPayload {
        public static final CustomPayload.Id<StructureTriggerC2S> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "structure_trigger"));
        public static final PacketCodec<RegistryByteBuf, StructureTriggerC2S> PACKET_CODEC = PacketCodec.of(StructureTriggerC2S::write, StructureTriggerC2S::new);

        public StructureTriggerC2S(RegistryByteBuf buf) {
            this(buf.readBlockPos(), buf.readIdentifier(), buf.readString(), buf.readInt());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeIdentifier(target);
            buf.writeString(triggerType);
            buf.writeInt(tickRate);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
