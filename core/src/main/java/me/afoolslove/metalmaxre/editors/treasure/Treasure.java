package me.afoolslove.metalmaxre.editors.treasure;

import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 宝藏数据
 *
 * @author AFoolLove
 */
public class Treasure extends Point2B {
    /**
     * 宝藏所在的地图
     */
    public byte map;
    /**
     * 宝藏的内容
     */
    public byte item;

    public Treasure(byte map, byte x, byte y, byte item) {
        super(x, y);
        setMap(map);
        setItem(item);
    }

    public Treasure(@Range(from = 0x00, to = 0xEF) int map,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y,
                    @Range(from = 0x00, to = 0xFF) int item) {
        super(x, y);
        set(map, x, y, item);
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
     * 设置宝藏所在的地图
     *
     * @param map 地图
     */
    public void setMap(@Range(from = 0x00, to = 0xFF) int map) {
        this.map = (byte) (map & 0xFF);
    }

    /**
     * 设置宝藏所在的地图
     *
     * @param map 地图
     */
    public void setMap(byte map) {
        this.map = map;
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
     * @return 这个宝藏所在的地图
     */
    public byte getMap() {
        return map;
    }

    public int intMap() {
        return getMap() & 0xFF;
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

    /**
     * @return 创建一个与当前宝藏一样的宝藏
     */
    @NotNull
    public Treasure copy() {
        return new Treasure(getMap(), getX(), getY(), getItem());
    }


    @Override
    public String toString() {
        return String.format("Treasure{map=%02X, x=%02X, y=%02X, item=%02X}", getMap(), getX(), getY(), getItem());
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
