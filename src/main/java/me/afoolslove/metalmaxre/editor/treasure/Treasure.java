package me.afoolslove.metalmaxre.editor.treasure;

import org.jetbrains.annotations.NotNull;

/**
 * 宝藏
 *
 * @author AFoolLove
 */
public class Treasure implements Cloneable {
    /**
     * 这个宝藏所在的地图
     */
    public byte map;
    /**
     * 这个宝藏所在的地图坐标
     */
    public byte x, y;
    /**
     * 这个宝藏的内容
     */
    public byte item;

    public Treasure(int map, int x, int y, int item) {
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
        this.map = (byte) (map & 0xFF);
        this.x = (byte) (x & 0xFF);
        this.y = (byte) (y & 0xFF);
        this.item = (byte) (item & 0xFF);
    }

    /**
     * 设置宝藏的坐标
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void setPosition(int x, int y) {
        this.x = (byte) (x & 0xFF);
        this.y = (byte) (y & 0xFF);
    }

    /**
     * 设置宝藏所在的 X 坐标
     *
     * @param x X 坐标
     */
    public void setX(byte x) {
        this.x = x;
    }

    /**
     * 设置宝藏所在的 Y 坐标
     *
     * @param y Y 坐标
     */
    public void setY(int y) {
        this.y = (byte) (y & 0xFF);
    }

    /**
     * 设置宝藏所在的地图
     *
     * @param map 地图
     */
    public void setMap(int map) {
        this.map = (byte) (map & 0xFF);
    }

    /**
     * 设置宝藏内容
     *
     * @param item 宝藏
     */
    public void setItem(int item) {
        this.item = (byte) (item & 0xFF);
    }


    /**
     * @return 这个宝藏所在的地图
     */
    public byte getMap() {
        return map;
    }

    /**
     * @return 这个宝藏所在的地图 X 坐标
     */
    public byte getX() {
        return x;
    }

    /**
     * @return 这个宝藏所在的地图 Y 坐标
     */
    public byte getY() {
        return y;
    }

    /**
     * @return 这个宝藏的内容
     */
    public byte getItem() {
        return item;
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
        return new Treasure(map, x, y, item);
    }


    @Override
    public String toString() {
        return String.format("Treasure{map=%02X, x=%02X, y=%02X, item=%02X}", map, x, y, item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Treasure)) {
            return false;
        }

        Treasure treasure = (Treasure) o;

        if (getMap() != treasure.getMap()) {
            return false;
        }
        if (getX() != treasure.getX()) {
            return false;
        }
        if (getY() != treasure.getY()) {
            return false;
        }
        return getItem() == treasure.getItem();
    }

    @Override
    public int hashCode() {
        int result = getMap();
        result = 31 * result + (int) getX();
        result = 31 * result + (int) getY();
        result = 31 * result + (int) getItem();
        return result;
    }
}
