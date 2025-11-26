package dev.creoii.greatbigworld.mixin.client.screen;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(CreateWorldScreen.GameTab.class)
public class CreateWorldScreenGameTabMixin {
    @Shadow @Final private TextFieldWidget worldNameField;
    @Shadow @Final CreateWorldScreen field_42174;

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/WorldCreator;addListener(Ljava/util/function/Consumer;)V", ordinal = 0))
    private void gbw$addGBWPrefixToWorldDirectoryName(WorldCreator instance, Consumer<WorldCreator> listener) {
        field_42174.getWorldCreator().addListener((creator) -> {
            worldNameField.setTooltip(Tooltip.of(Text.translatable("selectWorld.targetFolder", Text.literal("gbw/" + /*FileSystems.getDefault().getSeparator() +*/ creator.getWorldDirectoryName()).formatted(Formatting.ITALIC))));
        });
    }
}
