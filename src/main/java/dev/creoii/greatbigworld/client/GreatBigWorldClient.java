package dev.creoii.greatbigworld.client;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.client.screen.StructureTriggerScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;

public class GreatBigWorldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StructureTriggerBlock.OpenStructureTriggerScreenS2C.PACKET_ID, (openStructureTriggerScreenS2C, context) -> {
            context.client().execute(() -> {
                BlockEntity blockEntity = context.player().clientWorld.getBlockEntity(openStructureTriggerScreenS2C.pos());
                if (blockEntity instanceof StructureTriggerBlockEntity structureTriggerBlockEntity) {
                    context.client().setScreen(new StructureTriggerScreen(structureTriggerBlockEntity));
                }
            });
        });
    }
}
