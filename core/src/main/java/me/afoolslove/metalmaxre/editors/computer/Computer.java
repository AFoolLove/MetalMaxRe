package me.afoolslove.metalmaxre.editors.computer;

import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 计算机<p>
 * map: 计算机所处地图<p>
 * type: 计算机的类型，贩卖机、点唱机、密码电脑等<p>
 * <p>
 * *需要地图属性支持才会生效<p>
 * *不支持世界地图
 *
 * @author AFoolLove
 */
public class Computer extends MapPoint {
    public static final Computer EMPTY_COMPUTER = new Computer() {
        @Override
        public boolean equals(Object o) {
            return o == this;
        }
    };

    private byte type;

    public Computer() {
    }

    public Computer(byte map, byte type, byte x, byte y) {
        super(map, x, y);
        setType(type);
    }

    public Computer(@Range(from = 0x00, to = 0xFF) int map,
                    @Range(from = 0x00, to = 0xFF) int type,
                    @Range(from = 0x00, to = 0xFF) int x,
                    @Range(from = 0x00, to = 0xFF) int y) {
        super(map, x, y);
        setType(type);
    }

    public Computer(@NotNull Point2B point2B) {
        super(point2B);
    }

    public Computer(@NotNull MapPoint mapPoint) {
        super(mapPoint);
    }

    public Computer(@NotNull Computer computer) {
        super(computer);
        setType(computer.getType());
    }

    public byte getType() {
        return type;
    }

    public int intType() {
        return type & 0xFF;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setType(@Range(from = 0x00, to = 0xFF) int type) {
        setType((byte) (type & 0xFF));
    }

    @Override
    public String toString() {
        return String.format("map=0x%02X,type=0x%02X,%s", getMap(), getType(), super.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Computer computer)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return getType() == computer.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getType());
    }
}
