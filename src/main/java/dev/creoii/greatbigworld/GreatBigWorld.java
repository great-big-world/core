package dev.creoii.greatbigworld;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.creoii.greatbigworld.block.KnowledgeBlock;
import dev.creoii.greatbigworld.registry.*;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.dimension.LevelStem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

public class GreatBigWorld implements ModInitializer {
    public static final String NAMESPACE = "great_big_world";
    public static final Logger LOGGER = LogManager.getLogger(GreatBigWorld.class);
    private static final boolean DEBUG_OVERWORLD_BIOME_PARAMETERS = false;

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

        if (DEBUG_OVERWORLD_BIOME_PARAMETERS) {
            ServerWorldEvents.LOAD.register((minecraftServer, serverLevel) -> {
                if (serverLevel.dimension() == Level.OVERWORLD) {
                    ImmutableList.Builder<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> builder = ImmutableList.builder();
                    new OverworldBiomeBuilder().addBiomes(pair -> builder.add(pair.mapSecond(Function.identity())));
                    Climate.ParameterList<ResourceKey<Biome>> parameterList = new Climate.ParameterList<>(builder.build());

                    DataResult<JsonElement> result = Climate.ParameterList.codec(ResourceKey.codec(Registries.BIOME).fieldOf("biome")).encodeStart(JsonOps.INSTANCE, parameterList);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    try (FileWriter writer = new FileWriter("biome_parameters.json")) {
                        gson.toJson(result.getOrThrow(), writer);
                        System.out.println("outputted biome params");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
