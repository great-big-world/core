package dev.creoii.greatbigworld.element;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum Element {
    EARTH(Text.literal("\uD83D\uDF03").formatted(Formatting.DARK_GREEN)),
    FIRE(Text.literal("\uD83D\uDF02").formatted(Formatting.RED)),
    WATER(Text.literal("\uD83D\uDF04").formatted(Formatting.DARK_BLUE)),
    ICE(Text.literal("\uD83D\uDF60").formatted(Formatting.BLUE)),
    AIR(Text.literal("\uD83D\uDF01").formatted(Formatting.LIGHT_PURPLE)),
    LIGHTNING(Text.literal("\uD83D\uDF5F").formatted(Formatting.YELLOW)),
    LIGHT(Text.literal("\uD83D\uDF1A").formatted(Formatting.WHITE)),
    DARK(Text.literal("\uD83D\uDF1B").formatted(Formatting.BLACK)),
    VOID(Text.literal("\uD83D\uDF61").formatted(Formatting.DARK_PURPLE)),
    AETHER(Text.literal("\uD83D\uDF63").formatted(Formatting.GOLD));

    private final Text symbol;

    Element(Text symbol) {
        this.symbol = symbol;
    }

    public Text getSymbol() {
        return symbol;
    }
}
