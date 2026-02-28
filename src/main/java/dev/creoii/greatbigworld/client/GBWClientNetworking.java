package dev.creoii.greatbigworld.client;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.client.screen.StructureTriggerScreen;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.util.network.LearnKnowledgeS2C;
import dev.creoii.greatbigworld.util.network.ScreenShakeS2C;
import dev.creoii.greatbigworld.util.network.SyncKnowledgeS2C;
import dev.creoii.greatbigworld.util.network.SyncWorldEventS2C;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public final class GBWClientNetworking {
    public static void register() {
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
                    GreatBigWorldClient.setPreviousDimension(previousDimensionS2C.id());
                else GreatBigWorldClient.setToDimension(previousDimensionS2C.id());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(SyncWorldEventS2C.PACKET_ID, (syncWorldEventS2C, context) -> {
            context.client().execute(() -> {
                context.client().level.levelEvent(context.player(), syncWorldEventS2C.eventId(), syncWorldEventS2C.pos(), syncWorldEventS2C.data());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(SyncKnowledgeS2C.PACKET_ID, (syncKnowledgeS2C, context) -> {
            context.client().execute(() -> GreatBigWorldClient.knowledge = syncKnowledgeS2C.knowledge());
        });
        ClientPlayNetworking.registerGlobalReceiver(LearnKnowledgeS2C.PACKET_ID, (learnKnowledgeS2C, context) -> {
            context.client().execute(() -> {
                if (GreatBigWorldClient.knowledge == null)
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
    }
}
