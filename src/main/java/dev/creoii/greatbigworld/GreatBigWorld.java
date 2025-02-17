package dev.creoii.greatbigworld;

import dev.creoii.greatbigworld.worldgen.GBWPlacementModifiers;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GreatBigWorld implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final Logger LOGGER = LogManager.getLogger(GreatBigWorld.class);

    @Override
    public void onInitialize() {
        GBWPlacementModifiers.register();
    }
}
