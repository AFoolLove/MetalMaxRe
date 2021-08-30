package me.afoolslove.metalmaxre.editor.map;

import me.afoolslove.metalmaxre.NumberR;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 地图属性编辑器
 * <p>
 * 起始：0x0E510
 * 结束：0x0FBBD
 * <p>
 * 0x01 byte:特殊属性：地下地图、事件图块、传送带动画速度？<p>
 * 0x02 byte:地图宽度、地图高度：宽 * 高 不能超过 0x1000(4096)，超出后会死机<p>
 * 0x02 byte:可移动的宽度偏移量、可以移动的高度偏移量：玩家只能在该范围内移动，否则算入离开地图，无宽高限制<p>
 * 0x02 byte:可移动的宽度、可以移动的高度：玩家只能在该范围内移动，否则算入离开地图，无宽高限制<p>
 * 0x02 byte:组成地图的数据的索引<p>
 * 0x01 byte:图块组合A<p>
 * 0x01 byte:图块组合B<p>
 * 0x02 byte:边界和入口目的地<p>
 * 0x02 byte:调色盘<p>
 * 0x01 byte:精灵表<p>
 * 0x04 byte:图块表<p>
 * 0x01 byte:门后图块，玩家开门后门替换的图块，图块图像受图块组合影响<p>
 * 0x01 byte:边界填充图块，图块图像受图块组合影响<p>
 * 0x01 byte:背景音乐<p>
 * 0x02 byte:事件图块索引<p>
 * <p>
 * 支持世界地图的部分属性 {@link WorldMapProperties}<p>
 * <p>
 * 地图属性索引不需要建议被编辑！！所以不提供修改功能！！<p>
 * 2021年5月14日：已完成并通过测试基本编辑功能<p>
 *
 * @author AFoolLove<p>
 */
public class MapPropertiesEditor extends AbstractEditor<MapPropertiesEditor> {
    /**
     * 地图属性索引上卷和下卷地址
     */
    public static final int MAP_PROPERTIES_UP_ROLL_OFFSET = 0x0BE10 - 0x10;
    public static final int MAP_PROPERTIES_DOWN_ROLL_OFFSET = 0x1DEB0 - 0x10;

    /**
     * 地图属性数据
     */
    public static final int MAP_PROPERTIES_OFFSET = 0x0E510 - 0x10;

    /**
     * 地图属性
     */
    public static final int MAP_PROPERTIES_START_OFFSET = MAP_PROPERTIES_OFFSET - 0x08500;
    public static final int MAP_PROPERTIES_END_OFFSET = 0x0FBBD - 0x08500 - 0x10;

    /**
     * 地图怪物组合索引
     */
    public static final int MAP_PROPERTIES_MONSTER_GROUP_INDEX_START_OFFSET = 0x39343 - 0x10;
    public static final int MAP_PROPERTIES_MONSTER_GROUP_INDEX_END_OFFSET = 0x393B2 - 0x10;


    private final LinkedHashMap<Integer, MapProperties> mapProperties = new LinkedHashMap<>(MapEditor.MAP_MAX_COUNT);


    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        mapProperties.clear();


        // 读取世界地图属性
        byte[] properties = new byte[MapProperties.PROPERTIES_MAX_LENGTH];
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

        // 读取地图属性索引，上卷 0x40、下卷 0xB0
        // 地图属性索引不需要建议被编辑！！所以不提供修改功能！！
        char[] mapIndexRoll = new char[0x40 + 0xB0];
        setPrgRomPosition(MAP_PROPERTIES_UP_ROLL_OFFSET);
        for (int i = 0; i < 0x40; i++) {
            mapIndexRoll[i] = (char) NumberR.toInt(get(buffer), get(buffer));
        }
        setPrgRomPosition(MAP_PROPERTIES_DOWN_ROLL_OFFSET);
        for (int i = 0; i < 0xB0; i++) {
            mapIndexRoll[0x40 + i] = (char) NumberR.toInt(get(buffer), get(buffer));
        }

        // 通过地图属性索引读取地图属性
        // MapID从1开始，0是世界地图
        for (int mapId = 1; mapId < mapIndexRoll.length; mapId++) {
            char index = mapIndexRoll[mapId];
            setPrgRomPosition(MAP_PROPERTIES_START_OFFSET + index);
            // 获取基本属性，将两个不确定属性排除
            get(buffer, properties, 0, MapProperties.PROPERTIES_MAX_LENGTH - 2 - 2);
            if (MapProperties.hasDyTile(properties[0])) {
                // 如果存在动态图块，读取属性
                properties[0x18] = get(buffer);
                properties[0x19] = get(buffer);
            }
            if (MapProperties.hasEventTile(properties[0])) {
                // 如果存在事件图块，读取属性
                properties[0x1A] = get(buffer);
                properties[0x1B] = get(buffer);
            }

            MapProperties mapProperties = new MapProperties(properties);
            if (mapId >= 0x80) {
                // 读取地图怪物组合索引
                setPrgRomPosition(MAP_PROPERTIES_MONSTER_GROUP_INDEX_START_OFFSET + (mapId - 0x80));
                mapProperties.monsterGroupIndex = get(buffer);
            }
            this.mapProperties.put(mapId, mapProperties);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 写入地图属性索引，上卷 0x40、下卷 0xB0
        // 地图属性索引不建议被编辑！！所以不提供修改功能！！

        // 将地图属性整合为写入的格式，index=0无效
        byte[][] properties = new byte[MapEditor.MAP_MAX_COUNT + 1][];
        for (Map.Entry<Integer, MapProperties> entry : mapProperties.entrySet()) {
            // 排除世界地图
            if ((!(entry.getValue() instanceof WorldMapProperties))) {
                properties[entry.getKey()] = entry.getValue().toByteArray();
            }
        }

        // K: properties hashCode
        // V: propertiesIndex
        // 通过hash判断地图属性是否重复，根据已重复的地图进行索引到已有的数据
        Map<Integer, Character> mapping = new HashMap<>();
//        char[] mapIndexRoll = new char[0x40 + 0xB0];

        setPrgRomPosition(MAP_PROPERTIES_UP_ROLL_OFFSET);
        // 固定值，无任何作用？
        put(buffer, 0x00);
        put(buffer, 0x00);
//        mapIndexRoll[0x00] = 0x0000;

        char mapPropertiesIndex = 0x8500;
        for (int i = 0x01; i < 0x40; i++) {
            int hashCode = Arrays.hashCode(properties[i]);
            Character character = mapping.get(hashCode);
            if (character != null) {
                // 如果有相同的地图属性索引，则索引到同一个位置
//                mapIndexRoll[i] = character;
                putChar(buffer, NumberR.toChar(character));
            } else {
                // 如果是新的地图属性，就设置地图属性索引和写入地图属性
                mapping.put(hashCode, mapPropertiesIndex);
//                mapIndexRoll[i] = mapPropertiesIndex;
                putChar(buffer, NumberR.toChar(mapPropertiesIndex));
                mapPropertiesIndex += properties[i].length;
            }
        }

        setPrgRomPosition(MAP_PROPERTIES_DOWN_ROLL_OFFSET);
        for (int i = 0x40; i < (0x40 + 0xB0); i++) {
            int hashCode = Arrays.hashCode(properties[i]);
            Character character = mapping.get(hashCode);
            if (character != null) {
                // 如果有相同的地图属性索引，则索引到同一个位置
//                mapIndexRoll[i] = character;
                putChar(buffer, NumberR.toChar(character));
            } else {
                // 如果是新的地图属性，就设置地图属性索引和写入地图属性
                mapping.put(hashCode, mapPropertiesIndex);
//                mapIndexRoll[i] = mapPropertiesIndex;
                putChar(buffer, NumberR.toChar(mapPropertiesIndex));
                mapPropertiesIndex += properties[i].length;
            }
        }

        // 写入地图怪物组合索引
        setPrgRomPosition(MAP_PROPERTIES_MONSTER_GROUP_INDEX_START_OFFSET);
        for (int mapId = 0x80; mapId < MapEditor.MAP_MAX_COUNT; mapId++) {
            MapProperties mapProperties = this.mapProperties.get(mapId);
            put(buffer, mapProperties.monsterGroupIndex);
        }

        // 写入地图属性
        setPrgRomPosition(MAP_PROPERTIES_OFFSET);
        for (int i = 0x01; i < properties.length - 1; i++) {
            put(buffer, properties[i]);
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
        buffer.put(0x28AC3, NumberR.at(worldMapProperties.eventTilesIndex, 0));
        buffer.put(0x28AC7, NumberR.at(worldMapProperties.eventTilesIndex, 1));


        int end = bufferPosition - 1;
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

    /**
     * @return 世界地图的地图属性
     */
    public WorldMapProperties getWorldMapProperties() {
        return ((WorldMapProperties) mapProperties.get(0x00));
    }

}
