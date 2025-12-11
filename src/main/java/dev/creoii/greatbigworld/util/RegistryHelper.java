package dev.creoii.greatbigworld.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Function;

public final class RegistryHelper {
    public static Block registerBlock(Identifier id, BlockBehaviour.Properties settings) {
        return registerBlock(id, Block::new, settings);
    }

    public static Block registerBlock(Identifier id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties settings) {
        return Registry.register(BuiltInRegistries.BLOCK, id, factory.apply(settings.setId(ResourceKey.create(Registries.BLOCK, id))));
    }

    public static Block registerStairs(Identifier id, BlockState base, BlockBehaviour.Properties settings) {
        return registerBlock(id, (settings1 -> new StairBlock(base, settings)), settings);
    }

    public static Block registerWall(Identifier id, BlockBehaviour.Properties settings) {
        return registerBlock(id, WallBlock::new, settings);
    }

    public static Item registerItem(Identifier id, Item.Properties settings) {
        return registerItem(id, Item::new, settings);
    }

    public static Item registerItem(Identifier id) {
        return registerItem(id, Item::new);
    }

    public static Item registerItem(Identifier id, Function<Item.Properties, Item> factory, Item.Properties settings) {
        return Registry.register(BuiltInRegistries.ITEM, id, factory.apply(settings.setId(ResourceKey.create(Registries.ITEM, id))));
    }

    public static Item registerItem(Identifier id, Function<Item.Properties, Item> factory) {
        return Registry.register(BuiltInRegistries.ITEM, id, factory.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    }

    public static Item registerBlockItem(Identifier id, Block block, Item.Properties settings) {
        return registerItem(id, settings1 -> new BlockItem(block, settings1), settings.setId(ResourceKey.create(Registries.ITEM, id)).useBlockDescriptionPrefix());
    }

    public static Item registerBlockItem(Identifier id, Block block) {
        return registerItem(id, settings1 -> new BlockItem(block, settings1.setId(ResourceKey.create(Registries.ITEM, id)).useBlockDescriptionPrefix()));
    }
}
