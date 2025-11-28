package dev.creoii.greatbigworld.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

public class GameRuleEvents {
    public static final Event<Set> SET = EventFactory.createArrayBacked(Set.class, (callbacks) -> (key, world) -> {
        for (Set callback : callbacks) {
            callback.set(key, world);
        }
    });

    @FunctionalInterface
    public interface Set {
        void set(GameRules.Key<?> key, ServerWorld world);
    }
}
