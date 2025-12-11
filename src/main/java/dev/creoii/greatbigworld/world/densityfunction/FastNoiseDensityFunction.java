package dev.creoii.greatbigworld.world.densityfunction;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.world.fastnoise.FastNoiseLite;
import dev.creoii.greatbigworld.world.fastnoise.FastNoiseParameters;
import net.minecraft.core.Holder;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public class FastNoiseDensityFunction implements DensityFunction {
    public static final MapCodec<FastNoiseDensityFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(
                FastNoiseParameters.REGISTRY_ENTRY_CODEC.fieldOf("noise").forGetter(predicate -> {
                    return predicate.noise;
                }),
                Codec.DOUBLE.fieldOf("xz_scale").forGetter(predicate -> {
                    return predicate.xzScale;
                }),
                Codec.DOUBLE.fieldOf("y_scale").forGetter(predicate -> {
                    return predicate.yScale;
                })
        ).apply(instance, FastNoiseDensityFunction::new);
    });
    public static final KeyDispatchDataCodec<FastNoiseDensityFunction> CODEC_HOLDER = KeyDispatchDataCodec.of(CODEC);
    private final Holder<FastNoiseParameters> noise;
    private final double xzScale;
    private final double yScale;

    public FastNoiseDensityFunction(Holder<FastNoiseParameters> noise, double xzScale, double yScale) {
        this.noise = noise;
        this.xzScale = xzScale;
        this.yScale = yScale;
    }

    @Override
    public double compute(FunctionContext pos) {
        FastNoiseLite fastNoiseLite = new FastNoiseLite(noise.value());
        return fastNoiseLite.getNoise((float) (pos.blockX() * xzScale), (float) (pos.blockY() * yScale), (float) (pos.blockZ() * xzScale));
    }

    @Override
    public void fillArray(double[] densities, ContextProvider applier) {
        applier.fillAllDirectly(densities, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(new FastNoiseDensityFunction(noise, xzScale, yScale));
    }

    @Override
    public double minValue() {
        return -2d;
    }

    @Override
    public double maxValue() {
        return 2d;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC_HOLDER;
    }
}