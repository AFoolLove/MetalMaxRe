package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.NumberR;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * 地图属性编辑器
 * <p>
 * 起始：0x0E510
 * 结束：0x0FBBD
 * <p>
 * 0x01 byte:特殊属性：地下地图、事件图块、传送带动画速度？
 * 0x02 byte:地图宽度、地图高度：宽 * 高 不能超过 0x1000(4096)，超出后会死机
 * 0x02 byte:可移动的宽度偏移量、可以移动的高度偏移量：玩家只能在该范围内移动，否则算入离开地图，无宽高限制
 * 0x02 byte:可移动的宽度、可以移动的高度：玩家只能在该范围内移动，否则算入离开地图，无宽高限制
 * 0x02 byte:组成地图的数据的索引
 * 0x01 byte:图块组合A
 * 0x01 byte:图块组合B
 * 0x02 byte:边界和入口目的地
 * 0x02 byte:调色盘
 * 0x01 byte:精灵表
 * 0x04 byte:图块表
 * 0x01 byte:门后图块，玩家开门后门替换的图块，图块图像受图块组合影响
 * 0x01 byte:边界填充图块，图块图像受图块组合影响
 * 0x01 byte:背景音乐
 * 0x02 byte:事件图块索引
 * <p>
 * <p>
 * 支持世界地图的部分属性 {@link WorldMapProperties}
 * <p>
 * <p>
 * <p>
 * 地图属性索引不需要建议被编辑！！所以不提供修改功能！！
 * <p>
 * 2021年5月14日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class MapPropertiesEditor extends AbstractEditor {

    private final LinkedHashMap<Integer, MapProperties> mapProperties = new LinkedHashMap<>(MapEditor.MAP_MAX_COUNT);

    public static void main(String[] args) throws IOException {
        HashMap<Integer, Character> map = new HashMap<>();
        map.put(Objects.hash(0, 1), (char) 5);

        Character character = map.get(Objects.hash(0, 1));
        System.out.println(character);
    }


    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取地图属性索引，上卷 0x40、下卷 0xB0
        // 地图属性索引不需要建议被编辑！！所以不提供修改功能！！
        char[] mapIndexRoll = new char[0x40 + 0xB0];
        buffer.position(0x0BE10);
        for (int i = 0; i < 0x40; i++) {
            mapIndexRoll[i] = (char) ((buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8));
        }
        buffer.position(0x1DEB0);
        for (int i = 0x40; i < mapIndexRoll.length; i++) {
            mapIndexRoll[i] = (char) ((buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8));
        }

        // 通过地图属性索引读取地图属性
        buffer.position(0x06010);
        buffer.mark();
        byte[] properties = new byte[MapProperties.PROPERTIES_MAX_LENGTH];
        for (char index : mapIndexRoll) {
            buffer.reset();
            buffer.position(buffer.position() + index);
            // 获取基本属性，将两个不确定属性排除
            buffer.get(properties, 0, MapProperties.PROPERTIES_MAX_LENGTH - 2 - 2);
            if (MapProperties.hasDyTile(properties[0])) {
                // 如果存在动态图块，读取属性
                properties[0x18] = buffer.get();
                properties[0x19] = buffer.get();
            }
            if (MapProperties.hasEventTile(properties[0])) {
                // 如果存在事件图块，读取属性
                properties[0x1A] = buffer.get();
                properties[0x1B] = buffer.get();
            }
            mapProperties.put(mapProperties.size(), new MapProperties(properties));
        }

        // 移除世界地图（无效数据
        mapProperties.remove(0x00);

        // 读取世界地图属性
        Arrays.fill(properties, (byte) 0x00);
        // 读取宽度、高度，因为在代码中使用同一个数据赋值，所以宽高只能一样
        properties[0x01] = buffer.get(0x28A87);
        properties[0x02] = properties[0x01];
        // 读取可移动区域偏移量，同上
        properties[0x03] = buffer.get(0x28A8D);
        properties[0x04] = properties[0x03];
        // 读取可移动区域，同上
        properties[0x05] = buffer.get(0x28A95);
        properties[0x06] = properties[0x05];
        // 世界地图的出入口初始地址
        properties[0x0B] = (byte) 0x80;
        properties[0x0C] = (byte) 0x89;
        // 读取事件图块索引
        properties[0x1A] = buffer.get(0x28AC3);
        properties[0x1B] = buffer.get(0x28AC7);

        // 添加世界地图属性
        mapProperties.put(0x00, new WorldMapProperties(properties));
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 写入地图属性索引，上卷 0x40、下卷 0xB0
        // 地图属性索引不需要建议被编辑！！所以不提供修改功能！！

        // 将地图属性整合为写入的格式，index=0无效
        byte[][] properties = new byte[MapEditor.MAP_MAX_COUNT + 1][];
        for (Map.Entry<Integer, MapProperties> entry : mapProperties.entrySet()) {
            // 排除世界地图
            if ((!(entry.getValue() instanceof WorldMapProperties))) {
                properties[entry.getKey()] = entry.getValue().toArray();
            }
        }

        // K: properties hashCode
        // V: propertiesIndex
        // 通过hash判断地图属性是否重复，根据已重复的地图进行索引到已有的数据
        Map<Integer, Character> mapping = new HashMap<>();
//        char[] mapIndexRoll = new char[0x40 + 0xB0];

        buffer.position(0x0BE10);
        // 固定值，无任何作用？
        buffer.put((byte) 0x00);
        buffer.put((byte) 0x00);
//        mapIndexRoll[0x00] = 0x0000;

        char mapPropertiesIndex = 0x8500;
        for (int i = 0x01; i < 0x40; i++) {
            int hashCode = Arrays.hashCode(properties[i]);
            Character character = mapping.get(hashCode);
            if (character != null) {
                // 如果有相同的地图属性索引，则索引到同一个位置
//                mapIndexRoll[i] = character;
                buffer.putChar(NumberR.parseChar(character));
            } else {
                // 如果是新的地图属性，就设置地图属性索引和写入地图属性
                mapping.put(hashCode, mapPropertiesIndex);
//                mapIndexRoll[i] = mapPropertiesIndex;
                buffer.putChar(NumberR.parseChar(mapPropertiesIndex));
                mapPropertiesIndex += properties[i].length;
            }
        }

        buffer.position(0x1DEB0);
        for (int i = 0x40; i < (0x40 + 0xB0); i++) {
            int hashCode = Arrays.hashCode(properties[i]);
            Character character = mapping.get(hashCode);
            if (character != null) {
                // 如果有相同的地图属性索引，则索引到同一个位置
//                mapIndexRoll[i] = character;
                buffer.putChar(NumberR.parseChar(character));
            } else {
                // 如果是新的地图属性，就设置地图属性索引和写入地图属性
                mapping.put(hashCode, mapPropertiesIndex);
//                mapIndexRoll[i] = mapPropertiesIndex;
                buffer.putChar(NumberR.parseChar(mapPropertiesIndex));
                mapPropertiesIndex += properties[i].length;
            }
        }

        // 写入地图属性
        buffer.position(0x0E510);
        for (int i = 0x01; i < properties.length - 1; i++) {
            buffer.put(properties[i]);
        }

        // 写入世界地图属性
        MapProperties worldMapProperties = this.mapProperties.get(0x00);
        // 写入宽度、高度
        buffer.put(0x28A87, worldMapProperties.width);
        // 写入可移动区域偏移量，同上
        buffer.put(0x28A8D, worldMapProperties.movableWidthOffset);
        // 写入可移动区域，同上
        buffer.put(0x28A95, worldMapProperties.movableWidth);
        // 写入事件图块索引
        buffer.put(0x28AC3, (byte) (worldMapProperties.eventTilesIndex & 0x00FF));
        buffer.put(0x28AC7, (byte) ((worldMapProperties.eventTilesIndex & 0xFF00) >>> 8));


        int end = buffer.position() - 1;
        if (end <= 0x0FBBD) {
            System.out.printf("地图属性编辑器：剩余%d个空闲字节\n", 0x0FBBD - end);
        } else {
            System.out.printf("地图属性编辑器：错误！超出了数据上限%d字节\n", end - 0x0FBBD);
        }
        return true;
    }

    /**
     * @return 所有地图的地图属性
     */
    public LinkedHashMap<Integer, MapProperties> getMapProperties() {
        return mapProperties;
    }

    /**
     * @return 指定地图的地图属性
     */
    public MapProperties getMapProperties(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map) {
        return mapProperties.get(map);
    }

}
