package me.afoolslove.metalmaxre.editor.map.events;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

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

    private final HashMap<Integer, List<EventTile>> eventTiles = new HashMap<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 排除事件为 0x00 ！！！！
        buffer.position(0x1DCCF);

        // TODO read!!!!!
        int event = buffer.get();
        do {
            // 读取该事件的所有图块数量，不会真有人写入了数量为0的事件吧？？？
            int count = buffer.get() & 0xFF;
            ArrayList<EventTile> eventTiles = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                // 读取 X、Y、Tile
                eventTiles.add(new EventTile(buffer.get(), buffer.get(), buffer.get()));
            }

            getEventTiles().put(event, eventTiles);
        } while ((event = buffer.get()) == 0x00);


        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 排除事件为 0x00 ！！！！

        eventTiles.remove(0x00);
        Iterator<Map.Entry<Integer, List<EventTile>>> iterator = eventTiles.entrySet().iterator();

        return true;
    }

    public HashMap<Integer, List<EventTile>> getEventTiles() {
        return eventTiles;
    }
}
