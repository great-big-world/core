package dev.creoii.greatbigworld.util;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseChunk;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class SurfaceSystemRegistry {
    public static final Map<Predicate<Holder<Biome>>, Extension> EXTENSIONS = new HashMap<>();

    public static void register(Predicate<Holder<Biome>> predicate, Extension extension) {
        EXTENSIONS.put(predicate, extension);
    }

    @FunctionalInterface
    public interface Extension {
        void doExtension(Biome biome, BlockColumn blockColumn, int x, int y, int z, ChunkAccess chunkAccess, NoiseChunk noiseChunk);
    }
}
