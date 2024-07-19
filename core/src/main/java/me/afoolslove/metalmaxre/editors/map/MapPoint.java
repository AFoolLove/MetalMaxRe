package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 地图点
 *
 * @author AFoolLove
 */
public class MapPoint extends Point2B {
    private Byte map;

    public MapPoint() {
        super();
    }

    public MapPoint(Byte map) {
        super();
        setMap(map);
    }

    public MapPoint(byte x, byte y) {
        super(x, y);
        setMap(0);
    }

    public MapPoint(Byte map, byte x, byte y) {
        super(x, y);
        setMap(map);
    }

    public MapPoint(byte map, byte x, byte y) {
        super(x, y);
        setMap(map);
    }

    public MapPoint(@Range(from = 0x00, to = 0xFF) Byte map,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super(x, y);
        setMap(map);
    }

    public MapPoint(@Range(from = 0x00, to = 0xFF) int map,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super(x, y);
        setMap(map);
    }

    public MapPoint(@NotNull Point2B point2B) {
        super(point2B);
    }

    public MapPoint(@NotNull MapPoint mapPoint) {
        super(mapPoint);
        setMap(mapPoint.getMap());
    }


    public void setMap(int map) {
        setMap(Byte.valueOf((byte) (map & 0xFF)));
    }

    public void setMap(Byte map) {
        this.map = map;
    }

    public Byte getMap() {
        return map;
    }

    public Integer intMap() {
        if (getMap() == null) {
            return null;
        } else {
            return map & 0xFF;
        }
    }

    public void offsetMap(int map) {
        if (this.map != null) {
            this.map = (byte) (this.map + map);
        }
    }

    public MapPoint offset(int map, int x, int y) {
        offsetMap(map);
        super.offset(x, y);
        return this;
    }

    public MapPoint set(@Range(from = 0x00, to = 0xFF) int map,
                        @Range(from = 0x00, to = 0xFF) int x,
                        @Range(from = 0x00, to = 0xFF) int y) {
        setMap(map);
        super.set(x, y);
        return this;
    }

    @Override
    public String toString() {
        return String.format("x=%02X, y=%02X, map=%02X", getX(), getY(), getMap());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapPoint mapPoint)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        if (mapPoint.getMap() == null) {
            // 没有设置地图时
            // X和Y正确就行
            return true;
        }
        return Objects.equals(getMap(), mapPoint.getMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMap() == null ? 0 : getMap());
    }
}
