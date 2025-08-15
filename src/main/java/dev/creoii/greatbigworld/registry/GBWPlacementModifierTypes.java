package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.placementmodifier.AllOfPlacementModifier;
import dev.creoii.greatbigworld.world.placementmodifier.AnyOfPlacementModifier;
import dev.creoii.greatbigworld.world.placementmodifier.FastNoisePlacementModifier;
import dev.creoii.greatbigworld.world.placementmodifier.NoisePlacementModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;

public class GBWPlacementModifierTypes {
    public static final PlacementModifierType<NoisePlacementModifier> NOISE = () -> NoisePlacementModifier.CODEC;
    public static final PlacementModifierType<FastNoisePlacementModifier> FAST_NOISE = () -> FastNoisePlacementModifier.CODEC;
    public static final PlacementModifierType<AnyOfPlacementModifier> ANY_OF = () -> AnyOfPlacementModifier.CODEC;
    public static final PlacementModifierType<AllOfPlacementModifier> ALL_OF = () -> AllOfPlacementModifier.CODEC;

    public static void register() {
        Registry.register(Registries.PLACEMENT_MODIFIER_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "noise"), NOISE);
        Registry.register(Registries.PLACEMENT_MODIFIER_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "fast_noise"), FAST_NOISE);
        Registry.register(Registries.PLACEMENT_MODIFIER_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "any_of"), ANY_OF);
        Registry.register(Registries.PLACEMENT_MODIFIER_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "all_of"), ALL_OF);
    }
}
