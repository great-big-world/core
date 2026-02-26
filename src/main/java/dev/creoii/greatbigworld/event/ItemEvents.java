package dev.creoii.greatbigworld.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemEvents {
    public static final Event<Pickup> PICKUP = EventFactory.createArrayBacked(Pickup.class, (callbacks) -> (playerEntity, itemEntity) -> {
        for (Pickup callback : callbacks) {
            callback.pickup(playerEntity, itemEntity);
        }
    });

    public static final Event<Brush> BRUSH = EventFactory.createArrayBacked(Brush.class, (callbacks) -> (level, player, stack, state, pos,  brushDuration) -> {
        for (Brush callback : callbacks) {
            callback.brush(level, player, stack, state, pos,  brushDuration);
        }
    });

    @FunctionalInterface
    public interface Pickup {
        void pickup(Player player, ItemEntity itemEntity);
    }

    @FunctionalInterface
    public interface Brush {
        void brush(Level level, Player player, ItemStack stack, BlockState state, BlockPos pos, int brushDuration);
    }
}
