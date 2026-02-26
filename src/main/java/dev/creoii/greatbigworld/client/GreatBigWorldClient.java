package dev.creoii.greatbigworld.client;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.client.screen.StructureTriggerScreen;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.util.network.*;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GreatBigWorldClient implements ClientModInitializer {
    @Nullable
    private static Identifier previousDimension;
    @Nullable
    private static Identifier toDimension;
    private static Map<Knowledge.Type, Set<Knowledge>> knowledge;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_ID, (openStructureTriggerScreenS2C, context) -> {
            context.client().execute(() -> {
                BlockEntity blockEntity = context.player().level().getBlockEntity(openStructureTriggerScreenS2C.pos());
                if (blockEntity instanceof StructureTriggerBlockEntity structureTriggerBlockEntity) {
                    context.client().setScreen(new StructureTriggerScreen(structureTriggerBlockEntity));
                }
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(PreviousDimensionManager.PreviousDimensionS2C.PACKET_ID, (previousDimensionS2C, context) -> {
            context.client().execute(() -> {
                if (previousDimensionS2C.prev())
                    setPreviousDimension(previousDimensionS2C.id());
                else setToDimension(previousDimensionS2C.id());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(SyncWorldEventS2C.PACKET_ID, (syncWorldEventS2C, context) -> {
            context.client().execute(() -> {
                context.client().level.levelEvent(context.player(), syncWorldEventS2C.eventId(), syncWorldEventS2C.pos(), syncWorldEventS2C.data());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(SyncKnowledgeS2C.PACKET_ID, (syncKnowledgeS2C, context) -> {
            context.client().execute(() -> knowledge = syncKnowledgeS2C.knowledge());
        });
        ClientPlayNetworking.registerGlobalReceiver(LearnKnowledgeS2C.PACKET_ID, (learnKnowledgeS2C, context) -> {
            context.client().execute(() -> {
                if (knowledge == null)
                    return;

                learnKnowledgeS2C.knowledge().forEach(knowledge -> {
                    Knowledge.Type type = learnKnowledgeS2C.knowledgeType();
                    if (GreatBigWorldClient.knowledge.containsKey(type)) {
                        GreatBigWorldClient.knowledge.get(type).add(new Knowledge(type, knowledge.data()));
                    } else {
                        Set<Knowledge> newKnowledge = new HashSet<>();
                        newKnowledge.add(new Knowledge(type, knowledge.data()));
                        GreatBigWorldClient.knowledge.put(type, newKnowledge);
                    }

                    //System.out.println("Learned " + knowledge.data().toString() + " of type " + knowledge.type().name().toLowerCase());
                    context.client().player.displayClientMessage(Component.literal("Learned " + knowledge.data().toString() + " of type " + knowledge.type().name().toLowerCase()), true);
                });
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ScreenShakeS2C.PACKET_ID, (screenShakeS2C, context) -> {
            float intensity = screenShakeS2C.intensity();
            int duration = screenShakeS2C.duration();
            ScreenShakeManager.Easing easing = screenShakeS2C.easing();
            context.client().execute(() -> ScreenShakeManager.addShake(intensity, duration, easing));
        });

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (context.worldState() == null) // don't listen to Intellij, this can be null!
                return;

            ScreenShakeManager.tick();

            CameraRenderState cameraRenderState = context.worldState().cameraRenderState;
            cameraRenderState.pos = cameraRenderState.pos.add(ScreenShakeManager.getXOffset(), ScreenShakeManager.getYOffset(), 0);
        });

        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, client) -> {
            ClientPlayNetworking.send(RequestKnowledgeC2S.INSTANCE);
            knowledge = KnowledgeManager.createEmptyKnowledge();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((clientPacketListener, minecraft) -> {
            knowledge = KnowledgeManager.createEmptyKnowledge();
        });
    }

    public static @Nullable Identifier getPreviousDimension() {
        return previousDimension;
    }

    public static @Nullable Identifier getToDimension() {
        return toDimension;
    }

    public static void setPreviousDimension(@Nullable Identifier previousDimension) {
        GreatBigWorldClient.previousDimension = previousDimension;
    }

    public static void setToDimension(@Nullable Identifier toDimension) {
        GreatBigWorldClient.toDimension = toDimension;
    }

    public static Map<Knowledge.Type, Set<Knowledge>> getKnowledge() {
        return knowledge == null ? knowledge = KnowledgeManager.createEmptyKnowledge() : knowledge;
    }
}
