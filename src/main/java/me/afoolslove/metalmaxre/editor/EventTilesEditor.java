package me.afoolslove.metalmaxre.editor;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * 事件图块编辑器
 * 根据事件状态可以显示不同的图块，覆盖原图块
 * 注意尽量不要同时作用在同一个图块上，因为没测试会发生啥
 * <p>
 * 需要地图属性中的事件图块启用才会生效
 * 图块图像根据玩家当前的图块组合不同而不同
 * 不包含世界地图
 *
 * @author AFoolLove
 */
public class EventTilesEditor extends AbstractEditor {

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {

        return false;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        return false;
    }
}
