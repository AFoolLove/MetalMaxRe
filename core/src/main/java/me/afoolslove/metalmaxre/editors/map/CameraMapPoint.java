package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 摄像机坐标
 * <p>
 * 普通坐标在中心，摄像机坐标在右上角，要在中心就需要x+8,y+7
 *
 * @author AFoolLove
 */
public class CameraMapPoint extends MapPoint {
    public CameraMapPoint() {
    }

    public CameraMapPoint(byte map, byte x, byte y) {
        super(map, x, y);
    }

    public CameraMapPoint(@Range(from = 0x00, to = 0xEF) int map,
                          @Range(from = 0x00, to = 0xFF) int x,
                          @Range(from = 0x00, to = 0xFF) int y) {
        super(map, x, y);
    }

    public CameraMapPoint(@NotNull Point2B point2B) {
        super(point2B);
    }

    public CameraMapPoint(@NotNull MapPoint mapPoint) {
        super(mapPoint);
    }

    public void setCameraX(@Range(from = 0x00, to = 0xFF) int x) {
        setCameraX((byte) (x & 0xFF));
    }

    public void setCameraX(byte x) {
        x += 0x08;
        setX(x);
    }

    public void setCameraY(@Range(from = 0x00, to = 0xFF) int y) {
        setCameraY((byte) (y & 0xFF));
    }

    public void setCameraY(byte y) {
        y += 0x07;
        setY(y);
    }

    public void setCamera(@Range(from = 0x00, to = 0xFF) int map,
                          @Range(from = 0x00, to = 0xFF) int x,
                          @Range(from = 0x00, to = 0xFF) int y) {
        setMap(map);
        setCamera((byte) (x & 0xFF), (byte) (y & 0xFF));
    }

    public void setCamera(byte map, byte x, byte y) {
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
        set(x, y);
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
        return String.format("cx=%02X, cy=%02X, map=%02X", getCameraX(), getCameraY(), getMap());
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
