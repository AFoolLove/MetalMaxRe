package me.afoolslove.metalmaxre.utils;

import me.afoolslove.metalmaxre.editors.palette.Color;
import me.afoolslove.metalmaxre.editors.palette.SystemPalette;
import org.jetbrains.annotations.NotNull;

/**
 * RGB匹配相似的调色板颜色
 * 使用加权欧几里得距离公式，考虑人眼对不同颜色的敏感度差异
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

    /**
     * 计算两个颜色之间的感知距离
     * 使用基于ITU-R BT.601标准的加权欧几里得距离公式
     * 人眼对绿色最敏感(59%)，红色次之(30%)，蓝色最不敏感(11%)
     *
     * @param c1 颜色1
     * @param c2 颜色2
     * @return 加权后的颜色距离平方值（值越小表示颜色越接近）
     */
    private static int calculateColorDistance(Color c1, Color c2) {
        // 将byte转换为无符号int值（byte范围-128~127，需要转为0~255）
        int r1 = c1.getRed() & 0xFF;
        int g1 = c1.getGreen() & 0xFF;
        int b1 = c1.getBlue() & 0xFF;

        int r2 = c2.getRed() & 0xFF;
        int g2 = c2.getGreen() & 0xFF;
        int b2 = c2.getBlue() & 0xFF;

        int rDiff = r1 - r2;
        int gDiff = g1 - g2;
        int bDiff = b1 - b2;

        // 使用加权系数：R=0.30, G=0.59, B=0.11
        // 这些系数来自ITU-R BT.601亮度公式，更符合人眼感知
        return (int) (0.30 * rDiff * rDiff + 0.59 * gDiff * gDiff + 0.11 * bDiff * bDiff);
//        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }
}