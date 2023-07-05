package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
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
     * <p>
     * *支持相同出入口
     */
    private final List<SingleMapEntry<MapPoint, MapPoint>> entrance = new ArrayList<>();

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
    public List<SingleMapEntry<MapPoint, MapPoint>> getEntrances() {
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

    /**
     * 复制一个与当前数据一样的对象
     */
    public MapEntrance copy() {
        MapBorder mapBorder = new MapBorder(getBorder().getType());
        for (MapPoint mapPoint : getBorder()) {
            mapBorder.add(new MapPoint(mapPoint));
        }

        MapEntrance mapEntrance = new MapEntrance(mapBorder);
        for (Map.Entry<MapPoint, MapPoint> pointEntry : getEntrances()) {
            mapEntrance.getEntrances().add(SingleMapEntry.create(new MapPoint(pointEntry.getKey()), new MapPoint(pointEntry.getValue())));
        }
        return mapEntrance;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 写入边界数据
        outputStream.writeBytes(getBorder().toByteArray());
        // 写入入口数量
        outputStream.write(getEntrances().size());

        // 写入边界目标位置
        if (!getEntrances().isEmpty()) {
            // 写入地图出入口数据

            // 重新排序，需要顺序写入 出入口的坐标
            List<SingleMapEntry<MapPoint, MapPoint>> listEntrances = new ArrayList<>(getEntrances());
            // 写入入口 X、Y
            for (SingleMapEntry<MapPoint, MapPoint> entry : listEntrances) {
                MapPoint mapPoint = entry.getKey();
                outputStream.write(mapPoint.getX());
                outputStream.write(mapPoint.getY());
            }
            // 写入出口 Map、X、Y
            for (SingleMapEntry<MapPoint, MapPoint> entry : listEntrances) {
                MapPoint mapPoint = entry.getValue();
                outputStream.write(mapPoint.getMap());
                outputStream.write(mapPoint.getX());
                outputStream.write(mapPoint.getY());
            }
        }
        return outputStream.toByteArray();
    }
}
