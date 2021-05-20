package me.afoolslove.metalmaxre.editor.map;

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
    public int count;

    public MapTile(int tile, int count) {
        this.tile = (byte) tile;
        this.count = count;
    }

    public MapTile(int tile) {
        this.tile = (byte) tile;
        this.count = 1;
    }

    public byte getTile() {
        return tile;
    }

    public void setTile(byte tile) {
        this.tile = tile;
    }

    /**
     * @return tile的数量
     */
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("tile=%02X count=%03d}", tile, count);
    }
}
