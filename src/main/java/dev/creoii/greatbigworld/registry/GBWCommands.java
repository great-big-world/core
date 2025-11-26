package dev.creoii.greatbigworld.registry;

import com.google.common.collect.Sets;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.util.network.LearnKnowledgeS2C;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.registry.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public final class GBWCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            dispatcher.register(
                    CommandManager.literal("knowledge")
                            .then(
                                    CommandManager.argument("type", StringArgumentType.string())
                                            .suggests((commandContext, suggestionsBuilder) -> {
                                                for (Knowledge.Type type : Knowledge.Type.values()) {
                                                    suggestionsBuilder.suggest(type.name().toLowerCase());
                                                }
                                                return suggestionsBuilder.buildFuture();
                                            })
                                            .then(
                                                    CommandManager.literal("learn")
                                                            .then(
                                                                    CommandManager.argument("id", IdentifierArgumentType.identifier())
                                                                            .suggests((context, builder) -> {
                                                                                Knowledge.Type type = Knowledge.Type.valueOf(StringArgumentType.getString(context, "type").toUpperCase());
                                                                                return suggestTypeIds(context.getSource().getRegistryManager(), type, builder);
                                                                            })
                                                                            .executes(GBWCommands::executeLearn)
                                                            )
                                            )
                            )
            );
        });
    }

    private static CompletableFuture<Suggestions> suggestTypeIds(DynamicRegistryManager registryManager, Knowledge.Type type, SuggestionsBuilder builder) {
        Registry<?> registry = switch (type) {
            case ENCHANTMENT -> registryManager.getOrThrow(RegistryKeys.ENCHANTMENT);
            case ARMOR_TRIM -> registryManager.getOrThrow(RegistryKeys.TRIM_PATTERN);
            case POTTERY_SHERD -> registryManager.getOrThrow(RegistryKeys.DECORATED_POT_PATTERN);
            case BANNER_PATTERN -> registryManager.getOrThrow(RegistryKeys.BANNER_PATTERN);
        };

        registry.getIds().forEach(id -> builder.suggest(id.toString()));
        return builder.buildFuture();
    }

    private static int executeLearn(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        KnowledgeManager manager = KnowledgeManager.getServerState(source.getServer());

        Knowledge.Type type = Knowledge.Type.valueOf(StringArgumentType.getString(context, "type").toUpperCase());
        Identifier id = IdentifierArgumentType.getIdentifier(context, "id");

        Knowledge knowledge = new Knowledge(type, id);
        manager.learn(source.getPlayer(), knowledge);

        ServerPlayNetworking.send(context.getSource().getPlayer(), new LearnKnowledgeS2C(type, Sets.newHashSet(knowledge)));

        source.sendFeedback(() -> Text.literal("Learned " + id + " for type " + type.name()), false);
        return 1;
    }
}
