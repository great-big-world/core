package dev.creoii.greatbigworld.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;
import java.util.Optional;

public class GameEventCommand {
    private static final DynamicCommandExceptionType ERROR_INVALID_GAME_EVENT = new DynamicCommandExceptionType(object -> Component.translatableEscape("commands.gameevent.gameevent.invalid", object));

    private static int emitGameEvent(CommandSourceStack commandSourceStack, ServerLevel level, Vec3 pos, ResourceKey<GameEvent> gameEvent) {
        Registry<GameEvent> registry = level.registryAccess().lookupOrThrow(Registries.GAME_EVENT);
        Optional<Holder.Reference<GameEvent>> holder = registry.get(gameEvent);
        if (holder.isPresent()) {
            level.gameEvent(holder.get(), pos, GameEvent.Context.of(commandSourceStack.getPlayer()));
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.gameevent.success", gameEvent.identifier().toString(), formatDouble(pos.x), formatDouble(pos.y), formatDouble(pos.z)), true);
            return 1;
        }

        commandSourceStack.sendFailure(Component.translatable("commands.gameevent.fail", gameEvent.identifier().toString()));
        return 0;
    }

    private static String formatDouble(double d) {
        return String.format(Locale.ROOT, "%f", d);
    }

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("gameevent").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.argument("pos", Vec3Argument.vec3()).then((Commands.argument("gameevent", ResourceKeyArgument.key(Registries.GAME_EVENT)).executes(context -> {
                            return emitGameEvent(context.getSource(), context.getSource().getLevel(), Vec3Argument.getVec3(context, "pos"), ResourceKeyArgument.getRegistryKey(context, "gameevent", Registries.GAME_EVENT, ERROR_INVALID_GAME_EVENT));
                        })))));
    }
}
