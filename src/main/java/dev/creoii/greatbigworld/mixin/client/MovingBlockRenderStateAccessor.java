package dev.creoii.greatbigworld.mixin.client;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.state.BlockBreakingRenderState;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MovingBlockRenderState.class)
public interface MovingBlockRenderStateAccessor {
    @Accessor("blockState")
    void setBlockState(BlockState blockState);
}
