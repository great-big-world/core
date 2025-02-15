package dev.creoii.greatbigworld.util;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class RegistryHelper {
    public static Block registerBlock(Identifier id, AbstractBlock.Settings settings) {
        return registerBlock(id, Block::new, settings);
    }

    public static Block registerBlock(Identifier id, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
        return Registry.register(Registries.BLOCK, id, factory.apply(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, id))));
    }

    public static Block registerSlab(Identifier id, AbstractBlock.Settings settings) {
        return registerBlock(id, SlabBlock::new, settings);
    }

    public static Block registerStairs(Identifier id, BlockState base, AbstractBlock.Settings settings) {
        return registerBlock(id, (settings1 -> new StairsBlock(base, settings)), settings);
    }

    public static Block registerWall(Identifier id, AbstractBlock.Settings settings) {
        return registerBlock(id, WallBlock::new, settings);
    }

    public static Item registerItem(Identifier id, Item.Settings settings) {
        return registerItem(id, Item::new, settings);
    }

    public static Item registerItem(Identifier id) {
        return registerItem(id, Item::new);
    }

    public static Item registerItem(Identifier id, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return Registry.register(Registries.ITEM, id, factory.apply(settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, id))));
    }

    public static Item registerItem(Identifier id, Function<Item.Settings, Item> factory) {
        return Registry.register(Registries.ITEM, id, factory.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id))));
    }

    public static Item registerBlockItem(Identifier id, Block block, Item.Settings settings) {
        return registerItem(id, settings1 -> new BlockItem(block, settings1), settings);
    }

    public static Item registerBlockItem(Identifier id, Block block) {
        return registerItem(id, settings1 -> new BlockItem(block, settings1));
    }
}
