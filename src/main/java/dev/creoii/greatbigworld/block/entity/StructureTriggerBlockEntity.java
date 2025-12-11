package dev.creoii.greatbigworld.block.entity;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.registry.GBWBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class StructureTriggerBlockEntity extends BlockEntity {
    public static final Identifier DEFAULT_DATA_TYPE = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "empty");
    public static final Identifier DEFAULT_TARGET = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "empty");
    public static final int DEFAULT_TICK_RATE = 20;
    public static final String DEFAULT_FINAL_STATE = "minecraft:air";
    public static final String GROUP_KEY = "group";
    public static final String GROUP_DATA_TYPE_KEY = "group_data_type";
    public static final String TARGET_KEY = "target";
    public static final String TRIGGER_TYPE_KEY = "trigger_type";
    public static final String TICK_RATE_KEY = "tick_rate";
    public static final String FINAL_STATE_KEY = "final_state";
    @Nullable private Identifier group;
    private Identifier groupDataType;
    private Identifier target;
    private StructureTriggerBlock.TriggerType triggerType;
    private int tickRate;
    private String finalState;

    public StructureTriggerBlockEntity(BlockPos pos, BlockState state) {
        super(GBWBlockEntityTypes.STRUCTURE_TRIGGER, pos, state);
        group = null;
        groupDataType = DEFAULT_DATA_TYPE;
        target = DEFAULT_TARGET;
        triggerType = StructureTriggerBlock.TriggerType.INIT;
        tickRate = DEFAULT_TICK_RATE;
        finalState = DEFAULT_FINAL_STATE;
    }

    public @Nullable Identifier getGroup() {
        return group;
    }

    public Identifier getGroupDataType() {
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

    public void setGroupDataType(Identifier groupDataType) {
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

    @Override
    protected void saveAdditional(ValueOutput view) {
        view.putString(GROUP_KEY, group == null ? "" : group.toString());
        view.store(GROUP_DATA_TYPE_KEY, Identifier.CODEC, groupDataType);
        view.store(TARGET_KEY, Identifier.CODEC, target);
        view.putString(TRIGGER_TYPE_KEY, triggerType.getSerializedName().toLowerCase());
        view.putInt(TICK_RATE_KEY, tickRate);
        view.putString(FINAL_STATE_KEY, finalState);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        String nbtGroup = view.getStringOr(GROUP_KEY, "");
        group = nbtGroup.isEmpty() ? null : Identifier.parse(nbtGroup);
        groupDataType = view.read(GROUP_DATA_TYPE_KEY, Identifier.CODEC).orElse(DEFAULT_DATA_TYPE);
        target = view.read(TARGET_KEY, Identifier.CODEC).orElse(DEFAULT_TARGET);
        triggerType = StructureTriggerBlock.TriggerType.valueOf(view.getStringOr(TRIGGER_TYPE_KEY, "INIT").toUpperCase());
        tickRate = view.getIntOr(TICK_RATE_KEY, DEFAULT_TICK_RATE);
        finalState = view.getStringOr(FINAL_STATE_KEY, DEFAULT_FINAL_STATE);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    public record UpdateStructureTriggerC2S(BlockPos pos, @Nullable String group, Identifier groupDataType, Identifier target, String triggerType, int tickRate, String finalState) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<UpdateStructureTriggerC2S> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "update_structure_trigger"));
        public static final StreamCodec<RegistryFriendlyByteBuf, UpdateStructureTriggerC2S> PACKET_CODEC = StreamCodec.ofMember(UpdateStructureTriggerC2S::write, UpdateStructureTriggerC2S::new);

        public UpdateStructureTriggerC2S(RegistryFriendlyByteBuf buf) {
            this(buf.readBlockPos(), buf.readUtf(), buf.readIdentifier(), buf.readIdentifier(), buf.readUtf(), buf.readInt(), buf.readUtf());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeUtf(group == null ? "" : group);
            buf.writeIdentifier(groupDataType);
            buf.writeIdentifier(target);
            buf.writeUtf(triggerType);
            buf.writeInt(tickRate);
            buf.writeUtf(finalState);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }

    public record StructureTriggerC2S(BlockPos pos, Identifier target, String triggerType, int tickRate) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<StructureTriggerC2S> PACKET_ID = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "structure_trigger"));
        public static final StreamCodec<RegistryFriendlyByteBuf, StructureTriggerC2S> PACKET_CODEC = StreamCodec.ofMember(StructureTriggerC2S::write, StructureTriggerC2S::new);

        public StructureTriggerC2S(RegistryFriendlyByteBuf buf) {
            this(buf.readBlockPos(), buf.readIdentifier(), buf.readUtf(), buf.readInt());
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
            buf.writeIdentifier(target);
            buf.writeUtf(triggerType);
            buf.writeInt(tickRate);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_ID;
        }
    }
}
