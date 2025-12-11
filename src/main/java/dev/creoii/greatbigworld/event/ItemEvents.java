package dev.creoii.greatbigworld.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public class ItemEvents {
    public static final Event<Pickup> PICKUP = EventFactory.createArrayBacked(Pickup.class, (callbacks) -> (playerEntity, itemEntity) -> {
        for (Pickup callback : callbacks) {
            callback.pickup(playerEntity, itemEntity);
        }
    });

    @FunctionalInterface
    public interface Pickup {
        void pickup(Player player, ItemEntity itemEntity);
    }
}
