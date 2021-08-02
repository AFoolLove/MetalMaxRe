package me.afoolslove.metalmaxre.editor.computer;


import me.afoolslove.metalmaxre.Point2B;
import me.afoolslove.metalmaxre.editor.map.MapEditor;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 计算机
 * 售货机、游戏机、计算机等
 * 计算机的位置与功能
 * <p>
 * 不支持世界地图
 *
 * @author AFoolLove
 */
public class Computer extends Point2B {
    public byte map;
    public byte type;

    public Computer(byte map, byte type, byte x, byte y) {
        super(x, y);
        this.map = map;
        this.type = type;
    }

    public Computer(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map,
                    @Range(from = 0x00, to = 0xFF) int type,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super(x, y);
        this.map = (byte) (map & 0xFF);
        this.type = (byte) (type & 0xFF);
    }

    /**
     * 设置计算机所在的地图
     *
     * @param map 计算机所在的地图
     */
    public void setMap(byte map) {
        this.map = map;
    }

    /**
     * 设置计算机所在的地图
     *
     * @param map 计算机所在的地图
     */
    public void setMap(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map) {
        this.map = (byte) (map & 0xFF);
    }

    /**
     * 设置计算机的类型
     *
     * @param type 计算机的类型
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * 设置计算机的类型
     *
     * @param type 计算机的类型
     */
    public void setType(@Range(from = 0x00, to = 0xFF) int type) {
        this.type = (byte) (type & 0xFF);
    }

    /**
     * 设置计算机所在地图的坐标
     *
     * @param x X
     * @param y Y
     */
    @Override
    public void set(@Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
    }

    /**
     * 设置计算机所在地图的坐标
     *
     * @param x X
     * @param y Y
     */
    @Override
    public void set(byte x, byte y) {
        super.set(x, y);
    }

    /**
     * 设置计算机所在的地图、类型和坐标
     *
     * @param map  计算机所在的地图
     * @param type 计算机的类型
     * @param x    X
     * @param y    Y
     */
    public void set(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map,
                    @Range(from = 0x00, to = 0xFF) int type,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
        setMap(map);
        setType(type);
    }

    /**
     * @return 计算机所在的地图
     */
    public byte getMap() {
        return map;
    }

    /**
     * @return 计算机所在的地图
     */
    @Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1)
    public int intMap() {
        return map;
    }

    /**
     * @return 计算机的类型
     */
    public byte getType() {
        return type;
    }

    /**
     * @return 计算机的类型
     */
    @Range(from = 0x00, to = 0xFF)
    public int intType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Computer computer)) {
            return false;
        }
        return getMap() == computer.getMap()
                && getType() == computer.getType()
                && getX() == computer.getX()
                && getY() == computer.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMap(), getType());
    }

    @Override
    public String toString() {
        return String.format("Computer{map=%02X, x=%02X, y=%02X, type=%02X}", map, x, y, type);
    }
}
