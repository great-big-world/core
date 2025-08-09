package dev.creoii.greatbigworld.world.structuretrigger;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class StructureTriggerManager extends PersistentState {
    public static final Codec<BlockPos> BLOCK_POS_STRING_CODEC = Codec.STRING.xmap(
            str -> {
                String[] parts = str.split(",");
                return new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            }, pos -> pos.getX() + "," + pos.getY() + "," + pos.getZ()
    );
    public static final Codec<StructureTriggerManager> CODEC = Codec.unboundedMap(BLOCK_POS_STRING_CODEC, StructureTriggerInfo.CODEC).xmap(posStructureTriggerInfoMap -> {
        StructureTriggerManager manager = new StructureTriggerManager();
        manager.structureTriggers.putAll(posStructureTriggerInfoMap);
        return manager;
    }, structureTriggerManager -> new HashMap<>(structureTriggerManager.structureTriggers));
    private static final PersistentStateType<StructureTriggerManager> STATE_TYPE = new PersistentStateType<>("gbw_structure_triggers", StructureTriggerManager::new, CODEC, null);
    private final HashMap<BlockPos, StructureTriggerInfo> structureTriggers;

    public StructureTriggerManager() {
        structureTriggers = new HashMap<>();
    }

    @Nullable
    public StructureTriggerInfo getTrigger(BlockPos pos) {
        return structureTriggers.get(pos);
    }

    public void addTrigger(BlockPos pos, StructureTriggerInfo trigger) {
        structureTriggers.put(pos, trigger);

        StructureTriggerInfo triggerInfo = structureTriggers.get(pos);
        if (triggerInfo != null) {
            if (triggerInfo.trigger.structureStart().getValue() == null)
                return;
            ((StructureTriggerGroupContainer) (Object) triggerInfo.trigger.structureStart().getValue()).gbw$addStructureTrigger(triggerInfo.trigger);
        }
    }

    public void removeTrigger(BlockPos pos) {
        if (structureTriggers.containsKey(pos)) {
            structureTriggers.remove(pos);

            StructureTriggerInfo triggerInfo = structureTriggers.get(pos);
            if (triggerInfo != null) {
                if (triggerInfo.trigger.structureStart().getValue() == null)
                    return;
                ((StructureTriggerGroupContainer) (Object) triggerInfo.trigger.structureStart().getValue()).gbw$removeStructureTrigger(triggerInfo.trigger);
            }
        }
    }

    public void init(ServerWorld world) {
        Optional<Registry<Structure>> structureRegistry = world.getRegistryManager().getOptional(RegistryKeys.STRUCTURE);
        structureRegistry.ifPresent(structures -> structureTriggers.forEach((pos, info) -> {
            StructureStart structureStart = world.getStructureAccessor().getStructureAt(pos, structures.get(info.structureRef));
            if (structureStart != StructureStart.DEFAULT) {
                info.trigger.structureStart().setValue(structureStart);
            }
        }));
    }

    public void tick(ServerWorld world) {
        List<Map.Entry<BlockPos, StructureTriggerInfo>> entries = new ArrayList<>(structureTriggers.entrySet());
        for (int i = entries.size() - 1; i >= 0; --i) {
            Map.Entry<BlockPos, StructureTriggerInfo> entry = entries.get(i);
            BlockPos pos = entry.getKey();
            StructureTriggerInfo triggerInfo = entry.getValue();
            if (world.getTime() % triggerInfo.tickRate == 0) {
                StructureTriggerGroup group = null;
                if (triggerInfo.trigger().structureStart().getValue() != null)
                    group = ((StructureTriggerGroupContainer) (Object) triggerInfo.trigger().structureStart().getValue()).gbw$getStructureTriggerGroup(triggerInfo.trigger().group());

                if (!triggerInfo.trigger.trigger(world, pos, triggerInfo.state, group)) {
                    removeTrigger(pos);
                }
            }
        }
    }

    public static StructureTriggerManager getServerState(ServerWorld world) {
        StructureTriggerManager manager = world.getPersistentStateManager().getOrCreate(STATE_TYPE);
        manager.markDirty();
        return manager;
    }

    public record StructureTriggerInfo(RegistryKey<Structure> structureRef, StructureTrigger trigger, BlockState state, int tickRate) {
        public static final Codec<StructureTriggerInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(RegistryKey.createCodec(RegistryKeys.STRUCTURE).fieldOf("structure_ref").forGetter(info -> info.structureRef), Identifier.CODEC.fieldOf("trigger_id").forGetter(info -> GreatBigWorld.STRUCTURE_TRIGGERS.getId(info.trigger)), BlockState.CODEC.fieldOf("state").forGetter(info -> info.state), Codec.INT.fieldOf("tick_rate").forGetter(info -> info.tickRate)).apply(instance, StructureTriggerInfo::new));

        public StructureTriggerInfo(RegistryKey<Structure> structureRef, Identifier trigger, BlockState state, int tickRate) {
            this(structureRef, GreatBigWorld.STRUCTURE_TRIGGERS.get(trigger), state, tickRate);
        }
    }
}
