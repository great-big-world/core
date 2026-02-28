package dev.creoii.greatbigworld;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.util.network.*;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerGroup;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class GBWNetworking {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(StructureTriggerBlockEntity.UpdateStructureTriggerC2S.PACKET_ID, StructureTriggerBlockEntity.UpdateStructureTriggerC2S.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(StructureTriggerBlockEntity.StructureTriggerC2S.PACKET_ID, StructureTriggerBlockEntity.StructureTriggerC2S.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(RequestKnowledgeC2S.PACKET_ID, RequestKnowledgeC2S.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_ID, StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(PreviousDimensionManager.PreviousDimensionS2C.PACKET_ID, PreviousDimensionManager.PreviousDimensionS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncWorldEventS2C.PACKET_ID, SyncWorldEventS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncKnowledgeS2C.PACKET_ID, SyncKnowledgeS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(LearnKnowledgeS2C.PACKET_ID, LearnKnowledgeS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ScreenShakeS2C.PACKET_ID, ScreenShakeS2C.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(StructureTriggerBlockEntity.UpdateStructureTriggerC2S.PACKET_ID, (updateStructureTriggerC2S, context) -> {
            context.server().execute(() -> {
                if (!context.player().canUseGameMasterBlocks())
                    return;
                BlockEntity blockEntity = context.player().level().getBlockEntity(updateStructureTriggerC2S.pos());
                if (blockEntity instanceof StructureTriggerBlockEntity structureTriggerBlockEntity) {
                    if (updateStructureTriggerC2S.group() != null)
                        structureTriggerBlockEntity.setGroup(Identifier.parse(updateStructureTriggerC2S.group()));
                    structureTriggerBlockEntity.setGroupDataType(updateStructureTriggerC2S.groupDataType());
                    structureTriggerBlockEntity.setTarget(updateStructureTriggerC2S.target());
                    structureTriggerBlockEntity.setTriggerType(StructureTriggerBlock.TriggerType.valueOf(updateStructureTriggerC2S.triggerType().toUpperCase()));
                    structureTriggerBlockEntity.setTickRate(updateStructureTriggerC2S.tickRate());
                    structureTriggerBlockEntity.setFinalState(updateStructureTriggerC2S.finalState());

                    structureTriggerBlockEntity.setChanged();
                }
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(StructureTriggerBlockEntity.StructureTriggerC2S.PACKET_ID, (structureTriggerC2S, context) -> {
            context.server().execute(() -> {
                if (!context.player().canUseGameMasterBlocks())
                    return;
                BlockEntity blockEntity = context.player().level().getBlockEntity(structureTriggerC2S.pos());
                if (blockEntity instanceof StructureTriggerBlockEntity structureTriggerBlockEntity) {
                    Identifier id = Identifier.tryParse(structureTriggerBlockEntity.getFinalState());
                    if (id == null) {
                        id = Identifier.parse("air");
                    }
                    BlockState finalState = BuiltInRegistries.BLOCK.getValue(id).defaultBlockState();

                    StructureTrigger.Built trigger = StructureTrigger.build(GBWRegistries.STRUCTURE_TRIGGERS.getValue(structureTriggerC2S.target()), structureTriggerC2S.pos(), finalState, structureTriggerC2S.tickRate());

                    UUID uuid = UUID.randomUUID();
                    StructureTriggerManager manager = StructureTriggerManager.getServerState(context.player().level());

                    StructureTriggerGroup group = manager.getGroup(uuid);
                    if (group == null) {
                        group = new StructureTriggerGroup(new ArrayList<>(), trigger.trigger().dataType().create());
                        manager.addGroup(uuid, group);
                    }

                    if (StructureTriggerBlock.TriggerType.valueOf(structureTriggerC2S.triggerType().toUpperCase()) == StructureTriggerBlock.TriggerType.INIT) {
                        trigger.trigger().trigger(context.player().level(), structureTriggerC2S.pos(), finalState, group);
                    } else {
                        group.addTrigger(trigger);
                    }
                }
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(RequestKnowledgeC2S.PACKET_ID, (requestKnowledgeC2S, context) -> {
            context.server().execute(() -> {
                Map<Knowledge.Type, Set<Knowledge>> knowledge = KnowledgeManager.getServerState(context.server()).getPlayerKnowledge(context.player());
                if (knowledge == null || knowledge.isEmpty()) {
                    return;
                }
                ServerPlayNetworking.send(context.player(), new SyncKnowledgeS2C(knowledge));
            });
        });
    }
}
