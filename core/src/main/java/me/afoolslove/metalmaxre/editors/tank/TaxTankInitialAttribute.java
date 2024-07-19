package me.afoolslove.metalmaxre.editors.tank;

import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.utils.Point2B;

/**
 * 出租坦克初始属性
 * <p>
 * *没有初始坐标这个属性，默认为0x00
 *
 * @author AFoolLove
 * @see TankInitialAttribute
 */
public class TaxTankInitialAttribute extends TankInitialAttribute {
    @Override
    public byte getX() {
        return 0x00;
    }

    @Override
    public int intX() {
        return 0x00;
    }

    @Override
    public Point2B offsetX(int x) {
        return this;
    }

    @Override
    public byte getY() {
        return 0x00;
    }

    @Override
    public int intY() {
        return 0x00;
    }

    @Override
    public Point2B offsetY(int y) {
        return this;
    }

    @Override
    public MapPoint offset(int map, int x, int y) {
        return this;
    }

    @Override
    public Point2B offset(int x, int y) {
        return this;
    }
}
