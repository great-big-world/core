package dev.creoii.greatbigworld;

import dev.creoii.greatbigworld.registry.GBWTreeDecoratorTypes;
import dev.creoii.greatbigworld.registry.GBWPlacementModifiers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GreatBigWorld implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final Logger LOGGER = LogManager.getLogger(GreatBigWorld.class);

    public static final RegistryKey<World> ALTERWORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(GreatBigWorld.NAMESPACE, "the_alterworld"));;

    @Override
    public void onInitialize() {
        GBWTreeDecoratorTypes.register();
        GBWPlacementModifiers.register();
    }
}
