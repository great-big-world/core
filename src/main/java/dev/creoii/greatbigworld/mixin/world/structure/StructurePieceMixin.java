package dev.creoii.greatbigworld.mixin.world.structure;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.creoii.greatbigworld.block.PlaceableByStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructurePiece.class)
public class StructurePieceMixin {
    @WrapOperation(method = "placeBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean gbw$callPlaceableByStructureBlocks(WorldGenLevel instance, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
        if (state.getBlock() instanceof PlaceableByStructure placeableByStructure) {
            placeableByStructure.onPlaceByStructure(instance, state, pos);
        }
        return original.call(instance, pos, state, i);
    }
}
