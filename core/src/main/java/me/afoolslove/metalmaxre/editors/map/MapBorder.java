package me.afoolslove.metalmaxre.editors.map;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 * 地图边界
 * <p>
 * 移动到地图边界外后，传送的目的地
 * 本身储存边界地图目标位置
 *
 * @author AFoolLove
 */
public class MapBorder extends LinkedList<MapPoint> {
    /**
     * 地图边界的类型
     */
    public MapBorderType type;

    public MapBorder(@NotNull MapBorderType type) {
        this.type = type;
    }

    /**
     * 设置地图边界的类型
     *
     * @param type 边界类型
     */
    public void setType(@NotNull MapBorderType type) {
        this.type = type;
    }

    /**
     * @return 地图边界的类型
     */
    public MapBorderType getType() {
        return type;
    }

    public byte[] toByteArray() {
        return switch (type) {
            case LAST -> {
                byte[] bytes = new byte[0x01];
                bytes[0x00] = type.getValue();
                yield bytes;
            }
            case FIXED -> {
                // 不要少于1个数据！（懒得检测
                byte[] bytes = new byte[0x01 + 0x03];
                bytes[0x00] = type.getValue();
                MapPoint first = getFirst();
                bytes[0x01] = first.getMap();
                bytes[0x02] = first.getX();
                bytes[0x03] = first.getY();
                yield bytes;
            }
            default -> {
                // 不要少于4个数据！
                byte[] bytes = new byte[0x03 * 0x04];
                for (int count = 0; count < 0x04; count++) {
                    MapPoint mapPoint = get(count);
                    bytes[(count * 3) + 0x00] = mapPoint.getMap();
                    bytes[(count * 3) + 0x01] = mapPoint.getX();
                    bytes[(count * 3) + 0x02] = mapPoint.getY();
                }
                yield bytes;
            }
        };
    }
}
