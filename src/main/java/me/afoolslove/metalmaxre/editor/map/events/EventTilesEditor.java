package me.afoolslove.metalmaxre.editor.map.events;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.map.MapProperties;
import me.afoolslove.metalmaxre.editor.map.MapPropertiesEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件图块编辑器
 * 支持世界地图
 * 根据事件状态可以显示不同的图块，覆盖原图块
 * 注意尽量不要同时作用在同一个图块上，因为没测试会发生啥
 * <p>
 * 需要地图属性中的事件图块启用才会生效
 * 图块图像根据玩家当前的图块组合不同而不同
 * <p>
 * <p>
 * 世界地图：
 * 世界地图的总tile大小为0x100*0x100，其中每4*4为一个小块
 * 当世界地图使用时，单个tile数据控制此4*4的方块
 * 并且X、Y的计算方式变更为 X*4、Y*4，X、Y < 0x40
 * <p>
 * <p>
 * 2021年5月26日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class EventTilesEditor extends AbstractEditor {

    /**
     * K：original EventTilesIndex
     * V：
     * ----K：event
     * ----V：tile
     */
    private final HashMap<Character, Map<Integer, List<EventTile>>> eventTiles = new HashMap<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 排除事件为 0x00 ！！！！
        // buffer.position(0x1DCCF);

        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);


        var map = new HashMap<>(mapPropertiesEditor.getMapProperties())
                .entrySet().stream().parallel()
                .filter(entry -> entry.getValue().hasEventTile()) // 移除没有事件图块属性的地图
                .collect(
                        // 移除相同的事件图块数据索引
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing((o) -> o.getValue().eventTilesIndex)))
                );

        for (Map.Entry<Integer, MapProperties> mapPropertiesEntry : map) {
            char eventTilesIndex = mapPropertiesEntry.getValue().eventTilesIndex;
            buffer.position(0x10 + 0x1C000 + eventTilesIndex - 0x8000);

            // 一个或多个事件作为一组，一组使用 0x00 作为结尾
            var events = new HashMap<Integer, List<EventTile>>();
            // 事件
            int event = buffer.get();
            do {
                // 图块数量
                int count = buffer.get();

                List<EventTile> eventTiles = new ArrayList<>();
                // 读取事件图块：X、Y、图块
                for (int i = count; i > 0; i--) {
                    eventTiles.add(new EventTile(buffer.get(), buffer.get(), buffer.get()));
                }
                events.put(event, eventTiles);
            } while ((event = buffer.get()) != 0x00);

            // 添加事件图块组
            getEventTiles().put(eventTilesIndex, events);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);

        // 排除事件为 0x00 ！！！！
        getEventTiles().values().forEach(each -> {
            each.entrySet().removeIf(entry -> entry.getKey() == 0x00);
        });

        buffer.position(0x1DCCF);

        // 需要修改key，所以新起一个map
        for (Map.Entry<Character, Map<Integer, List<EventTile>>> e : new HashMap<>(getEventTiles()).entrySet()) {
            // 原始事件图块索引
            Character original = e.getKey();
            // 事件组
            Map<Integer, List<EventTile>> events = e.getValue();

            // 计算新的事件图块索引，太长了！简称：索引
            char newEventTilesIndex = (char) (buffer.position() - 0x10 - 0x1C000 + 0x8000);
            // 将旧的索引替换为新的索引
            for (var entry : mapPropertiesEditor.getMapProperties().entrySet()) {
                if (entry.getValue().eventTilesIndex == original) {
                    // 替换Key
                    // 移除旧的索引数据并添加到新的索引中
                    getEventTiles().put(newEventTilesIndex, getEventTiles().remove(original));
                    entry.getValue().eventTilesIndex = newEventTilesIndex;
                }
            }
            // 写入数据
            for (Map.Entry<Integer, List<EventTile>> entry : events.entrySet()) {
                // 写入事件
                buffer.put(entry.getKey().byteValue());
                // 写入事件数量
                buffer.put(((byte) entry.getValue().size()));
                // 写入 X、Y、Tile
                for (EventTile eventTile : entry.getValue()) {
                    buffer.put(eventTile.x);
                    buffer.put(eventTile.y);
                    buffer.put(eventTile.tile);
                }
            }
            // 写入事件组结束符
            buffer.put((byte) 0x00);
        }

        return true;
    }

    public HashMap<Character, Map<Integer, List<EventTile>>> getEventTiles() {
        return eventTiles;
    }

    /**
     * @return 获取指定map的事件图块，可能为null，包含世界地图
     */
    public Map<Integer, List<EventTile>> getEventTile(int map) {
        MapPropertiesEditor editor = EditorManager.getEditor(MapPropertiesEditor.class);
        return eventTiles.get(editor.getMapProperties().get(map).eventTilesIndex);
    }
}
