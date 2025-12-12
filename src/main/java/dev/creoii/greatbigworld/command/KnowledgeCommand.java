package dev.creoii.greatbigworld.command;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class KnowledgeCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("knowledge")
                        .then(Commands.literal("learn")
                                .then(Commands.argument("type", StringArgumentType.string())
                                        .suggests((commandContext, suggestionsBuilder) -> {
                                            //suggestionsBuilder.suggest("*");
                                            for (Knowledge.Type type : Knowledge.Type.values()) {
                                                suggestionsBuilder.suggest(type.name().toLowerCase());
                                            }
                                            return suggestionsBuilder.buildFuture();
                                        })
                                        .executes(KnowledgeCommand::executeLearn)
                                        .then(Commands.argument("id", IdentifierArgument.id())
                                                .suggests((context, builder) -> {
                                                    String type = StringArgumentType.getString(context, "type").toUpperCase();
                                                    if ("*".equals(type))
                                                        return builder.buildFuture();
                                                    Knowledge.Type knowledgeType = Knowledge.Type.valueOf(type);
                                                    return suggestTypeIds(context.getSource().registryAccess(), knowledgeType, builder);
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

        //builder.suggest("*");
        registry.keySet().forEach(id -> builder.suggest(id.toString()));
        return builder.buildFuture();
    }

    private static int executeLearn(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        KnowledgeManager manager = KnowledgeManager.getServerState(source.getServer());

        String type = StringArgumentType.getString(context, "type").toUpperCase();

        List<Knowledge> knowledge = new ArrayList<>();

        if ("*".equals(type)) {

        } else {
            Knowledge.Type knowledgeType = Knowledge.Type.valueOf(type);
            Identifier id = IdentifierArgument.getId(context, "id");

            knowledge.add(new Knowledge(knowledgeType, id));

            Set<Knowledge> toSend = new HashSet<>();
            for (Knowledge k : knowledge) {
                if (manager.learn(source.getPlayer(), k)) {
                    toSend.add(k);
                }
            }

            if (!toSend.isEmpty()) {
                ServerPlayNetworking.send(source.getPlayer(), new LearnKnowledgeS2C(knowledgeType, toSend));
                source.sendSuccess(() -> Component.translatable("commands.knowledge.success", id.toString(), knowledgeType.name()), false);
                return 1;
            } else {
                source.sendFailure(Component.translatable("commands.knowledge.nothing"));
                return 0;
            }
        }

        Knowledge first = knowledge.getFirst();
        source.sendFailure(Component.translatable("commands.knowledge.fail", first.data(), first.type().name()));
        return -1;
    }
}
