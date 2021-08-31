package me.afoolslove.metalmaxre.editor.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 地图的调查点
 *
 * @author AFoolLove
 */
public class MapCheckPoints {
    /**
     * 巨炮阵地前的隐藏入口<p>
     * 作用：调查后根据选项是否进入到目标坐标<p>
     * K：inPoint<p>
     * V：outPoint
     */
    public MapCheckPoint<MapPoint, MapPoint> entrance = new MapCheckPoint<>(new MapPoint(), new MapPoint());

    /**
     * 塔玛之墓<p>
     * 作用：调查后，显示一段文本（* 这里无法更改
     */
    public MapCheckPoint<MapPoint, Object> text = new MapCheckPoint<>(new MapPoint());

    /**
     * 复活胶囊<p>
     * 作用：调查后，根据复活胶囊的获取条件是否获取得到物品
     */
    public MapCheckPoint<MapPoint, Byte> reviveCapsule = new MapCheckPoint<>(new MapPoint(), (byte) 0xB1);

    /**
     * 塔镇的乌鲁米井<p>
     * 作用：调查后，提示是否喝下，喝下后满血和消除所有异常
     */
    public MapCheckPoint<MapPoint, Object> urumi = new MapCheckPoint<>(new MapPoint());

    /**
     * 家里的抽屉（衣柜，字库中没有"抽屉"）<p>
     * 作用：调查后，随机获取两种物品中的一个
     */
    public MapCheckPoint<MapPoint, List<Byte>> drawers = new MapCheckPoint<>(new MapPoint(), new ArrayList<>(0x02));

    /**
     * 妈妈的相片<p>
     * 作用：调查后，显示两段文本（* 这里无法更改文本
     */
    public MapCheckPoint<MapPoint, Object> text2 = new MapCheckPoint<>(new MapPoint());

    public static class MapCheckPoint<K, V> implements Map.Entry<K, V> {
        K key;
        V value;

        public MapCheckPoint(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public MapCheckPoint(K key) {
            this.key = key;
            this.value = null;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V v = this.value;
            this.value = value;
            return v;
        }
    }
}
