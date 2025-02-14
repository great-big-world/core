package dev.creoii.greatbigworld.util;

public final class ColorHelper {
    public static int add(int color, int r, int g, int b) {
        return (Math.min(red(color) + r, 255) << 16) | (Math.min(green(color) + g, 255) << 8) | Math.min(blue(color) + b, 255);
    }

    public static int add(int color, int r, int g, int b, int a) {
        return (Math.min(alpha(color) + a, 255) << 24) | (Math.min(red(color) + r, 255) << 16) | (Math.min(green(color) + g, 255) << 8) | Math.min(blue(color) + b, 255);
    }

    public static int interpolate(double delta, int color1, int color2) {
        delta = Math.max(Math.min(delta, 1f), 0f);

        int red = (int) (red(color1) + ((red(color2) - red(color1)) * delta));
        int green = (int) (green(color1) + ((green(color2) - green(color1)) * delta));
        int blue = (int) (blue(color1) + ((blue(color2) - blue(color1)) * delta));

        return Math.max(Math.min(red, 255), 0) << 16 | Math.max(Math.min(green, 255), 0) << 8 | Math.max(Math.min(blue, 255), 0);
    }

    public static int alpha(int color) {
        return color >> 24 & 0xff;
    }

    public static int red(int color) {
        return color >> 16 & 0xff;
    }

    public static int green(int color) {
        return color >> 8 & 0xff;
    }

    public static int blue(int color) {
        return color & 0xff;
    }
}
