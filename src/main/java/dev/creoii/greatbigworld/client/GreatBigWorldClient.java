package dev.creoii.greatbigworld.client;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.client.screen.KnowledgeScreen;
import dev.creoii.greatbigworld.client.screen.StructureTriggerScreen;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.util.network.LearnKnowledgeS2C;
import dev.creoii.greatbigworld.util.network.RequestKnowledgeC2S;
import dev.creoii.greatbigworld.util.network.SyncKnowledgeS2C;
import dev.creoii.greatbigworld.util.network.SyncWorldEventS2C;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GreatBigWorldClient implements ClientModInitializer {
    @Nullable
    private static Identifier previousDimension;
    @Nullable
    private static Identifier toDimension;
    private static Map<Knowledge.Type, Set<Knowledge>> knowledge = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_ID, (openStructureTriggerScreenS2C, context) -> {
            context.client().execute(() -> {
                BlockEntity blockEntity = context.player().getEntityWorld().getBlockEntity(openStructureTriggerScreenS2C.pos());
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
                context.client().world.syncWorldEvent(context.player(), syncWorldEventS2C.eventId(), syncWorldEventS2C.pos(), syncWorldEventS2C.data());
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(SyncKnowledgeS2C.PACKET_ID, (syncKnowledgeS2C, context) -> {
            context.client().execute(() -> {
                knowledge = syncKnowledgeS2C.knowledge();
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(LearnKnowledgeS2C.PACKET_ID, (learnKnowledgeS2C, context) -> {
            context.client().execute(() -> {
                learnKnowledgeS2C.knowledge().forEach(knowledge -> {
                    Knowledge.Type type = learnKnowledgeS2C.type();
                    if (GreatBigWorldClient.knowledge.containsKey(type)) {
                        GreatBigWorldClient.knowledge.get(type).add(new Knowledge(type, knowledge.data()));
                    } else {
                        Set<Knowledge> newKnowledge = new HashSet<>();
                        newKnowledge.add(new Knowledge(type, knowledge.data()));
                        GreatBigWorldClient.knowledge.put(type, newKnowledge);
                    }

                    context.client().player.sendMessage(Text.literal("Learned " + knowledge.data().toString() + " of type " + knowledge.type().name().toLowerCase()), true);
                });
            });
        });

        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, client) -> {
            ClientPlayNetworking.send(RequestKnowledgeC2S.INSTANCE);
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
        return knowledge;
    }
}
