package dev.creoii.greatbigworld.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class ScreenShakeManager {
    private static float INTENSITY = 0;
    private static int TIME = 0;
    private static int MAX_TIME = 0;

    private static float X_OFFSET, Y_OFFSET;
    private static Easing EASING = Easing.NONE;

    public static void addShake(float amount, int duration, Easing easing) {
        INTENSITY = Math.max(INTENSITY, amount);
        TIME = Math.max(TIME, duration);
        MAX_TIME = TIME;
        ScreenShakeManager.EASING = easing;
    }

    public static void tick() {
        if (TIME > 0) {
            --TIME;

            float intensity = ScreenShakeManager.INTENSITY * EASING.getApplier().apply(1f - (TIME / (float) MAX_TIME));
            X_OFFSET = (float) ((Math.random() - .5f) * intensity);
            Y_OFFSET = (float) ((Math.random() - .5f) * intensity);
        } else {
            INTENSITY = 0;
            X_OFFSET = Y_OFFSET = 0;
        }
    }

    public static float getXOffset() {
        return X_OFFSET;
    }

    public static float getYOffset() {
        return Y_OFFSET;
    }

    // TODO: Custom class to track start/end
    public enum Easing {
        NONE(t -> t),
        IN(t -> t * t),
        OUT(t -> 1 - (1 - t) * (1 - t)),
        IN_OUT(t -> {
            if (t < 0.5f)
                return 2 * t * t;
            float u = t - 0.5f;
            return 1 - 2 * u * u;
        });

        private final Function<Float, Float> applier;

        Easing(Function<Float, Float> applier) {
            this.applier = applier;
        }

        public Function<Float, Float> getApplier() {
            return applier;
        }
    }
}