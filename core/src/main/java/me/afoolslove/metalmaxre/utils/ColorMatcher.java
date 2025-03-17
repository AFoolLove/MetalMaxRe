package me.afoolslove.metalmaxre.utils;

import me.afoolslove.metalmaxre.editors.palette.Color;
import me.afoolslove.metalmaxre.editors.palette.SystemPalette;
import org.jetbrains.annotations.NotNull;

/**
 * RGB匹配相似的调色板颜色
 *
 * *但不理想
 */
public class ColorMatcher {

    public static byte findClosestColor(@NotNull Color targetColor, @NotNull SystemPalette systemPalette) {
        return findClosestColor(targetColor, systemPalette.getColors());
    }

    public static byte findClosestColor(@NotNull Color targetColor, @NotNull Color[] predefinedColors) {
        byte closestColor = 0x00;
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < predefinedColors.length; i++) {
            Color predefinedColor = predefinedColors[i];
            int distance = calculateColorDistance(targetColor, predefinedColor);
            if (distance < minDistance) {
                minDistance = distance;
                closestColor = (byte) i;
            }
        }
        return closestColor;
    }

    private static int calculateColorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }
}