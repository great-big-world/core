package dev.creoii.greatbigworld.command;

import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.knowledge.KnowledgeManager;
import dev.creoii.greatbigworld.util.network.LearnKnowledgeS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class KnowledgeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("knowledge")
                        .then(Commands.literal("learn")
                                        .then(Commands.argument("type", StringArgumentType.string())
                                                        .suggests((commandContext, suggestionsBuilder) -> {
                                                            for (Knowledge.Type type : Knowledge.Type.values()) {
                                                                suggestionsBuilder.suggest(type.name().toLowerCase());
                                                            }
                                                            return suggestionsBuilder.buildFuture();
                                                        })
                                                        .then(Commands.argument("id", IdentifierArgument.id())
                                                                        .suggests((context, builder) -> {
                                                                            Knowledge.Type type = Knowledge.Type.valueOf(StringArgumentType.getString(context, "type").toUpperCase());
                                                                            return suggestTypeIds(context.getSource().registryAccess(), type, builder);
                                                                        })
                                                                        .executes(KnowledgeCommand::executeLearn)))));
    }

    private static CompletableFuture<Suggestions> suggestTypeIds(RegistryAccess registryManager, Knowledge.Type type, SuggestionsBuilder builder) {
        Registry<?> registry = switch (type) {
            case ENCHANTMENT -> registryManager.lookupOrThrow(Registries.ENCHANTMENT);
            case ARMOR_TRIM -> registryManager.lookupOrThrow(Registries.TRIM_PATTERN);
            case POTTERY_SHERD -> registryManager.lookupOrThrow(Registries.DECORATED_POT_PATTERN);
            case BANNER_PATTERN -> registryManager.lookupOrThrow(Registries.BANNER_PATTERN);
        };

        registry.keySet().forEach(id -> builder.suggest(id.toString()));
        return builder.buildFuture();
    }

    private static int executeLearn(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        KnowledgeManager manager = KnowledgeManager.getServerState(source.getServer());

        Knowledge.Type type = Knowledge.Type.valueOf(StringArgumentType.getString(context, "type").toUpperCase());
        Identifier id = IdentifierArgument.getId(context, "id");

        Knowledge knowledge = new Knowledge(type, id);
        if (manager.learn(source.getPlayer(), knowledge)) {
            ServerPlayNetworking.send(source.getPlayer(), new LearnKnowledgeS2C(type, Sets.newHashSet(knowledge)));
            source.sendSuccess(() -> Component.translatable("commands.knowledge.success", id.toString(), type.name()), false);
            return 1;
        }
        source.sendFailure(Component.translatable("commands.knowledge.fail", id.toString(), type.name()));
        return 0;
    }
}
