package dev.creoii.greatbigworld.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.util.SurfaceSystemRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SurfaceSystem.class)
public class SurfaceSystemMixin {
    @Inject(method = "buildSurface", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;getHeight(Lnet/minecraft/world/level/levelgen/Heightmap$Types;II)I", ordinal = 1))
    private void gbw$injectCustomSurfaceSystems(RandomState randomState, BiomeManager biomeManager, Registry<Biome> registry, boolean bl, WorldGenerationContext worldGenerationContext, ChunkAccess chunkAccess, NoiseChunk noiseChunk, SurfaceRules.RuleSource ruleSource, CallbackInfo ci, @Local BlockColumn blockColumn, @Local Holder<Biome> holder, @Local(ordinal = 4) int m, @Local(ordinal = 5) int n, @Local(ordinal = 6) int o) {
        SurfaceSystemRegistry.EXTENSIONS.forEach((predicate, extension) -> {
            if (predicate.test(holder)) {
                extension.doExtension(holder.value(), blockColumn, m, n, o, chunkAccess, noiseChunk);
            }
        });
    }
}
