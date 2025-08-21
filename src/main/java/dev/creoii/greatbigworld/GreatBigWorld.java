package dev.creoii.greatbigworld;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.registry.*;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerGroup;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerGroupContainer;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GreatBigWorld implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final Logger LOGGER = LogManager.getLogger(GreatBigWorld.class);

    public static final RegistryKey<World> ALTERWORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(NAMESPACE, "the_alterworld"));;

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
        GBWStructureTriggers.register();

        PayloadTypeRegistry.playC2S().register(StructureTriggerBlockEntity.UpdateStructureTriggerC2S.PACKET_ID, StructureTriggerBlockEntity.UpdateStructureTriggerC2S.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(StructureTriggerBlockEntity.StructureTriggerC2S.PACKET_ID, StructureTriggerBlockEntity.StructureTriggerC2S.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_ID, StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(StructureTriggerBlockEntity.UpdateStructureTriggerC2S.PACKET_ID, (updateStructureTriggerC2S, context) -> {
            context.server().execute(() -> {
                if (!context.player().isCreativeLevelTwoOp())
                    return;
                BlockEntity blockEntity = context.player().getWorld().getBlockEntity(updateStructureTriggerC2S.pos());
                if (blockEntity instanceof StructureTriggerBlockEntity structureTriggerBlockEntity) {
                    if (updateStructureTriggerC2S.group() != null)
                        structureTriggerBlockEntity.setGroup(Identifier.of(updateStructureTriggerC2S.group()));
                    structureTriggerBlockEntity.setGroupDataType(updateStructureTriggerC2S.groupDataType());
                    structureTriggerBlockEntity.setTarget(updateStructureTriggerC2S.target());
                    structureTriggerBlockEntity.setTriggerType(StructureTriggerBlock.TriggerType.valueOf(updateStructureTriggerC2S.triggerType().toUpperCase()));
                    structureTriggerBlockEntity.setTickRate(updateStructureTriggerC2S.tickRate());
                    structureTriggerBlockEntity.setFinalState(updateStructureTriggerC2S.finalState());
                }
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(StructureTriggerBlockEntity.StructureTriggerC2S.PACKET_ID, (structureTriggerC2S, context) -> {
            context.server().execute(() -> {
                if (!context.player().isCreativeLevelTwoOp())
                    return;
                BlockEntity blockEntity = context.player().getWorld().getBlockEntity(structureTriggerC2S.pos());
                if (blockEntity instanceof StructureTriggerBlockEntity structureTriggerBlockEntity) {
                    StructureTrigger trigger = GBWRegistries.STRUCTURE_TRIGGERS.get(structureTriggerBlockEntity.getTarget());
                    Identifier id = Identifier.tryParse(structureTriggerBlockEntity.getFinalState());
                    if (trigger != null && id != null) {
                        BlockState finalState = Registries.BLOCK.get(id).getDefaultState();
                        if (StructureTriggerBlock.TriggerType.valueOf(structureTriggerC2S.triggerType().toUpperCase()) == StructureTriggerBlock.TriggerType.INIT) {
                            StructureTriggerGroup group = ((StructureTriggerGroupContainer) (Object) trigger.structureStart().getValue()).gbw$getStructureTriggerGroup(trigger.group());
                            trigger.trigger((ServerWorld) context.player().getWorld(), structureTriggerC2S.pos(), finalState, group);
                        } else {
                            StructureTriggerManager manager = StructureTriggerManager.getServerState((ServerWorld) context.player().getWorld());
                            manager.addTrigger(structureTriggerC2S.pos(), new StructureTriggerManager.StructureTriggerInfo(null, trigger, finalState, structureTriggerC2S.tickRate()));
                        }
                    }
                }
            });
        });

        ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
            StructureTriggerManager manager = StructureTriggerManager.getServerState(serverWorld);
            manager.init(serverWorld);
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.getTickManager().shouldTick()) {
                StructureTriggerManager manager = StructureTriggerManager.getServerState(world);
                manager.tick(world);
            }
        });
    }
}
