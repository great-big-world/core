package dev.creoii.greatbigworld.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;

public final class Codecs {
    public static final Codec<BlockPos> BLOCK_POS_STRING_CODEC = Codec.STRING.xmap(
        str -> {
            String[] parts = str.split(",");
            return new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }, pos -> pos.getX() + "," + pos.getY() + "," + pos.getZ()
    );
}
