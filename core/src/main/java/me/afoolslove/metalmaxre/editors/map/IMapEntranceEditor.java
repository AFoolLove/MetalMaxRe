package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.Map;

public interface IMapEntranceEditor extends IRomEditor {
    @Override
    default String getId() {
        return "mapEntranceEditor";
    }

    /**
     * @return 所有地图的边界和出入口
     */
    Map<Integer, MapEntrance> getMapEntrances();

    /**
     * @return 指定地图的边界和出入口
     */
    default MapEntrance getMapEntrance(int map) {
        return getMapEntrances().get(map);
    }

    /**
     * @return 世界地图的边界和出入口
     */
    default MapEntrance getWorldMapEntrance() {
        return getMapEntrances().get(0x00);
    }


    /**
     * @return 地图边界和出入口数据地址
     */
    DataAddress getMapEntranceAddress();
}
