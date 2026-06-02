package me.afoolslove.metalmaxre.editors.palette;

import me.afoolslove.metalmaxre.editors.IRomEditor;

import java.util.List;

/**
 * 调色板编辑器
 *
 * @author AFoolLove
 */
public interface IPaletteEditor extends IRomEditor {
    @Override
    default String getId() {
        return "paletteEditor";
    }

    /**
     * @return 调色板数量
     */
    default int getPaletteMaxCount() {
        return 0xA5;
    }

    /**
     * @return 所有调色板集
     */
    List<List<PaletteRow>> getPalettes();

    /**
     * @return 精灵的调色板
     */
    List<PaletteRow> getSpritePalette();

    /**
     * @return 战斗时的精灵调色板
     */
    List<PaletteRow> getBattleSpritePalette();

    /**
     * 通过游戏中使用的数据索引获取调色板集
     * ！不是本程序的索引！
     *
     * @return 调色板
     */
    List<PaletteRow> getPaletteByIndex(int position);
}
