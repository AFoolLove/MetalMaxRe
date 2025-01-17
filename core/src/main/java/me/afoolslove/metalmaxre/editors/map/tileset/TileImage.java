package me.afoolslove.metalmaxre.editors.map.tileset;

import org.jetbrains.annotations.NotNull;

/**
 * 8*8图案数据
 * <p>
 * 占用16字节
 *
 * @author AFoolLove
 */
public class TileImage {
    private final byte[] tileData;

    public TileImage() {
        this.tileData = new byte[0x10];
    }

    public TileImage(byte[] tileData) {
        this();
        setTileData(tileData);
    }

    public TileImage(@NotNull TileImage tile, boolean copy) {
        this(copy ? tile.tileData.clone() : tile.tileData);
    }

    public TileImage(@NotNull TileImage tile) {
        this(tile, true);
    }


    public byte[] getTileData() {
        return tileData;
    }

    public void setTileData(byte[] tileData, int offset, int length) {
        // 确认length不会超过数组长度
        length = Math.min(length, 0x10);
        length = Math.min(length, tileData.length - offset);
        System.arraycopy(tileData, offset, this.tileData, 0, length);
    }

    public void setTileData(byte[] tileData, int offset) {
        int length = Math.min(tileData.length - offset, 0x10);
        setTileData(tileData, offset, length);
    }

    public void setTileData(byte[] tileData) {
        setTileData(tileData, 0);
    }

    public byte[] cloneTileData() {
        return tileData.clone();
    }
}
