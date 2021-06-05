package me.afoolslove.metalmaxre.editor.palette;

import me.afoolslove.metalmaxre.ColorTool;

import java.awt.*;
import java.util.LinkedList;

/**
 * 调色板集
 * 共4个元素
 * 最后一个调色板固定为黑白
 *
 * @author AFoolLove
 */
public class PaletteList extends LinkedList<Palette> {

    /**
     * @return 转换为数组
     */
    public Color[][] toColors() {
        Color[][] colors = new Color[0x04][];
        colors[0x00] = get(0x00).toColors();
        colors[0x01] = get(0x01).toColors();
        colors[0x02] = get(0x02).toColors();
        // 固定颜色
        colors[0x03] = new Color[]{Color.BLACK, Color.WHITE, ColorTool.getColor(0x10), ColorTool.getColor(0x00)};
        return colors;
    }
}
