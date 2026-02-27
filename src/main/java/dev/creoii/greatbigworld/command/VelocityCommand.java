package dev.creoii.greatbigworld.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Locale;

public class VelocityCommand {
    private static int addVelocity(CommandSourceStack commandSourceStack, Collection<? extends Entity> entities, Vec3 velocity) {
        if (velocity.isFinite()) {
            for (Entity entity : entities) {
                Vec3 vec3 = entity.getDeltaMovement();
                entity.setDeltaMovement(vec3.x + velocity.x, vec3.y + velocity.y, vec3.z + velocity.z);
                entity.needsSync = true;

                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(entity));
                }
            }

            if (entities.size() == 1) {
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.velocity.success.single", formatDouble(velocity.x), formatDouble(velocity.y), formatDouble(velocity.z), entities.iterator().next().getDisplayName()), true);
            } else commandSourceStack.sendSuccess(() -> Component.translatable("commands.velocity.success.multiple", formatDouble(velocity.x), formatDouble(velocity.y), formatDouble(velocity.z), entities.size()), true);
        }

        return entities.size();
    }

    private static String formatDouble(double d) {
        return String.format(Locale.ROOT, "%f", d);
    }

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(Commands.literal("velocity").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)).then(Commands.argument("targets", EntityArgument.entities()).then((Commands.argument("velocity", Vec3Argument.vec3(false)).executes(context -> addVelocity(context.getSource(), EntityArgument.getEntities(context, "targets"), Vec3Argument.getVec3(context, "velocity")))))));
    }
}
