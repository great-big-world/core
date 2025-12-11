package dev.creoii.greatbigworld.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockBehaviour.BlockStateBase.class)
public interface AbstractBlockStateAccessor {
    @Accessor("lightEmission")
    void setLightEmission(int lightEmission);

    @Accessor("destroySpeed")
    void setDestroySpeed(float destroySpeed);
}
