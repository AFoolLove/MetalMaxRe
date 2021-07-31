package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * byte类型的X、Y
 *
 * @author AFoolLove
 */
public class Point2B {
    public byte x;
    public byte y;

    public Point2B(byte x, byte y) {
        this.x = x;
        this.y = y;
    }

    public Point2B(@Range(from = 0x00, to = 0xFF) int x, @Range(from = 0x00, to = 0xFF) int y) {
        this.x = (byte) (x & 0xFF);
        this.y = (byte) (y & 0xFF);
    }

    public Point2B(@NotNull Point2B point) {
        this.x = point.x;
        this.y = point.y;
    }

    public byte getX() {
        return x;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intX() {
        return getX() & 0xFF;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public void setX(@Range(from = 0x00, to = 0xFF) int x) {
        setX((byte) (x & 0xFF));
    }

    public byte getY() {
        return y;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intY() {
        return getY() & 0xFF;
    }

    public void setY(byte y) {
        this.y = y;
    }

    public void setY(@Range(from = 0x00, to = 0xFF) int y) {
        setY((byte) (y & 0xFF));
    }

    public void offset(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void offsetX(int x) {
        this.x += x;
    }

    public void offsetY(int y) {
        this.y += y;
    }

    protected void set(byte x, byte y) {
        setX(x);
        setY(y);
    }

    protected void set(@Range(from = 0x00, to = 0xFF) int x, @Range(from = 0x00, to = 0xFF) int y) {
        setX(x);
        setY(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point2B point2B)) {
            return false;
        }
        return Objects.equals(x, point2B.x) && Objects.equals(y, point2B.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
