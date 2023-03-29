package me.afoolslove.metalmaxre.editors.map.world;

import me.afoolslove.metalmaxre.editors.map.MapPoint;

/**
 * 航线方向
 */
public enum LineDirection {
    UP((byte) 0x50),
    DOWN((byte) 0x51),
    LEFT((byte) 0x52),
    RIGHT((byte) 0x53),
    END((byte) 0x5E);

    private final byte action;

    LineDirection(byte action) {
        this.action = action;
    }

    /**
     * @return 航线对应的代码
     */
    public byte getAction() {
        return action;
    }

    public int intAction() {
        return getAction() & 0xFF;
    }

    /**
     * @return 通过代码得到实例
     */
    public static LineDirection fromAction(byte action) {
        for (LineDirection value : values()) {
            if (value.action == action) {
                return value;
            }
        }
        return null;
    }

    /**
     * @return 通过坐标xy1到xy2的方向获取
     */
    public static LineDirection fromPoint(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            if (y1 < y2) {
                return UP;
            } else {
                return DOWN;
            }
        } else if (y1 == y2) {
            if (x1 < x2) {
                return RIGHT;
            } else {
                return LEFT;
            }
        }
        return null;
    }

    public static LineDirection fromPoint(MapPoint point1, MapPoint point2) {
        return fromPoint(point1.intX(), point1.intY(), point2.intX(), point2.intY());
    }
}
