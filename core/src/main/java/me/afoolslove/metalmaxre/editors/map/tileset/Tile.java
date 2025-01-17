package me.afoolslove.metalmaxre.editors.map.tileset;

/**
 * 图块id及对应的调色板索引
 *
 * @author AFoolLove
 */
public class Tile {
    private byte tile = 0;
    private byte palette = 0;

    public Tile() {
    }

    public Tile(byte tile, byte palette) {
        this.tile = tile;
        this.palette = palette;
    }

    public void setTile(byte tile) {
        this.tile = tile;
    }

    public void setTile(int tile) {
        setTile((byte) (tile & 0xFF));
    }

    public void setPalette(byte palette) {
        this.palette = palette;
    }

    public void setPalette(int palette) {
        setPalette((byte) (palette & 0xFF));
    }

    public byte getTile() {
        return tile;
    }

    public int intTile() {
        return getTile() & 0xFF;
    }

    public byte getPalette() {
        return palette;
    }

    public int intPalette() {
        return getPalette() & 0xFF;
    }
}
