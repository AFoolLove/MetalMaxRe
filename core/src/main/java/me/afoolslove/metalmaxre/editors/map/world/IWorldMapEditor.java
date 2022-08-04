package me.afoolslove.metalmaxre.editors.map.world;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.map.CameraMapPoint;
import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;
import java.util.Map;

public interface IWorldMapEditor extends IRomEditor {
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

    byte[][] getIndexA();

    byte[] getIndexA(int index);

    byte[] getIndexA(int offset, boolean canOut);

    byte[][] getIndexB();

    byte[] getIndexB(int index);

    byte[][] getIndexC();

    byte[] getIndexC(int index);

    byte[] getIndex();

    byte getIndex(int index);

    byte[][] getMap();

    /**
     * 航线最大路径点
     */
    default int getWorldMapLineMaxCount() {
        return 0x10;
    }

    /**
     * @return 地雷坐标
     */
    List<MapPoint> getMines();

    /**
     * @return 出航路径点和目的地
     */
    Map.Entry<List<MapPoint>, CameraMapPoint> getShippingLineOut();

    /**
     * @return 归航路径点和目的地
     */
    Map.Entry<List<MapPoint>, CameraMapPoint> getShippingLineBack();

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
     * 图块组A，一组16（byte）个图块
     */
    DataAddress getWorldMapIndexAAddress();

    /**
     * 图块组B，一组16（byte）个图块
     */
    DataAddress getWorldMapIndexBAddress();

    /**
     * 图块组C，一组16（byte）个图块
     */
    DataAddress getWorldMapIndexCAddress();

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
     * 出航航线起始
     */
    DataAddress getWorldMapOutLineAddress();

    /**
     * 归航航线起始
     */
    DataAddress getWorldMapBackLineAddress();


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
            for (int offsetX = 0; offsetX < 4; offsetX++) {
                map[y + offsetY][offsetX + x] = bytes[offsetY * 4 + offsetX];
            }
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
            for (int offsetX = 0; offsetX < 4; offsetX++) {
                bytes[(offsetY * 4) + offsetX] = map[y + offsetY][x + offsetX];
            }
        }
        return bytes;
    }
}
