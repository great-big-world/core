package dev.creoii.greatbigworld;

import dev.creoii.greatbigworld.item.UseThroughBlock;
import dev.creoii.greatbigworld.worldgen.GBWPlacementModifiers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GreatBigWorld implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final Logger LOGGER = LogManager.getLogger(GreatBigWorld.class);

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(UseThroughBlock.AttackThroughBlock.PACKET_ID, UseThroughBlock.AttackThroughBlock.PACKET_CODEC);

        GBWPlacementModifiers.register();

        ServerPlayNetworking.registerGlobalReceiver(UseThroughBlock.AttackThroughBlock.PACKET_ID, (payload, context) -> {
            int entityId = payload.entityId();
            context.server().execute(() -> {
                ServerPlayerEntity serverPlayer = context.player();
                if (serverPlayer.getServer() != null) {
                    ItemStack stack = serverPlayer.getStackInHand(serverPlayer.getActiveHand());
                    if (stack.getItem() instanceof UseThroughBlock useThroughBlock) {
                        Entity entity = serverPlayer.getWorld().getEntityById(entityId);
                        if (entity != null && useThroughBlock.canAttackThroughBlock(serverPlayer, stack, entity)) {
                            serverPlayer.attack(entity);
                            useThroughBlock.onAttackThroughBlock(serverPlayer, stack, entity);
                        }
                    }
                }
            });
        });
    }
}
