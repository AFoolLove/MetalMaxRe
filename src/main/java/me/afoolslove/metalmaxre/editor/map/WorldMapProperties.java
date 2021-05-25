package me.afoolslove.metalmaxre.editor.map;

/**
 * 世界地图属性
 * <p>
 * 只能修改以下属性
 * 地图的宽度、高度。不建议修改！！！
 * 地图的可移动区域偏移量
 * 地图的可移动区域
 * 事件图块索引
 * <p>
 * 其它属性无效
 *
 * @author AFoolLove
 */
public class WorldMapProperties extends MapProperties {

    public WorldMapProperties(byte[] properties) {
        super(properties);
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
    public boolean hasDyTile() {
        return super.hasDyTile();
    }
}
