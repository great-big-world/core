package dev.creoii.greatbigworld.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ItemEvents {
    public static final Event<Pickup> PICKUP = EventFactory.createArrayBacked(Pickup.class, (callbacks) -> (playerEntity, itemEntity) -> {
        for (Pickup callback : callbacks) {
            callback.pickup(playerEntity, itemEntity);
        }
    });

    @FunctionalInterface
    public interface Pickup {
        void pickup(PlayerEntity player, ItemEntity itemEntity);
    }
}
