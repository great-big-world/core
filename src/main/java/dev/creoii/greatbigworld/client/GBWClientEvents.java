package dev.creoii.greatbigworld.client;

import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.util.network.RequestKnowledgeC2S;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.renderer.state.CameraRenderState;

@Environment(EnvType.CLIENT)
public final class GBWClientEvents {
    public static void register() {
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> {
            if (context.worldState() == null) // don't listen to Intellij, this can be null!
                return;

            ScreenShakeManager.tick();

            CameraRenderState cameraRenderState = context.worldState().cameraRenderState;
            cameraRenderState.pos = cameraRenderState.pos.add(ScreenShakeManager.getXOffset(), ScreenShakeManager.getYOffset(), 0);
        });

        ClientPlayConnectionEvents.JOIN.register((clientPlayNetworkHandler, packetSender, client) -> {
            ClientPlayNetworking.send(RequestKnowledgeC2S.INSTANCE);
            GreatBigWorldClient.knowledge = KnowledgeManager.createEmptyKnowledge();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((clientPacketListener, minecraft) -> {
            GreatBigWorldClient.knowledge = KnowledgeManager.createEmptyKnowledge();
        });
    }
}
