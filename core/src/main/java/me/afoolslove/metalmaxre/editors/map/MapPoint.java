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
    private byte map;

    public MapPoint() {
    }

    public MapPoint(byte map, byte x, byte y) {
        super(x, y);
        setMap(map);
    }

    public MapPoint(@Range(from = 0x00, to = 0xEF) int map,
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
        setMap(map);
    }


    public void setMap(@Range(from = 0x00, to = 0xEF) int map) {
        setMap((byte) (map & 0xFF));
    }

    public void setMap(byte map) {
        this.map = map;
    }

    @Range(from = 0x00, to = 0xEF)
    public byte getMap() {
        return map;
    }

    @Range(from = 0x00, to = 0xEF)
    public int intMap() {
        return map & 0xFF;
    }

    public void offsetMap(int map) {
        this.map += map;
    }

    public MapPoint offset(int map, int x, int y) {
        offsetMap(map);
        super.offset(x, y);
        return this;
    }

    public MapPoint set(@Range(from = 0x00, to = 0xEF) int map,
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
        return getMap() == mapPoint.getMap();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMap());
    }
}
