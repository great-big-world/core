package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.densityfunction.FastNoiseDensityFunction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class GBWDensityFunctionTypes {
    public static void register() {
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "fast_noise"), FastNoiseDensityFunction.CODEC);
    }
}
