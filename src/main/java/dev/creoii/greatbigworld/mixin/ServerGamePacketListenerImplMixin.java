package dev.creoii.greatbigworld.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.creoii.greatbigworld.registry.GBWDataAttachments;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @ModifyExpressionValue(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isInPostImpulseGraceTime()Z"))
    private boolean gbw$allowHollowLogMovement(boolean original) {
        if (original) {
            return true;
        }
        return player.getAttachedOrElse(GBWDataAttachments.PLAYER_ENTERING_HOLLOW_LOG, false);
    }

    @Inject(method = "handleMovePlayer", at = @At("TAIL"))
    private void gbw$clearEnteringHollowLogAttachment(ServerboundMovePlayerPacket serverboundMovePlayerPacket, CallbackInfo ci) {
        player.removeAttached(GBWDataAttachments.PLAYER_ENTERING_HOLLOW_LOG);
    }
}
