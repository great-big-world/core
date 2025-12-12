package dev.creoii.greatbigworld.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class HealCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("heal").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)).then(Commands.argument("targets", EntityArgument.entities()).then((Commands.argument("amount", FloatArgumentType.floatArg(0f)).executes(commandContext -> heal(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), FloatArgumentType.getFloat(commandContext, "amount")))))));
    }

    private static int heal(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, float f) {
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                living.heal(f);
            }
        }

        if (entities.size() == 1) {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.heal.success.single", f, entities.iterator().next().getDisplayName()), true);
        } else commandSourceStack.sendSuccess(() -> Component.translatable("commands.heal.success.multiple", f, entities.size()), true);

        return entities.size();
    }
}
