package me.afoolslove.metalmaxre.editor.map;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 地图出入口
 * <p>
 * 地图边界和地图出入口储存在同一段数据中，就做一起了
 *
 * @author AFoolLove
 */
public class MapEntrance {
    /**
     * 地图边界类型
     * <p>
     * 0xFF 回到上一个地图，进入当前地图之前的位置
     * 0xFE 传送到指定地图位置
     * 0xXX 其它数据，根据玩家移动的方向，上、下、左、右，传送到不同的地图位置
     */
    public MapBorder border;

    /**
     * 地图内的出入口
     */
    private final Map<MapPoint, MapPoint> entrance = new HashMap<>();

    public MapEntrance(@NotNull MapBorder border) {
        this.border = border;
    }

    /**
     * 默认创建一个返回放一个地图的边界
     */
    public MapEntrance() {
        this.border = new MapBorder(MapBorderType.LAST);
    }

    /**
     * @return 地图内的出入口
     */
    public Map<MapPoint, MapPoint> getEntrances() {
        return entrance;
    }

    /**
     * @return 地图边界
     */
    public MapBorder getBorder() {
        return border;
    }

    public void setBorder(MapBorder border) {
        this.border = border;
    }
}
