package dev.creoii.greatbigworld.client;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.client.screen.StructureTriggerScreen;
import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class GreatBigWorldClient implements ClientModInitializer {
    @Nullable
    private static Identifier previousDimension;
    @Nullable
    private static Identifier toDimension;

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
    }

    public static @Nullable Identifier getPreviousDimension() {
        return previousDimension;
    }

    public static @Nullable Identifier getToDimension() {
        return toDimension;
    }

    public static void setPreviousDimension(@Nullable Identifier previousDimension) {
        GreatBigWorldClient.previousDimension = previousDimension;
        System.out.println("setPrevDimension: " + previousDimension.toString());
    }

    public static void setToDimension(@Nullable Identifier toDimension) {
        GreatBigWorldClient.toDimension = toDimension;
        System.out.println("setToDimension: " + toDimension.toString());
    }
}
