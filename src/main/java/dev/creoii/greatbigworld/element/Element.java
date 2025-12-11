package dev.creoii.greatbigworld.element;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum Element {
    EARTH(Component.literal("\uD83D\uDF03").withStyle(ChatFormatting.DARK_GREEN), 1),
    FIRE(Component.literal("\uD83D\uDF02").withStyle(ChatFormatting.RED), 0),
    WATER(Component.literal("\uD83D\uDF04").withStyle(ChatFormatting.DARK_BLUE), 3),
    ICE(Component.literal("\uD83D\uDF60").withStyle(ChatFormatting.BLUE), 2),
    AIR(Component.literal("\uD83D\uDF01").withStyle(ChatFormatting.LIGHT_PURPLE), 5),
    LIGHTNING(Component.literal("\uD83D\uDF5F").withStyle(ChatFormatting.YELLOW), 4),
    LIGHT(Component.literal("\uD83D\uDF1A").withStyle(ChatFormatting.WHITE)),
    DARK(Component.literal("\uD83D\uDF1B").withStyle(ChatFormatting.BLACK)),
    VOID(Component.literal("\uD83D\uDF61").withStyle(ChatFormatting.DARK_PURPLE)),
    AETHER(Component.literal("\uD83D\uDF63").withStyle(ChatFormatting.GOLD));

    private final Component symbol;
    private final int synergy;

    Element(Component symbol, int synergy) {
        this.symbol = symbol;
        this.synergy = synergy < 0 ? -1 : synergy;
    }

    Element(Component symbol) {
        this(symbol, -1);
    }

    public Component getSymbol() {
        return symbol;
    }

    public Element getSynergy() {
        return Element.values()[synergy];
    }
}
