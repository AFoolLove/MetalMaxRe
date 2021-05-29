package me.afoolslove.metalmaxre.editor.map.events;

import me.afoolslove.metalmaxre.Point;

import java.util.Objects;

/**
 * 事件图块
 *
 * @author AFoolLove
 */
public class EventTile extends Point<Byte> {
    public byte tile;

    public EventTile(int x, int y, int tile) {
        super((byte) x, (byte) y);
        this.tile = (byte) tile;
    }

    public void setTile(int tile) {
        this.tile = (byte) tile;
    }

    public byte getTile() {
        return tile;
    }

    public byte[] toArray() {
        return new byte[]{x, y, tile};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTile)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EventTile eventTile = (EventTile) o;
        return getTile() == eventTile.getTile();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTile());
    }
}
