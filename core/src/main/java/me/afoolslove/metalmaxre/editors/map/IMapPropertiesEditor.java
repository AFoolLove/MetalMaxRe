package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.editors.IRomEditor;

import java.util.Map;

public interface IMapPropertiesEditor extends IRomEditor {
    @Override
    default String getId() {
        return "mapPropertiesEditor";
    }

    /**
     * @return 进入地图时根据条件是否重定向到其它地图的最大数量
     */
    default int getMapPropertiesRedirectMaxCount() {
        return 0x03;
    }

    /**
     * @return 所有地图的地图属性
     */
    Map<Integer, MapProperties> getMapProperties();

    /**
     * @return 自定义地图赏金首画像
     */
    Map<Integer, Byte> getCustomMapWanted();

    /**
     * @return 指定地图的地图属性
     */
    default MapProperties getMapProperties(int map) {
        return getMapProperties().get(map);
    }

    /**
     * @return 世界地图的地图属性
     */
    default WorldMapProperties getWorldMapProperties() {
        return ((WorldMapProperties) getMapProperties(0x00));
    }
}
