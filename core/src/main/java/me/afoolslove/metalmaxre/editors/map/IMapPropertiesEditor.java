package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.Map;

public interface IMapPropertiesEditor extends IRomEditor {
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

    /**
     * @return 地图属性索引上卷地址
     */
    DataAddress getMapPropertiesIndexUpRollAddress();

    /**
     * @return 地图属性索引下卷地址
     */
    DataAddress getMapPropertiesIndexDownRollAddress();

    /**
     * @return 地图属性地址
     */
    DataAddress getMapPropertiesAddress();

    /**
     * 进入地图时根据条件是否重定向到其它地图，保留坐标
     *
     * @return 进入地图时根据条件是否重定向到其它地图地址
     */
    DataAddress getMapPropertiesRedirectAddress();

    /**
     * @return 地图怪物组合索引
     */
    DataAddress getMapPropertiesMonsterGroupIndexAddress();

    /**
     * @return 自定义地图通缉令地址
     */
    DataAddress getCustomMapWantedAddress();
}
