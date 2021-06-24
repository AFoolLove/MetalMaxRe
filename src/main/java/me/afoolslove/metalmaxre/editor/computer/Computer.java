package me.afoolslove.metalmaxre.editor.computer;


import me.afoolslove.metalmaxre.Point2B;
import me.afoolslove.metalmaxre.editor.map.MapEditor;
import org.jetbrains.annotations.Range;

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

    public void setMap(byte map) {
        this.map = map;
    }

    public void setMap(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map) {
        this.map = (byte) (map & 0xFF);
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setType(@Range(from = 0x00, to = 0xFF) int type) {
        this.type = (byte) (type & 0xFF);
    }

    @Override
    public void set(@Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
    }

    @Override
    public void set(byte x, byte y) {
        super.set(x, y);
    }

    public void set(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map,
                    @Range(from = 0x00, to = 0xFF) int type,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
        setMap(map);
        setType(type);
    }

    public byte getMap() {
        return map;
    }

    @Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1)
    public int intMap() {
        return map;
    }

    public byte getType() {
        return type;
    }

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
        int result = getMap();
        result = 31 * result + (int) getX();
        result = 31 * result + (int) getY();
        result = 31 * result + (int) getType();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Computer{map=%02X, x=%02X, y=%02X, type=%02X}", map, x, y, type);
    }
}
