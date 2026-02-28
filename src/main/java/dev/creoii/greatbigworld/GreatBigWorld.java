package dev.creoii.greatbigworld;

import dev.creoii.greatbigworld.block.KnowledgeBlock;
import dev.creoii.greatbigworld.registry.*;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GreatBigWorld implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final Logger LOGGER = LogManager.getLogger(GreatBigWorld.class);

    public static final ResourceKey<Level> ALTERWORLD_KEY = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(NAMESPACE, "the_alterworld"));
    public static final ResourceKey<LevelStem> ALTERWORLD_OPTIONS = ResourceKey.create(Registries.LEVEL_STEM, Identifier.fromNamespaceAndPath(NAMESPACE, "the_alterworld"));

    @Override
    public void onInitialize() {
        GBWRegistries.register();
        GBWBlocks.register();
        GBWItems.register();
        GBWBlockEntityTypes.register();
        GBWFeatures.register();
        GBWTreeDecoratorTypes.register();
        GBWPlacementModifierTypes.register();
        GBWDensityFunctionTypes.register();
        GBWCommands.register();
        StructureTriggerDataType.register();
        GBWStructureTriggers.register();
        GBWDataComponentTypes.register();
        GBWNetworking.register();
        GBWEvents.register();

        BuiltInRegistries.BLOCK.forEach(block -> {
            if (block instanceof KnowledgeBlock) {
                ((BlockEntityTypeAccessor) GBWBlockEntityTypes.KNOWLEDGE_BLOCK).getBlocks().add(block);
            }
        });

        ComponentTooltipAppenderRegistry.addBefore(DataComponents.MAP_ID, GBWDataComponentTypes.KNOWLEDGE);
    }
}
