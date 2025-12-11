package dev.creoii.greatbigworld;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.registry.*;
import dev.creoii.greatbigworld.util.network.*;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerGroup;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerManager;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.LevelStem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GreatBigWorld implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final Logger LOGGER = LogManager.getLogger(GreatBigWorld.class);

    public static final ResourceKey<Level> ALTERWORLD_KEY = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(NAMESPACE, "the_alterworld"));
    public static final ResourceKey<LevelStem> ALTERWORLD_OPTIONS = ResourceKey.create(Registries.LEVEL_STEM, Identifier.fromNamespaceAndPath(NAMESPACE, "the_alterworld"));

    @Override
    public void onInitialize() {
        GBWRegistries.register();

        GBWBlocks.register();
        GBWItems.register();
        GBWBlockEntityTypes.register();
        GBWFeatures.register();
        GBWTreeDecoratorTypes.register();
        GBWPlacementModifierTypes.register();
        GBWDensityFunctionTypes.register();
        GBWCommands.register();
        StructureTriggerDataType.register();
        GBWStructureTriggers.register();
        GBWDataComponentTypes.register();

        PayloadTypeRegistry.playC2S().register(StructureTriggerBlockEntity.UpdateStructureTriggerC2S.PACKET_ID, StructureTriggerBlockEntity.UpdateStructureTriggerC2S.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(StructureTriggerBlockEntity.StructureTriggerC2S.PACKET_ID, StructureTriggerBlockEntity.StructureTriggerC2S.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(RequestKnowledgeC2S.PACKET_ID, RequestKnowledgeC2S.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_ID, StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(PreviousDimensionManager.PreviousDimensionS2C.PACKET_ID, PreviousDimensionManager.PreviousDimensionS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncWorldEventS2C.PACKET_ID, SyncWorldEventS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncKnowledgeS2C.PACKET_ID, SyncKnowledgeS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(LearnKnowledgeS2C.PACKET_ID, LearnKnowledgeS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ScreenShakeS2C.PACKET_ID, ScreenShakeS2C.PACKET_CODEC);

        ComponentTooltipAppenderRegistry.addBefore(DataComponents.MAP_ID, GBWDataComponentTypes.KNOWLEDGE);

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
                Map<Knowledge.Type, Set<Knowledge>> knowledge = KnowledgeManager.getInstance().getPlayerKnowledge(context.player());
                if (knowledge == null || knowledge.isEmpty())
                    return;
                ServerPlayNetworking.send(context.player(), new SyncKnowledgeS2C(knowledge));
            });
        });

        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.dimensionTypeRegistration() == Level.OVERWORLD) {
                PreviousDimensionManager manager = PreviousDimensionManager.getServerState(server);
                manager.init(server);
            }
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.tickRateManager().runsNormally()) {
                StructureTriggerManager manager = StructureTriggerManager.getServerState(world);
                manager.tick(world);
            }
        });
    }
}
