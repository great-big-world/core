package dev.creoii.greatbigworld.element;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum Element {
    EARTH(Text.literal("\uD83D\uDF03").formatted(Formatting.DARK_GREEN), 1),
    FIRE(Text.literal("\uD83D\uDF02").formatted(Formatting.RED), 0),
    WATER(Text.literal("\uD83D\uDF04").formatted(Formatting.DARK_BLUE), 3),
    ICE(Text.literal("\uD83D\uDF60").formatted(Formatting.BLUE), 2),
    AIR(Text.literal("\uD83D\uDF01").formatted(Formatting.LIGHT_PURPLE), 5),
    LIGHTNING(Text.literal("\uD83D\uDF5F").formatted(Formatting.YELLOW), 4),
    LIGHT(Text.literal("\uD83D\uDF1A").formatted(Formatting.WHITE)),
    DARK(Text.literal("\uD83D\uDF1B").formatted(Formatting.BLACK)),
    VOID(Text.literal("\uD83D\uDF61").formatted(Formatting.DARK_PURPLE)),
    AETHER(Text.literal("\uD83D\uDF63").formatted(Formatting.GOLD));

    private final Text symbol;
    private final int synergy;

    Element(Text symbol, int synergy) {
        this.symbol = symbol;
        this.synergy = synergy < 0 ? -1 : synergy;
    }

    Element(Text symbol) {
        this(symbol, -1);
    }

    public Text getSymbol() {
        return symbol;
    }

    public Element getSynergy() {
        return Element.values()[synergy];
    }
}
