package me.afoolslove.metalmaxre.palette;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;

/**
 * 调色板编辑器
 *
 * @author AFoolLove
 */
public interface IPaletteEditor extends IRomEditor {

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
     * @return 调色板数据地址
     */
    DataAddress getPaletteAddress();

    /**
     * @return 全局精灵调色板（非战斗时）
     */
    DataAddress getGlobalSpritePalettesAddress();

    /**
     * @return 全局精灵调色板（战斗时）
     */
    DataAddress getBattleGlobalSpritePalettesAddress();
}
