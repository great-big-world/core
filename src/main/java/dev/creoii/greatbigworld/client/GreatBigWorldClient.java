package dev.creoii.greatbigworld.client;

import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class GreatBigWorldClient implements ClientModInitializer {
    @Nullable
    private static Identifier previousDimension;
    @Nullable
    private static Identifier toDimension;
    protected static Map<Knowledge.Type, Set<Knowledge>> knowledge;

    @Override
    public void onInitializeClient() {
        GBWClientNetworking.register();
        GBWClientEvents.register();
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
