package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * X、Y
 *
 * @author AFoolLove
 */
public class Point<T extends Number> {
    public T x;
    public T y;

    public Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public Point(@NotNull Point<T> point) {
        this.x = point.x;
        this.y = point.y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public void setPoint(T x, T y) {
        setX(x);
        setY(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point)) {
            return false;
        }
        Point<?> point = (Point<?>) o;
        return Objects.equals(x, point.x) && Objects.equals(y, point.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * @author AFoolLove
     */
    public static class Point2D extends Point<Integer> {
        public Point2D(Integer x, Integer y) {
            super(x, y);
        }

        public Point2D(@NotNull Point<Integer> point) {
            super(point);
        }
    }

    /**
     * @author AFoolLove
     */
    public static class Point2B extends Point<Byte> {
        public Point2B(Byte x, Byte y) {
            super(x, y);
        }

        public Point2B(@NotNull Point<Byte> point) {
            super(point);
        }
    }
}
