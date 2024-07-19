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

    /**
     * 格式化山脉，将山脉ID0x02格式化为游戏中不同的0x02和0x03与0x0A、0x0B
     */
    static void formatPeck(byte[][] map) {
        if (map == null || map.length == 0x00) {
            return;
        }
        // 连续山脉的计数器
        int continuous = 0;
        int h = map.length;
        int w = map[0x00].length;
        for (int index = 0; index < (w * h); index++) {
            int x = index % w;
            int y = index / h;

//            int tile = map[y][x] & 0xFF;

            int offset = (y & 1) == 1 ? 0 : 1;

            // 在新行中，如果上一行的山脉未结束，立即结束并重置连续山脉计数器
            if (x == 0x00) {
                if (continuous > 1) {
                    // 上一行未结束，立即结束山脉
                    map[(index - 1) / h][(index - 1) % w] = 0x0B;
                }
                // 重置连续山脉计数器
                continuous = 0x00;
            }

            // 获取当前图块是否为山脉
            // 如果山脉刚开始计数，设置山脉起始
            // 否则判断山脉是否结束，设置山脉结束
            if (map[y][x] == 0x02) {
                if (continuous == 0x00) {
                    // 设置山脉起始
                    map[y][x] = 0x0A;
                } else {
                    map[y][x] = (byte) (0x02 + ((x + offset) % 2));
                }
                continuous++;
            } else {
                if (continuous <= 1) {
                    continue;
                }
                // 设置山脉结束
                map[(index - 1) / h][(index - 1) % w] = 0x0B;
                continuous = 0;
            }
        }
    }

    /**
     * 恢复山脉，将图块ID 0x03、0x0A和0x0B替换为0x02
     */
    static void formatPeckRestore(byte[][] map) {
        for (byte[] mapRow : map) {
            for (int column = 0; column < mapRow.length; column++) {
                switch (column) {
                    case 0x03:
                    case 0x0A:
                    case 0x0B:
                        mapRow[column] = 0x02;
                        break;
                }
            }
        }
    }
}
