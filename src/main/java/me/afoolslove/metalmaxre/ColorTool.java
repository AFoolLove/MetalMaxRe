package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.Range;

import java.awt.*;

/**
 * 所有颜色
 *
 * @author AFoolLove
 */
public class ColorTool {
    /**
     * 所有颜色
     */
    public static final Color[] COLORS = new Color[0x40];

    /**
     * 黑色
     */
    public static final Color BLACK;
    /**
     * 白色
     */
    public static final Color WHITE;

    static {
        int[] colors = new int[]{
                0x585858, 0x00238C, 0x00139B, 0x2D0585, 0x5D0052, 0x7A0017, 0x7A0800, 0x5F1800, 0x352A00, 0x093900, 0x003F00, 0x003C22, 0x00325D, 0x000000, 0x000000, 0x000000,
                0xA1A1A1, 0x0053EE, 0x153CFE, 0x6028E4, 0xA91D98, 0xD41E41, 0xD22C00, 0xAA4400, 0x6C5E00, 0x2D7300, 0x007D06, 0x007852, 0x0069A9, 0x000000, 0x000000, 0x000000,
                0xFFFFFF, 0x1FA5FE, 0x5E89FE, 0xB572FE, 0xFE65F6, 0xFE6790, 0xFE773C, 0xFE9308, 0xC4B200, 0x79CA10, 0x3AD54A, 0x11D1A4, 0x06BFFE, 0x424242, 0x000000, 0x000000,
                0xFFFFFF, 0xA0D9FE, 0xBDCCFE, 0xE1C2FE, 0xFEBCFB, 0xFEBDD0, 0xFEC5A9, 0xFED18E, 0xE9DE86, 0xC7E992, 0xA8EEB0, 0x95ECD9, 0x91E4FE, 0xACACAC, 0x000000, 0x000000,
        };
        for (int i = 0; i < COLORS.length; i++) {
            COLORS[i] = new Color(colors[i]);
        }
        BLACK = COLORS[0x0F];
        WHITE = COLORS[0x30];
    }

    /**
     * @return 指定颜色
     */
    public static Color getColor(@Range(from = 0x00, to = 0x3F) int color) {
        return COLORS[color % 0x40];
    }

    private ColorTool() {
    }
}
