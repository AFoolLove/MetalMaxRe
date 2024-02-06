package me.afoolslove.metalmaxre.editors.map.events;

import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 事件图块
 * <p>
 * X    所在地图的X坐标
 * Y    所在地图的Y坐标
 * Tile 图块
 *
 * @author AFoolLove
 */
public class EventTile extends Point2B {
    protected byte tile;

    public EventTile(int x, int y, int tile) {
        super(x, y);
        this.tile = (byte) (tile & 0xFF);
    }

    public EventTile(byte x, byte y, byte tile) {
        super(x, y);
        this.tile = tile;
    }

    public EventTile setTile(int tile) {
        this.tile = (byte) (tile & 0xFF);
        return this;
    }

    public EventTile setTile(byte tile) {
        this.tile = tile;
        return this;
    }

    public byte getTile() {
        return tile;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intTile() {
        return getTile() & 0xFF;
    }

    @Override
    public EventTile set(@Range(from = 0x00, to = 0xFF) int x,
                         @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
        return this;
    }

    public EventTile set(@Range(from = 0x00, to = 0xFF) int tile,
                         @Range(from = 0x00, to = 0xFF) int x,
                         @Range(from = 0x00, to = 0xFF) int y) {
        super.set(x, y);
        setTile(tile);
        return this;
    }

    @Override
    public EventTile set(byte x, byte y) {
        super.set(x, y);
        return this;
    }

    public EventTile set(byte tile, byte x, byte y) {
        super.set(x, y);
        setTile(tile);
        return this;
    }

    public byte[] toByteArray() {
        return new byte[]{getX(), getY(), getTile()};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTile eventTile)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return getTile() == eventTile.getTile();
    }

    /**
     * 判断给定的坐标是否在矩形内
     *
     * @param x 给定的横坐标
     * @param y 给定的纵坐标
     * @return 如果给定的坐标在矩形内，则返回true；否则返回false
     */
    public boolean contains(int x, int y) {
        return getX() == x && getY() == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTile());
    }

    @Override
    public String toString() {
        return super.toString() + String.format(",tile=0x%02X", getTile());
    }
}
