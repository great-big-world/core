package dev.creoii.greatbigworld.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.util.GBWHeightmapTypes;
import net.minecraft.client.render.WeatherRendering;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WeatherRendering.class)
public class WeatherRenderingMixin {
    @WrapOperation(method = "buildPrecipitationPieces", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I"))
    private int creo$applyWeatherRenderIgnores(World instance, Heightmap.Type heightmap, int x, int z, Operation<Integer> original) {
        return original.call(instance, GBWHeightmapTypes.WEATHER, x, z);
    }
}
