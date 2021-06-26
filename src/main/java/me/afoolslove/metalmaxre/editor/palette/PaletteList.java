package me.afoolslove.metalmaxre.editor.palette;

import me.afoolslove.metalmaxre.ColorTool;

import java.awt.*;
import java.util.ArrayList;

/**
 * 调色板集
 * 共4个元素
 * 最后一个调色板固定为黑白
 *
 * @author AFoolLove
 */
public class PaletteList extends ArrayList<Palette> {
    /**
     * 黑白的调色板
     */
    public static final Color[][] BLACK_WHITE = new Color[][]{
            {ColorTool.BLACK, ColorTool.WHITE, ColorTool.getColor(0x10), ColorTool.getColor(0x00)},
            {ColorTool.BLACK, ColorTool.WHITE, ColorTool.getColor(0x10), ColorTool.getColor(0x00)},
            {ColorTool.BLACK, ColorTool.WHITE, ColorTool.getColor(0x10), ColorTool.getColor(0x00)},
            {ColorTool.BLACK, ColorTool.WHITE, ColorTool.getColor(0x10), ColorTool.getColor(0x00)}
    };

    /**
     * @return 转换为数组
     */
    public Color[][] toColors() {
        Color[][] colors = new Color[0x04][];
        colors[0x00] = get(0x00).toColors();
        colors[0x01] = get(0x01).toColors();
        colors[0x02] = get(0x02).toColors();
        colors[0x03] = get(0x03).toColors();
        return colors;
    }
}
