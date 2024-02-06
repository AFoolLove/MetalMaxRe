package me.afoolslove.metalmaxre.editors.map.events;

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

    /**
     * 判断坐标是否在事件地图中
     * <p>
     * 参数坐标范围为00-FF，自动转换为00-3F
     *
     * @param x 给定的横坐标
     * @param y 给定的纵坐标
     * @return 是否在事件地图中
     */
    @Override
    public boolean contains(int x, int y) {
        return super.contains(x / 4, y / 4);
    }
}
