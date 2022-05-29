package me.afoolslove.metalmaxre.editors.map.events;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IEventTilesEditor extends IRomEditor {
    /**
     * @return 事件图块数据地址
     */
    DataAddress getEventTilesAddress();

    /**
     * K：Map<p>
     * V：events<p>
     * -- K: event<p>
     * -- V: tile,x,y
     *
     * @return 所有地图的事件图块
     */
    Map<Integer, Map<Integer, List<EventTile>>> getEventTiles();

    /**
     * @return 获取指定map的事件图块，可能为null，包含世界地图
     */
    Map<Integer, List<EventTile>> getEventTile(int map);

    /**
     * @return 获取世界地图的事件图块
     */
    default Map<Integer, List<EventTile>> getWorldEventTile() {
        return getEventTiles().get(0x00);
    }

    /**
     * 特殊事件图块<p>
     * 罗克东部涨潮和退潮的4个4*4tile
     */
    WorldMapInteractiveEvent getWorldMapInteractiveEvent();
}
