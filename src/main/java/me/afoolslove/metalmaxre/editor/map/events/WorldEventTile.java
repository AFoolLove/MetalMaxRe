package me.afoolslove.metalmaxre.editor.map.events;

import java.util.Arrays;

/**
 * @author AFoolLove
 */
public class WorldEventTile extends EventTile {
    public byte[] tiles;

    public WorldEventTile(int x, int y, int tile) {
        super(x, y, tile);
        tiles = new byte[0x10];
    }

    public WorldEventTile(int x, int y, int tile, byte[] tiles) {
        super(x, y, tile);
        this.tiles = tiles;
    }

    public WorldEventTile(byte x, byte y, byte tile, byte[] tiles) {
        super(x, y, tile);
        this.tiles = tiles;
    }

    public byte[] getTiles() {
        return tiles;
    }

    public void setTiles(byte[] tiles) {
        this.tiles = Arrays.copyOf(tiles, tiles.length);
    }
}
