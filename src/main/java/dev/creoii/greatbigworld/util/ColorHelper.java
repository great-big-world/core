package dev.creoii.greatbigworld.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.material.MapColor;

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

    public static int multiply(int color1, int color2) {
        int r = red(color1) * red(color2) / 255;
        int g = green(color1) * green(color2) / 255;
        int b = blue(color1) * blue(color2) / 255;
        int a = alpha(color1) * alpha(color2) / 255;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int multiply(int color1, int color2, double delta) {
        delta = Math.max(0, Math.min(delta, 1));

        int rBase = red(color1);
        int gBase = green(color1);
        int bBase = blue(color1);
        int aBase = alpha(color1);

        int rMul = rBase * red(color2) / 255;
        int gMul = gBase * green(color2) / 255;
        int bMul = bBase * blue(color2) / 255;
        int aMul = aBase * alpha(color2) / 255;

        int r = (int)(rBase + (rMul - rBase) * delta);
        int g = (int)(gBase + (gMul - gBase) * delta);
        int b = (int)(bBase + (bMul - bBase) * delta);
        int a = (int)(aBase + (aMul - aBase) * delta);

        return (a << 24) | (r << 16) | (g << 8) | b;
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

    public static MapColor getTerracottaColor(DyeColor color) {
        return switch (color) {
            case BROWN -> MapColor.TERRACOTTA_BROWN;
            case RED -> MapColor.TERRACOTTA_RED;
            case ORANGE -> MapColor.TERRACOTTA_ORANGE;
            case YELLOW -> MapColor.TERRACOTTA_YELLOW;
            case LIME -> MapColor.TERRACOTTA_LIGHT_GREEN;
            case GREEN -> MapColor.TERRACOTTA_GREEN;
            case CYAN -> MapColor.TERRACOTTA_CYAN;
            case BLUE -> MapColor.TERRACOTTA_BLUE;
            case LIGHT_BLUE -> MapColor.TERRACOTTA_LIGHT_BLUE;
            case PINK -> MapColor.TERRACOTTA_PINK;
            case MAGENTA -> MapColor.TERRACOTTA_MAGENTA;
            case PURPLE -> MapColor.TERRACOTTA_PURPLE;
            case BLACK -> MapColor.TERRACOTTA_BLACK;
            case GRAY -> MapColor.TERRACOTTA_GRAY;
            case LIGHT_GRAY -> MapColor.TERRACOTTA_LIGHT_GRAY;
            case WHITE -> MapColor.TERRACOTTA_WHITE;
        };
    }
}
