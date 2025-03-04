package me.afoolslove.metalmaxre.editors.map.tileset;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.map.MapProperties;
import me.afoolslove.metalmaxre.editors.map.WorldMapProperties;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

public interface ITileSetEditor extends IRomEditor {
    @Override
    default String getId() {
        return "tileSetEditor";
    }

    XXTileSet[] getTiles();

    TileCombinationSet[] getCombinations();

    default TileCombinationSet getCombinations(int index) {
        return getCombinations()[index];
    }

    TileCombinationSet[] getCombinations(int... indexes);

    default TileCombinationSet[] getCombinations(@NotNull MapProperties mapProperties) {
        if (mapProperties instanceof WorldMapProperties) {
            return getWorldCombinations();
        }
        return getCombinations(mapProperties.combinationA, mapProperties.combinationB);
    }

    TileAttributeSet[] getAttributes();

    default TileAttributeSet getAttributes(int xXX) {
        return getAttributes()[xXX];
    }

    TileAttributeSet[] getAttributes(int... xXXs);

    default TileAttributeSet[] getAttributes(@NotNull MapProperties mapProperties) {
        if (mapProperties instanceof WorldMapProperties) {
            return getWorldAttributes();
        }
        return getAttributes(mapProperties.combinationA, mapProperties.combinationB);
    }

    TileCombinationSet[] getWorldCombinations();

    TileAttributeSet[] getWorldAttributes();

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
