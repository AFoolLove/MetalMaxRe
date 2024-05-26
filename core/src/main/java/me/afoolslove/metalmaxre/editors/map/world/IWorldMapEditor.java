package me.afoolslove.metalmaxre.editors.map.world;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;

public interface IWorldMapEditor extends IRomEditor {
    @Override
    default String getId() {
        return "worldMapEditor";
    }

    /**
     * index与indexOffsets的比例是 1byte：2bit
     */
    byte[] getIndexOffsets();

    byte getIndexOffset(int index);

    /**
     * index与x00410的比例是 8byte：1bit
     */
    byte[] getX00410();

    byte getX00410(int index);

    byte[] getIndex(int x, int y, int offset);

    byte[] getIndex();

    byte[][][] getIndexes();

    byte getIndex(int index);

    byte[][] getMap();

    /**
     * 获取图块索引组的容量
     */
    default int[] getIndexesCapacity() {
        return getWorldMapIndexesAddress().stream().mapToInt(m -> m.getStartAddress() == (7 * 0x2000) ? 0x50 : 0x200).toArray();
    }

    /**
     * @return 地雷坐标
     */
    List<MapPoint> getMines();

    /**
     * 世界地图图块索引偏移地址
     * <p>
     * 0x01 = 0x1000 byte
     */
    DataAddress getWorldMapTilesIndexAddress();

    /**
     * 图块组索引地址<p>
     * 0B0101_0101<p>
     * 0(bit)：使用图块组A<p>
     * 1(bit)：使用图块组B
     */
    DataAddress getWorldMapX00410Address();

    /**
     * 图块组，一组16（byte）个图块
     */
    List<DataAddress> getWorldMapIndexesAddress();

    /**
     * 相对图块索引
     * <p>
     * CHR ROM
     */
    DataAddress getWorldMapIndexAddress();

    /**
     * 地雷坐标起始 4x + 4y
     */
    DataAddress getWorldMapMinesAddress();

    /**
     * 将0x10个byte以4*4的顺序写入map
     * e.g：
     * 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
     * to：
     * 0,1,2,3
     * 4,5,6,7
     * 8,9,10,11
     * 12,13,14,15
     */
    static void map0(byte[][] map, int x, int y, byte[] bytes) {
        x *= 4;
        y *= 4;
        for (int offsetY = 0; offsetY < 4; offsetY++) {
            System.arraycopy(bytes, offsetY * 4 + 0, map[y + offsetY], 0 + x, 4);
        }
    }

    /**
     * 将4*4的数据组合为0x10的数据
     * e.g：
     * 0,1,2,3
     * 4,5,6,7
     * 8,9,10,11
     * 12,13,14,15
     * to：
     * 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
     */
    static byte[] getTiles4x4(byte[][] map, int x, int y) {
        x *= 4;
        y *= 4;
        byte[] bytes = new byte[0x10];
        for (int offsetY = 0; offsetY < 4; offsetY++) {
            System.arraycopy(map[y + offsetY], x + 0, bytes, (offsetY * 4) + 0, 4);
        }
        return bytes;
    }
}
