package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.Point;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author AFoolLove
 */
public class MapPoint extends Point<Byte> {
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

    public byte getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = (byte) (map & 0xFF);
    }

    public void setX(int x) {
        super.setX((byte) (x & 0xFF));
    }

    public void setY(int y) {
        super.setY((byte) ((byte) y & 0xFF));
    }

    public void set(int map, int x, int y) {
        setMap(map);
        setX(x);
        setY(y);
    }

    public void set(MapPoint mapPoint) {
        setMap(mapPoint.map);
        setX(mapPoint.x);
        setY(mapPoint.y);
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
        if (!(o instanceof MapPoint)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MapPoint mapPoint = (MapPoint) o;
        return getMap() == mapPoint.getMap();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMap());
    }
}
