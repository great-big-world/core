package dev.creoii.greatbigworld.element;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.resources.Identifier;

public enum Element {
    EARTH(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/earth"))), 1),
    FIRE(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/fire"))), 0),
    WATER(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/water"))), 3),
    ICE(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/ice"))), 2),
    AIR(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/air"))), 5),
    LIGHTNING(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/lightning"))), 4),
    LIGHT(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/light")))),
    DARK(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/dark")))),
    VOID(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/void")))),
    AETHER(Component.object(new AtlasSprite(AtlasIds.GUI, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "element/aether"))));

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
