package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.world.dimension.PreviousDimensionManager;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.world.level.Level;

public final class GBWEvents {
    public static void register() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.dimensionTypeRegistration() == Level.OVERWORLD) {
                PreviousDimensionManager manager = PreviousDimensionManager.getServerState(server);
                manager.init(server);
            }
        });

        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (world.tickRateManager().runsNormally()) {
                StructureTriggerManager manager = StructureTriggerManager.getServerState(world);
                manager.tick(world);
            }
        });
    }
}
