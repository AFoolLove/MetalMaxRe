package me.afoolslove.metalmaxre.editors.map.tileset;

import me.afoolslove.metalmaxre.editors.palette.Color;
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

    public Color[][] toColors(Color[] colors) {
        Color[][] image = new Color[0x08][0x08];
        final byte[] bytes = getTileData(); // 0x10 size
        for (int b = 0; b < 0x08; b++) { // byte
            for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                int l = (bytes[b] & d) >>> (7 - k);
                l += ((bytes[b + 0x08] & d) >>> (7 - k)) << 1;
                image[b][k] = colors[l];
            }
        }
        return image;
    }

    public byte[] cloneTileData() {
        return tileData.clone();
    }
}
