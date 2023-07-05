package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IDogSystemEditor extends IRomEditor {
    @Override
    default String getId() {
        return "dogSystemEditor";
    }

    /**
     * 获取城镇的数量
     *
     * @return 城镇的数量
     */
    default int getTownMaxCount() {
        return 0x0C;
    }

    /**
     * 获取时空隧道机器目的地的数量
     *
     * @return 时空隧道机器目的地的数量
     */
    default int getTeleportMaxCount() {
        // 1个时空隧道传送错误和3个未知数据(可能是坐标？)
        return 0x0C + 0x01 + 0x03;
    }

    /**
     * 城镇所在的位置
     * <p>
     * *犬系统传送时使用的坐标
     *
     * @return 城镇所在的位置
     */
    @NotNull
    List<CameraMapPoint> getTownLocations();

    /**
     * 获取指定城镇所在的位置
     *
     * @param townIndex 城镇索引
     * @return 指定城镇所在的位置
     */
    default CameraMapPoint getTownLocation(int townIndex) {
        return getTownLocations().get(townIndex);
    }

    /**
     * 时空隧道传送的目的地位置
     * <p>
     * *时空隧道机器传送使用的目的地位置
     *
     * @return 时空隧道传送的目的地位置
     */
    List<CameraMapPoint> getTeleportLocations();

    /**
     * 获取指定时空隧道传送的目的地位置
     *
     * @param teleportIndex 目的地索引，一般与城镇索引相同
     * @return 指定时空隧道传送的目的地位置
     */
    default CameraMapPoint getTeleportLocation(int teleportIndex) {
        return getTeleportLocations().get(teleportIndex);
    }

    /**
     * 获取所有地图和进入地图后开启的事件代码
     * <p>
     * *list的索引为城镇索引，值为对应的地图
     *
     * @return 所有城镇对应地图
     */
    @NotNull
    List<SingleMapEntry<Byte, Byte>> getTowns();

    /**
     * 获取指定位置的地图和进入地图后开启的事件代码
     *
     * @param townIndex 索引
     * @return 指定位置的地图和进入地图后开启的事件代码
     */
    default SingleMapEntry<Byte, Byte> getTown(int townIndex) {
        return getTowns().get(townIndex);
    }

    /**
     * 获取地图和进入地图后开启的事件代码
     *
     * @return 地图和进入地图的事件代码
     */
    @NotNull
    DataAddress getTownsAddress();

    /**
     * 获取使用犬系统传送的目的地址
     *
     * @return 犬系统传送的目的地址
     */
    @NotNull
    DataAddress getTownLocationsAddress();

    /**
     * 获取使用时空隧道机器传送的目的地地址
     *
     * @return 时空隧道机器目的地地址
     */
    @NotNull
    DataAddress getTeleportLocationAddress();
}
