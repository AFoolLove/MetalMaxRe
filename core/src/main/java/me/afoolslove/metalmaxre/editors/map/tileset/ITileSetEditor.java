package me.afoolslove.metalmaxre.editors.map.tileset;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

public interface ITileSetEditor extends IRomEditor {
    byte[][][] getTiles();
    byte[][][] getCombinations();
    byte[][] getAttributes();
    byte[][][] getWorldCombinations();
    byte[][] getWorldAttributes();
    byte[] getXA597();
    byte[] getXA59B();
    byte[] getXA59E();
    byte[] getXA5DD();
    byte[] getX83F2();
    byte[] getX83FA();
    byte[] getX847B();
    byte[] getX8552();
    byte[] getX8629();

    /**
     * 图块数据起始地址
     * <p>
     * CHR ROM
     */
    DataAddress getTileSetsAddress();

    /**
     * 图块组合数据地址
     * <p>
     * CHR ROM
     */
    DataAddress getTileSetCombinationsAddress();

    /**
     * 图块颜色和图块的特性数据地址
     * <p>
     * CHR ROM
     */
    DataAddress getTileSetAttributesAddress();

    /**
     * 世界地图图块组合数据地址
     */
    DataAddress getWorldTileSetCombinationsAddress();

    /**
     * 世界地图图块颜色和图块的特性数据地址
     */
    DataAddress getWorldTileSetAttributesAddress();
}
