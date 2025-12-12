package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.command.GameEventCommand;
import dev.creoii.greatbigworld.command.HealCommand;
import dev.creoii.greatbigworld.command.KnowledgeCommand;
import dev.creoii.greatbigworld.command.VelocityCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class GBWCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, registrationEnvironment) -> {
            GameEventCommand.register(dispatcher);
            HealCommand.register(dispatcher);
            KnowledgeCommand.register(dispatcher);
            VelocityCommand.register(dispatcher);
        });
    }
}
