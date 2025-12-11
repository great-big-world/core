package dev.creoii.greatbigworld.world.feature;

import com.mojang.serialization.Codec;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.List;
import java.util.function.BiPredicate;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class CompositeFeature extends Feature<CompositeFeatureConfig> {
    public CompositeFeature(Codec<CompositeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext context) {
        CompositeFeatureConfig config = (CompositeFeatureConfig) context.config();
        return config.type().test(context, config.features());
    }

    public enum Type implements StringRepresentable {
        SOFT("soft", (context, entries) -> {
            MutableBoolean fail = new MutableBoolean(false);
            for (Holder<PlacedFeature> entry : entries) {
                if (entry.value().place(context.level(), context.chunkGenerator(), context.random(), context.origin()))
                    fail.setTrue();
            }
            return fail.booleanValue();
        }),
        HARD("hard", (context, entries) -> {
            for (Holder<PlacedFeature> entry : entries) {
                if (!entry.value().place(context.level(), context.chunkGenerator(), context.random(), context.origin()))
                    return false;
            }
            return true;
        }),
        FREE("free", (context, entries) -> {
            for (Holder<PlacedFeature> entry : entries) {
                entry.value().place(context.level(), context.chunkGenerator(), context.random(), context.origin());
            }
            return true;
        });

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private final String id;
        private final BiPredicate<FeaturePlaceContext<?>, List<Holder<PlacedFeature>>> typePredicate;

        Type(String id, BiPredicate<FeaturePlaceContext<?>, List<Holder<PlacedFeature>>> typePredicate) {
            this.id = id;
            this.typePredicate = typePredicate;
        }

        public boolean test(FeaturePlaceContext<?> context, List<Holder<PlacedFeature>> entries) {
            return typePredicate.test(context, entries);
        }

        @Override
        public String getSerializedName() {
            return id;
        }
    }
}
