package me.afoolslove.metalmaxre.editors.treasure;

import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 宝藏数据
 *
 * @author AFoolLove
 */
public class Treasure extends MapPoint {
    /**
     * 宝藏的内容
     */
    private byte item;

    public Treasure() {
        super();
    }

    public Treasure(byte map, byte x, byte y, byte item) {
        super(map, x, y);
        setItem(item);
    }

    public Treasure(@Range(from = 0x00, to = 0xEF) int map,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y,
                    @Range(from = 0x00, to = 0xFF) int item) {
        super(map, x, y);
        setItem(item);
    }

    public Treasure(@NotNull Point2B point2B) {
        super(point2B);
    }

    public Treasure(@NotNull MapPoint mapPoint) {
        super(mapPoint);
    }

    public Treasure(@NotNull Treasure treasure) {
        super(treasure);
        setItem(treasure.getItem());
    }

    /**
     * 重新当前宝藏的位置和宝藏内容
     *
     * @param map  所在地图
     * @param x    所在的 X 坐标
     * @param y    所在的 Y 坐标
     * @param item 宝藏
     */
    public void set(int map, int x, int y, int item) {
        setMap(map);
        setX(x);
        setY(y);
        setItem(item);
    }

    /**
     * 设置宝藏内容
     *
     * @param item 宝藏
     */
    public void setItem(@Range(from = 0x00, to = 0xFF) int item) {
        this.item = (byte) (item & 0xFF);
    }

    /**
     * 设置宝藏内容
     *
     * @param item 宝藏
     */
    public void setItem(byte item) {
        this.item = item;
    }

    /**
     * @return 这个宝藏的内容
     */
    public byte getItem() {
        return item;
    }

    public int intItem() {
        return getItem() & 0xFF;
    }

    /**
     * @return 是否为空宝藏
     */
    public boolean isEmptyTreasure() {
        return getItem() == 0x00;
    }

    /**
     * @return 宝藏是否有效
     */
    public boolean isNull() {
        return (getItem() & getX() & getY() & getX()) == (byte) 0xFF;
    }

    @Override
    public String toString() {
        return String.format("map=%02X, x=%02X, y=%02X, item=%02X", getMap(), getX(), getY(), getItem());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Treasure treasure)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return getMap() == treasure.getMap()
               && getItem() == treasure.getItem();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMap(), getItem());
    }
}
