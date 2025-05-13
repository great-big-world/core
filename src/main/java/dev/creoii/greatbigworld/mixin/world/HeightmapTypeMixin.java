package dev.creoii.greatbigworld.mixin.world;

import dev.creoii.greatbigworld.tag.GBWBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.world.Heightmap;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

@Mixin(Heightmap.Type.class)
public class HeightmapTypeMixin {
    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static Heightmap.Type create(String internalName, int internalId, final int index, final String id, final Heightmap.Purpose purpose, final Predicate<BlockState> blockPredicate) {
        throw new AssertionError();
    }

    @Shadow @Final @Mutable private static Heightmap.Type[] field_13199;

    @SuppressWarnings("deprecation")
    @Inject(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, target = "Lnet/minecraft/world/Heightmap$Type;field_13199:[Lnet/minecraft/world/Heightmap$Type;", shift = At.Shift.AFTER))
    private static void gbw$addWeatherHeightmap(CallbackInfo ci) {
        ArrayList<Heightmap.Type> types = new ArrayList<>(Arrays.asList(field_13199));
        Heightmap.Type last = types.getLast();

        Heightmap.Type weather = create("WEATHER", last.ordinal() + 1, 6, "WEATHER", Heightmap.Purpose.CLIENT, state -> {
            if (state.isIn(GBWBlockTags.PRECIPITATION_IGNORES))
                return false;
            else return state.blocksMovement() || !state.getFluidState().isEmpty();
        });
        types.add(weather);

        field_13199 = types.toArray(new Heightmap.Type[0]);
    }
}
