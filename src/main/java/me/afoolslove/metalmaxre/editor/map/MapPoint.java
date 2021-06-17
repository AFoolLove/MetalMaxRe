package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 地图点
 * X    地图X
 * Y    地图Y
 * Map  地图
 *
 * @author AFoolLove
 */
public class MapPoint extends Point2B {
    public byte map;

    public MapPoint(byte map, byte x, byte y) {
        super(x, y);
        this.map = map;
    }

    public MapPoint(byte x, byte y) {
        super(x, y);
        this.map = 0x00;
    }

    public MapPoint(@NotNull MapPoint point) {
        super(point);
        this.map = point.map;
    }

    public void setMap(byte map) {
        this.map = map;
    }

    public void setMap(@Range(from = 0x00, to = 0xFF) int map) {
        this.map = (byte) (map & 0xFF);
    }

    @Override
    public void set(@Range(from = 0x00, to = 0xFF) int x, @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
    }

    public void set(@Range(from = 0x00, to = 0xFF) int x, @Range(from = 0x00, to = 0xFF) int y, @Range(from = 0x00, to = 0xFF) int map) {
        super.set(x, y);
        setMap(map);
    }

    @Override
    public void set(byte x, byte y) {
        super.set(x, y);
    }

    public void set(byte x, byte y, byte map) {
        super.set(x, y);
        setMap(map);
    }

    public void set(@NotNull MapPoint mapPoint) {
        setMap(mapPoint.map);
        setX(mapPoint.x);
        setY(mapPoint.y);
    }


    public byte getMap() {
        return map;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intMap() {
        return getMap();
    }

    @Override
    public String toString() {
        return String.format("MapPoint{x=%02X, y=%02X, map=%02X}", x, y, map);
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
