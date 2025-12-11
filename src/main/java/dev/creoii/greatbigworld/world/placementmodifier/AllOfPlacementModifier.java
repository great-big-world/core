package dev.creoii.greatbigworld.world.placementmodifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.registry.GBWPlacementModifierTypes;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class AllOfPlacementModifier extends PlacementFilter {
    public static final MapCodec<AllOfPlacementModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(PlacementModifier.CODEC.listOf().fieldOf("placements").forGetter(predicate -> {
            return predicate.placements;
        })).apply(instance, AllOfPlacementModifier::new);
    });
    private final List<PlacementModifier> placements;

    public AllOfPlacementModifier(List<PlacementModifier> placements) {
        this.placements = placements;
        if (placements.size() == 1) {
            GreatBigWorld.LOGGER.error("Instance of {} contains 1 placement entry. This is redundant.", BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.getKey(type()));
        }
    }

    @Override
    public PlacementModifierType<?> type() {
        return GBWPlacementModifierTypes.ALL_OF;
    }

    @Override
    public boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        for (PlacementModifier modifier : placements) {
            if (modifier instanceof PlacementFilter conditional && !conditional.shouldPlace(context, random, pos)) {
                return false;
            }
        }
        return true;
    }
}
