package me.afoolslove.metalmaxre.utils;

import org.jetbrains.annotations.Range;

import java.util.Objects;

public class Point2B {
    private byte x;
    private byte y;

    public Point2B() {
        set(0, 0);
    }

    public Point2B(byte x, byte y) {
        set(x, y);
    }

    public Point2B(@Range(from = 0x00, to = 0xFF) int x,
                   @Range(from = 0x00, to = 0xFF) int y) {
        set(x, y);
    }

    public byte getX() {
        return x;
    }

    public int intX() {
        return x & 0xFF;
    }

    public Point2B setX(byte x) {
        this.x = x;
        return this;
    }

    public Point2B setX(@Range(from = 0x00, to = 0xFF) int x) {
        return setX((byte) (x & 0xFF));
    }

    public byte getY() {
        return y;
    }

    public int intY() {
        return y & 0xFF;
    }

    public Point2B setY(byte y) {
        this.y = y;
        return this;
    }

    public Point2B setY(@Range(from = 0x00, to = 0xFF) int y) {
        return setY((byte) (y & 0xFF));
    }

    public Point2B set(byte x, byte y) {
        setX(x);
        setY(y);
        return this;
    }

    public Point2B set(@Range(from = 0x00, to = 0xFF) int x,
                       @Range(from = 0x00, to = 0xFF) int y) {
        setX(x);
        setY(y);
        return this;
    }

    public Point2B offset(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Point2B offsetX(int x) {
        this.x += x;
        return this;
    }

    public Point2B offsetY(int y) {
        this.y += y;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point2B point2B)) return false;
        return getX() == point2B.getX() && getY() == point2B.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return String.format("x=0x%02X,y=0x%02X", this.x, this.y);
    }
}
