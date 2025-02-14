package dev.creoii.greatbigworld.mixin.client;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Redirect(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I"))
    private int creo$applyWeatherRenderIgnores(World instance, Heightmap.Type heightmap, int x, int z) {
        return instance.getTopY(Heightmap.Type.valueOf("WEATHER"), x, z);
    }
}