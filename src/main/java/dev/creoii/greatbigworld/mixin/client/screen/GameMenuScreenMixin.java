package dev.creoii.greatbigworld.mixin.client.screen;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.client.screen.KnowledgeScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin {
    @Shadow
    protected abstract ButtonWidget createButton(Text text, Supplier<Screen> screenSupplier);

    @Unique
    private static final Text KNOWLEDGE_TEXT = Text.translatable("gui.knowledge");

    @Inject(method = "initWidgets", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/GridWidget$Adder;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;", ordinal = 0))
    private void gbw$addKnowledgeButton(CallbackInfo ci, @Local GridWidget.Adder adder) {
        ButtonWidget buttonWidget = createButton(KNOWLEDGE_TEXT, () -> new KnowledgeScreen((GameMenuScreen) (Object) this));
        buttonWidget.setWidth(204);
        adder.add(buttonWidget, 2);
    }
}
