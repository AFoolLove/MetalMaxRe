package me.afoolslove.metalmaxre.editor.computer;


/**
 * 计算机
 * 售货机、游戏机、计算机等
 * 计算机的位置与功能
 * <p>
 * 不支持世界地图
 *
 * @author AFoolLove
 */
public class Computer {
    public byte map;
    public byte type;
    public byte x, y;

    public Computer(byte map, byte type, byte x, byte y) {
        this.map = map;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public byte getMap() {
        return map;
    }

    public void setMap(byte map) {
        this.map = map;
    }

    public byte getX() {
        return x;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public byte getY() {
        return y;
    }

    public void setY(byte y) {
        this.y = y;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Computer)) {
            return false;
        }
        Computer computer = (Computer) o;
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
