package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.densityfunction.FastNoiseDensityFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class GBWDensityFunctionTypes {
    public static void register() {
        Registry.register(BuiltInRegistries.DENSITY_FUNCTION_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "fast_noise"), FastNoiseDensityFunction.CODEC);
    }
}
