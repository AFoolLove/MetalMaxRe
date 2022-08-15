package me.afoolslove.metalmaxre.utils;

import me.afoolslove.metalmaxre.editors.palette.Color;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class BufferedImageUtils {
    private BufferedImageUtils() {
    }

    public static BufferedImage fromColors(@NotNull Color[][] image) {
        int width = image[0].length;
        int height = image.length;

        java.awt.Color[][] colors = new java.awt.Color[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                java.awt.Color color = colors[y][x];
                if (color != null) {
                    continue;
                }
                if (image[y][x] == null) {
                    // 透明
                    continue;
                }

                colors[y][x] = image[y][x].toAwtColor();
                // 更新后面相同的颜色
                for (int y2 = y; y2 < height; y2++) {
                    for (int x2 = x + 1; x2 < width; x2++) {
                        if (image[y2][x2] == image[y][x]) {
                            colors[y2][x2] = colors[y][x];
                        }
                    }
                }
            }
        }

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var graphics = bufferedImage.getGraphics();

        for (int y = 0; y < colors.length; y++) {
            final java.awt.Color[] cs = colors[y];
            for (int x = 0; x < cs.length; x++) {
                if (cs[x] == null) {
                    continue;
                }
                graphics.setColor(cs[x]);
                graphics.drawLine(x, y, x, y);
            }
        }
        graphics.dispose();
        return bufferedImage;
    }
}
