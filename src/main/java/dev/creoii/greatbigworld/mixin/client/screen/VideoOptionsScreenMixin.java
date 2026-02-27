package dev.creoii.greatbigworld.mixin.client.screen;

import dev.creoii.greatbigworld.util.OptionsAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;

@Mixin(VideoSettingsScreen.class)
public class VideoOptionsScreenMixin {
    @Inject(method = "qualityOptions", at = @At("RETURN"), cancellable = true)
    private static void gbw$addCustomVideoOptions(Options gameOptions, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        int size = OptionsAPI.CUSTOM_VIDEO_OPTIONS.size();
        if (size > 0) {
            int oldSize = cir.getReturnValue().length;

            OptionInstance<?>[] newOptions = Arrays.copyOf(cir.getReturnValue(), cir.getReturnValue().length + size);

            int i = 0;
            for (OptionInstance<?> optionInstance : OptionsAPI.CUSTOM_VIDEO_OPTIONS.values()) {
                newOptions[oldSize + i++] = optionInstance;
            }

            cir.setReturnValue(newOptions);
        }
    }
}
