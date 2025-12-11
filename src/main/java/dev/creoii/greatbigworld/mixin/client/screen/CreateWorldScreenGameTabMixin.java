package dev.creoii.greatbigworld.mixin.client.screen;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.network.chat.Component;

@Mixin(CreateWorldScreen.GameTab.class)
public class CreateWorldScreenGameTabMixin {
    @Shadow @Final private EditBox nameEdit;
    @Shadow @Final CreateWorldScreen field_42174;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState;addListener(Ljava/util/function/Consumer;)V", ordinal = 0))
    private void gbw$addGBWPrefixToWorldDirectoryName(WorldCreationUiState instance, Consumer<WorldCreationUiState> listener) {
        field_42174.getUiState().addListener((creator) -> {
            nameEdit.setTooltip(Tooltip.create(Component.translatable("selectWorld.targetFolder", Component.literal("gbw/" + /*FileSystems.getDefault().getSeparator() +*/ creator.getTargetFolder()).withStyle(ChatFormatting.ITALIC))));
        });
    }
}
