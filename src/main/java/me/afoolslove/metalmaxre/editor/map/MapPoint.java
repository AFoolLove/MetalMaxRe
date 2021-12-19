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
 * <p>
 * CameraX 以相机坐标设置，左上角为原点
 * CameraY 以相机坐标设置，左上角为原点
 *
 * @author AFoolLove
 */
public class MapPoint extends Point2B {
    public byte map;

    public MapPoint() {
        super(0, 0);
    }

    public MapPoint(byte map, byte x, byte y) {
        super(x, y);
        this.map = map;
    }

    public MapPoint(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super(x, y);
        this.map = (byte) (map & 0xFF);
    }

    public MapPoint(byte x, byte y) {
        super(x, y);
        this.map = 0x00;
    }

    public MapPoint(@Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
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

    public void offset(int map, int x, int y) {
        this.map += map;
        super.offset(x, y);
    }

    public void offsetMap(int map) {
        this.map += map;
    }

    @Override
    public void set(@Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
    }

    public void set(@Range(from = 0x00, to = 0xFF) int map,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
        setMap(map);
    }

    public void setCameraX(@Range(from = 0x00, to = 0xFF) int x) {
        setCameraX((byte) (x & 0xFF));
    }

    public void setCameraX(byte x) {
        x += 0x08;
        super.setX(x);
    }

    public void setCameraY(@Range(from = 0x00, to = 0xFF) int y) {
        setCameraY((byte) (y & 0xFF));
    }

    public void setCameraY(byte y) {
        y += 0x07;
        super.setY(y);
    }

    public void setCamera(@Range(from = 0x00, to = 0xFF) int map,
                          @Range(from = 0x00, to = 0xFF) int x,
                          @Range(from = 0x00, to = 0xFF) int y) {
        setMap(map);
        setCamera((byte) (x & 0xFF), (byte) (y & 0xFF));
    }

    public void setCamera(byte map,
                          byte x,
                          byte y) {
        setMap(map);
        setCamera(x, y);
    }

    public void setCamera(@Range(from = 0x00, to = 0xFF) int x,
                          @Range(from = 0x00, to = 0xFF) int y) {
        setCamera((byte) (x & 0xFF), (byte) (y & 0xFF));
    }

    public void setCamera(byte x, byte y) {
        x += 0x08;
        y += 0x07;
        super.set(x, y);
    }

    @Override
    public void set(byte x, byte y) {
        super.set(x, y);
    }

    public void set(byte map, byte x, byte y) {
        super.set(x, y);
        setMap(map);
    }

    public void set(@NotNull MapPoint mapPoint) {
        setMap(mapPoint.getMap());
        setX(mapPoint.getX());
        setY(mapPoint.getY());
    }

    public byte getMap() {
        return map;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intMap() {
        return getMap() & 0xFF;
    }

    public byte getCameraX() {
        return (byte) ((getX() - 0x08) & 0xFF);
    }

    @Range(from = 0x00, to = 0xFF)
    public int intCameraX() {
        return getCameraX() & 0xFF;
    }

    public byte getCameraY() {
        return (byte) ((getY() - 0x07) & 0xFF);
    }

    @Range(from = 0x00, to = 0xFF)
    public int intCameraY() {
        return getCameraY() & 0xFF;
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

    @NotNull
    public MapPoint copy() {
        return new MapPoint(map, x, y);
    }
}
