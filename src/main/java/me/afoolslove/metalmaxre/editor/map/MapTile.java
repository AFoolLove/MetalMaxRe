package me.afoolslove.metalmaxre.editor.map;

import org.jetbrains.annotations.Range;

/**
 * 地图块和数量
 * <p>
 * 当数量为0时，实际数量为256
 * 单个数量最大0x7F(127)
 * <p>
 * 数量在3以下时，最好分割为1个1个的
 * 多数量请3或以上才使用，否则只会增大体积
 *
 * @author AFoolLove
 */
public class MapTile {
    public byte tile;
    public byte count;

    public MapTile(@Range(from = 0x00, to = 0xFF) int tile,
                   @Range(from = 0x00, to = 0x7F) int count) {
        this.tile = (byte) (tile & 0xFF);
        this.count = (byte) (count & 0x7F);
    }

    public MapTile(byte tile, @Range(from = 0x00, to = 0x7F) byte count) {
        this.tile = tile;
        this.count = count;
    }

    public MapTile(@Range(from = 0x00, to = 0xFF) int tile) {
        this.tile = (byte) (tile & 0xFF);
        this.count = 1;
    }

    public MapTile(byte tile) {
        this.tile = tile;
        this.count = 1;
    }

    public byte getTile() {
        return tile;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intTile() {
        return getTile() & 0xFF;
    }

    public void setTile(byte tile) {
        this.tile = tile;
    }

    public void setTile(@Range(from = 0x00, to = 0xFF) int tile) {
        this.tile = (byte) (tile & 0xFF);
    }

    /**
     * @return tile的数量
     */
    public byte getCount() {
        return count;
    }

    @Range(from = 0x00, to = 0x7F)
    public int intCount() {
        return getCount() & 0x7F;
    }

    public void setCount(@Range(from = 0x00, to = 0x7F) int count) {
        this.count = (byte) (count & 0x7F);
    }

    public void setCount(@Range(from = 0x00, to = 0x7F) byte count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("tile=%02X count=%03d}", tile, count);
    }
}
