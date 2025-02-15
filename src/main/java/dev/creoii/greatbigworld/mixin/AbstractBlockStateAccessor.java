package dev.creoii.greatbigworld.mixin;

import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractBlock.AbstractBlockState.class)
public interface AbstractBlockStateAccessor {
    @Accessor
    int getLuminance();

    @Accessor
    float getHardness();

    @Accessor("luminance")
    void setLuminance(int luminance);

    @Accessor("hardness")
    void setHardness(float hardness);
}
