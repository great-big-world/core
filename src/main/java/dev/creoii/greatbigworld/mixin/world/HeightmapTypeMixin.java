package dev.creoii.greatbigworld.mixin.world;

import dev.creoii.greatbigworld.tag.GBWBlockTags;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

@Mixin(Heightmap.Types.class)
public class HeightmapTypeMixin {
    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static Heightmap.Types create(String internalName, int internalId, final int index, final String id, final Heightmap.Usage purpose, final Predicate<BlockState> blockPredicate) {
        throw new AssertionError();
    }

    @Shadow @Final @Mutable private static Heightmap.Types[] $VALUES;

    @SuppressWarnings("deprecation")
    @Inject(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, target = "Lnet/minecraft/world/level/levelgen/Heightmap$Types;$VALUES:[Lnet/minecraft/world/level/levelgen/Heightmap$Types;", shift = At.Shift.AFTER))
    private static void gbw$addWeatherHeightmap(CallbackInfo ci) {
        ArrayList<Heightmap.Types> types = new ArrayList<>(Arrays.asList($VALUES));
        Heightmap.Types last = types.getLast();

        Heightmap.Types weather = create("WEATHER", last.ordinal() + 1, 6, "WEATHER", Heightmap.Usage.CLIENT, state -> {
            if (state.is(GBWBlockTags.PRECIPITATION_IGNORES))
                return false;
            else return state.blocksMotion() || !state.getFluidState().isEmpty();
        });
        types.add(weather);

        $VALUES = types.toArray(new Heightmap.Types[0]);
    }
}
