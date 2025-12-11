package dev.creoii.greatbigworld.mixin.client.screen;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.client.screen.KnowledgeScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(PauseScreen.class)
public abstract class GameMenuScreenMixin {
    @Shadow
    protected abstract Button openScreenButton(Component text, Supplier<Screen> screenSupplier);

    @Unique
    private static final Component KNOWLEDGE_TEXT = Component.translatable("gui.knowledge");

    @Inject(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 0))
    private void gbw$addKnowledgeButton(CallbackInfo ci, @Local GridLayout.RowHelper adder) {
        Button buttonWidget = openScreenButton(KNOWLEDGE_TEXT, () -> new KnowledgeScreen((PauseScreen) (Object) this));
        buttonWidget.setWidth(204);
        adder.addChild(buttonWidget, 2);
    }
}
