package dev.creoii.greatbigworld.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.OptionInstance;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class OptionsAPI {
    public static final Map<Identifier, OptionInstance<?>> CUSTOM_VIDEO_OPTIONS = new HashMap<>();

    public static void registerVideoOption(Identifier id, OptionInstance<?> optionInstance) {
        CUSTOM_VIDEO_OPTIONS.put(id, optionInstance);
    }

    public static OptionInstance<?> getOption(Identifier id) {
        return CUSTOM_VIDEO_OPTIONS.get(id);
    }
}
