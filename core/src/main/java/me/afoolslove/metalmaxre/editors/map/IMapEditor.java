package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.editors.IRomEditor;

import java.util.Map;

public interface IMapEditor extends IRomEditor {
    @Override
    default String getId() {
        return "mapEditor";
    }

    /**
     * @return 地图的数量（包括世界地图
     */
    default int getMapMaxCount() {
        return 0xF0;
    }

    /**
     * @return 获取除世界地图外的所有世界构建器（不包括世界地图
     */
    Map<Integer, MapBuilder> getMaps();

    /**
     * @param map 指定地图
     * @return 指定地图的构建器
     */
    MapBuilder getMap(int map);
}
