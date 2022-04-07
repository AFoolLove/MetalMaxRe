package me.afoolslove.metalmaxre.editors.computer;

import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 计算机<br/>
 * map: 计算机所处地图<br/>
 * type: 计算机的类型，贩卖机、点唱机、密码电脑等<br/>
 * <p>
 * *需要地图属性支持才会生效<br/>
 * *不支持世界地图
 *
 * @author AFoolLove
 */
public class Computer extends Point2B {
    private byte map;
    private byte type;

    public Computer() {
    }

    public Computer(byte map, byte type, byte x, byte y) {
        super(x, y);
        setMap(map);
        setType(type);
    }

    public Computer(@Range(from = 0x00, to = 0xFF) int map,
                    @Range(from = 0x00, to = 0xFF) int type,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super(x, y);
        setMap(map);
        setType(type);
    }

    public byte getMap() {
        return map;
    }

    public int intMap() {
        return map & 0xFF;
    }

    public Computer setMap(byte map) {
        this.map = map;
        return this;
    }

    public Computer setMap(@Range(from = 0x00, to = 0xFF) int map) {
        return setMap((byte) (map & 0xFF));
    }

    public byte getType() {
        return type;
    }

    public int intType() {
        return type & 0xFF;
    }

    public Computer setType(byte type) {
        this.type = type;
        return this;
    }

    public Computer setType(@Range(from = 0x00, to = 0xFF) int type) {
        return setType((byte) (type & 0xFF));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Computer computer)) return false;
        if (!super.equals(o)) return false;
        return getMap() == computer.getMap() && getType() == computer.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getMap(), getType());
    }

    @Override
    public String toString() {
        return String.format("map=0x%02X,type=0x%02X,%s", this.map, this.type, super.toString());
    }
}
