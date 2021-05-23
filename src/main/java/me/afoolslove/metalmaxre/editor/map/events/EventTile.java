package me.afoolslove.metalmaxre.editor.map.events;

import me.afoolslove.metalmaxre.Point;

/**
 * 事件图块
 *
 * @author AFoolLove
 */
public class EventTile extends Point<Byte> {
    public byte tile;

    public EventTile(byte x, byte y, byte tile) {
        super(x, y);
        this.tile = tile;
    }

    public byte getTile() {
        return tile;
    }
}
