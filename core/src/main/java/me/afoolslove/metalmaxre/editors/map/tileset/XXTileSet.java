package me.afoolslove.metalmaxre.editors.map.tileset;

import java.util.Arrays;

/**
 * 图块集合
 * <p>
 * 包含0x40(64)个图块
 *
 * @author AFoolLove
 */
public class XXTileSet {
    public static final int TILE_SET_LENGTH = 0x40;

    private final TileImage[] tiles;

    public XXTileSet() {
        this(new TileImage[TILE_SET_LENGTH]);
    }

    public XXTileSet(TileImage[] tiles) {
        this.tiles = tiles;
    }

    public TileImage[] getTiles() {
        return tiles;
    }

    public TileImage getTile(int tileId) {
        return tiles[tileId & 0x3F];
    }

    public TileImage getTile(byte tileId) {
        return tiles[tileId & 0x3F];
    }

    /**
     * 将null值填充为默认数据
     * <p>
     * 保留原始数据
     */
    public void fill() {
        for (int i = 0; i < TILE_SET_LENGTH; i++) {
            TileImage tile = this.tiles[i];
            if (tile == null) {
                this.tiles[i] = new TileImage();
            }
        }
    }

    /**
     * 将所有数据设置为初始值
     * <p>
     * 不保留数据
     */
    public void fillZero() {
        for (int i = 0; i < TILE_SET_LENGTH; i++) {
            TileImage tile = this.tiles[i];
            if (tile == null) {
                this.tiles[i] = new TileImage();
            } else {
                Arrays.fill(tile.getTileData(), (byte) 0x00);
            }
        }
    }
}
