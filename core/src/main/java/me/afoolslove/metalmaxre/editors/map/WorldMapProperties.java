package me.afoolslove.metalmaxre.editors.map;

/**
 * 世界地图属性
 * <p>
 * 只能修改以下属性
 * 地图的宽度、高度。不建议修改！！！
 * 地图的可移动区域偏移量
 * 地图的可移动区域
 * 事件图块索引
 * 入口
 * <p>
 * 其它属性无效
 *
 * @author AFoolLove
 */
public class WorldMapProperties extends MapProperties {

    public WorldMapProperties(byte[] properties) {
        super(properties);
    }

    @Override
    public int getIntSpriteTiles() {
        return 0x95940504;
    }

    /**
     * @return 世界地图一定拥有事件图块
     */
    @Override
    public boolean hasEventTile() {
        return true;
    }

    /**
     * @return 不确定呢，不要使用
     */
    @Deprecated
    @Override
    public boolean hasBeltConveyor() {
        return super.hasBeltConveyor();
    }

    /**
     * @return {@code null}，世界地图的属性不能转换为普通地图的数据格式
     */
    @Override
    public byte[] toByteArray() {
        return null;
    }
}
