package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.palette.IPaletteEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 地图属性编辑器
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
 * 支持世界地图的部分属性 {@link WorldMapProperties}
 * <p>
 * 地图属性索引不需要建议被编辑！！所以不提供修改功能！！
 *
 * @author AFoolLove
 */
public class MapPropertiesEditorImpl extends RomBufferWrapperAbstractEditor implements IMapPropertiesEditor {
    private final DataAddress mapPropertiesIndexUpRollAddress;
    private final DataAddress mapPropertiesIndexDownRollAddress;
    private final DataAddress mapPropertiesAddress;
    private final DataAddress mapPropertiesRedirectAddress;
    private final DataAddress mapPropertiesMonsterGroupIndexAddress;


    private final LinkedHashMap<Integer, MapProperties> mapProperties = new LinkedHashMap<>();

    public MapPropertiesEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x0BE10 - 0x10, 0x0BE8F - 0x10),
                DataAddress.fromPRG(0x1DEB0 - 0x10, 0x1E010 - 0x10),
                DataAddress.fromPRG(0x0E510 - 0x10, 0x0FBBD - 0x10),
                DataAddress.fromPRG(0x28707 - 0x10, 0x2870F - 0x10),
                DataAddress.fromPRG(0x39343 - 0x10, 0x393B2 - 0x10));
    }

    public MapPropertiesEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                   @NotNull DataAddress mapPropertiesIndexUpRollAddress,
                                   @NotNull DataAddress mapPropertiesIndexDownRollAddress,
                                   @NotNull DataAddress mapPropertiesAddress,
                                   @NotNull DataAddress mapPropertiesRedirectAddress,
                                   @NotNull DataAddress mapPropertiesMonsterGroupIndexAddress) {
        super(metalMaxRe);
        this.mapPropertiesIndexUpRollAddress = mapPropertiesIndexUpRollAddress;
        this.mapPropertiesIndexDownRollAddress = mapPropertiesIndexDownRollAddress;
        this.mapPropertiesAddress = mapPropertiesAddress;
        this.mapPropertiesRedirectAddress = mapPropertiesRedirectAddress;
        this.mapPropertiesMonsterGroupIndexAddress = mapPropertiesMonsterGroupIndexAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getMapProperties().clear();


        // 读取世界地图属性
        byte[] properties = new byte[MapProperties.PROPERTIES_MAX_LENGTH];
        // 读取宽度、高度，因为在代码中使用同一个数据赋值，所以宽高只能一样
        properties[0x01] = getBuffer().getPrg(0x28A87 - 0x10);
        properties[0x02] = properties[0x01];
        // 读取可移动区域偏移量，同上
        properties[0x03] = getBuffer().getPrg(0x28A8D - 0x10);
        properties[0x04] = properties[0x03];
        // 读取可移动区域，同上
        properties[0x05] = getBuffer().getPrg(0x28A95 - 0x10);
        properties[0x06] = properties[0x05];
        // 世界地图的出入口初始地址
        properties[0x0B] = (byte) 0x80;
        properties[0x0C] = (byte) 0x89;
        // 读取事件图块索引
        properties[0x1A] = getBuffer().getPrg(0x28AC3 - 0x10);
        properties[0x1B] = getBuffer().getPrg(0x28AC7 - 0x10);

        // 添加世界地图属性
        getMapProperties().put(0x00, new WorldMapProperties(properties));

        // 读取地图属性索引，上卷 0x40、下卷 0xB0
        // 地图属性索引不需要建议被编辑！！所以不提供修改功能！！
        char[] mapIndexRoll = new char[0x40 + 0xB0];
        position(getMapPropertiesIndexUpRollAddress());
        for (int i = 0; i < 0x40; i++) {
            mapIndexRoll[i] = getBuffer().getChar();
        }
        position(getMapPropertiesIndexDownRollAddress());
        for (int i = 0; i < 0xB0; i++) {
            mapIndexRoll[0x40 + i] = getBuffer().getChar();
        }

        // 通过地图属性索引读取地图属性
        // MapID从1开始，0是世界地图
        final int mapPropertiesOffset = getMapPropertiesAddress().getStartAddress() - 0x08500;
        for (int mapId = 1; mapId < mapIndexRoll.length; mapId++) {
            char index = mapIndexRoll[mapId];
            prgPosition(mapPropertiesOffset + index);
            // 获取基本属性，将两个不确定属性排除
            getBuffer().get(properties, 0, MapProperties.PROPERTIES_MAX_LENGTH - 2 - 2);
            if (MapProperties.hasDyTile(properties[0])) {
                // 如果存在动态图块，读取属性
                properties[0x18] = getBuffer().get();
                properties[0x19] = getBuffer().get();
            }
            if (MapProperties.hasEventTile(properties[0])) {
                // 如果存在事件图块，读取属性
                properties[0x1A] = getBuffer().get();
                properties[0x1B] = getBuffer().get();
            }

            MapProperties mapProperties = new MapProperties(properties);
            if (mapId >= 0x80) {
                // 读取地图怪物组合索引
                position(getMapPropertiesMonsterGroupIndexAddress(), mapId - 0x80);
                mapProperties.monsterGroupIndex = getBuffer().get();
            }
            this.mapProperties.put(mapId, mapProperties);
        }

        // 读取重定向的条件数据和地图
        position(getMapPropertiesRedirectAddress());
        // data[0] = fromMaps
        // data[1] = data
        // data[2] = toMaps
        byte[][] redirectData = new byte[3][getMapPropertiesRedirectMaxCount()];

        getBuffer().getAABytes(0, getMapPropertiesRedirectMaxCount(), redirectData);

        for (int index = 0; index < getMapPropertiesRedirectMaxCount(); index++) {
            MapProperties mapProperties = getMapProperties(redirectData[0][index]);
            mapProperties.redirect = SingleMapEntry.create(redirectData[1][index], redirectData[2][index]);
        }
    }

    @Editor.Apply
    public void onApply(IMapEditor mapEditor,
                        IPaletteEditor paletteEditor,
                        @Editor.QuoteOnly IEventTilesEditor eventTilesEditor,
                        @Editor.QuoteOnly MapEntranceEditorImpl mapEntranceEditor) {
        // 写入地图属性索引，上卷 0x40、下卷 0xB0
        // 地图属性索引不建议被编辑！！所以不提供修改功能！！

        // 将地图属性整合为写入的格式，index=0无效
        byte[][] properties = new byte[mapEditor.getMapMaxCount() + 1][];
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

        position(getMapPropertiesIndexUpRollAddress());
        // 固定值，无任何作用？
        getBuffer().put((byte) 0x00);
        getBuffer().put((byte) 0x00);
//        mapIndexRoll[0x00] = 0x0000;

        char mapPropertiesIndex = 0x8500;
        for (int i = 0x01; i < 0x40; i++) {
            int hashCode = Arrays.hashCode(properties[i]);
            Character character = mapping.get(hashCode);
            if (character != null) {
                // 如果有相同的地图属性索引，则索引到同一个位置
//                mapIndexRoll[i] = character;
                getBuffer().putChar(NumberR.toChar(character));
            } else {
                // 如果是新的地图属性，就设置地图属性索引和写入地图属性
                mapping.put(hashCode, mapPropertiesIndex);
//                mapIndexRoll[i] = mapPropertiesIndex;
                getBuffer().putChar(NumberR.toChar(mapPropertiesIndex));
                mapPropertiesIndex += properties[i].length;
            }
        }

        position(getMapPropertiesIndexDownRollAddress());
        for (int i = 0x40; i < (0x40 + 0xB0); i++) {
            int hashCode = Arrays.hashCode(properties[i]);
            Character character = mapping.get(hashCode);
            if (character != null) {
                // 如果有相同的地图属性索引，则索引到同一个位置
//                mapIndexRoll[i] = character;
                getBuffer().putChar(NumberR.toChar(character));
            } else {
                // 如果是新的地图属性，就设置地图属性索引和写入地图属性
                mapping.put(hashCode, mapPropertiesIndex);
//                mapIndexRoll[i] = mapPropertiesIndex;
                getBuffer().putChar(NumberR.toChar(mapPropertiesIndex));
                mapPropertiesIndex += properties[i].length;
            }
        }

        // 写入重定向的条件数据和地图
        // data[0] = fromMaps
        // data[1] = data
        // data[2] = toMaps
        byte[][] redirectData = new byte[3][getMapPropertiesRedirectMaxCount()];

        for (int count = 0; ; ) {
            for (Map.Entry<Integer, MapProperties> entry : getMapProperties().entrySet()) {
                if (count >= getMapPropertiesRedirectMaxCount()) {
                    break;
                }
                SingleMapEntry<Byte, Byte> redirect = entry.getValue().redirect;
                if (redirect != null) {
                    redirectData[0][count] = (byte) (entry.getKey() & 0xFF);
                    redirectData[1][count] = redirect.getValue();
                    redirectData[2][count] = redirect.getKey();
                    count++;
                }
            }
            break;
        }
        position(getMapPropertiesRedirectAddress());
        getBuffer().putAABytes(0, getMapPropertiesRedirectMaxCount(), redirectData);

        // 写入地图怪物组合索引
        position(getMapPropertiesMonsterGroupIndexAddress());
        for (int mapId = 0x80; mapId < mapEditor.getMapMaxCount(); mapId++) {
            MapProperties mapProperties = this.mapProperties.get(mapId);
            getBuffer().put(mapProperties.monsterGroupIndex);
        }

        // 写入地图属性
        position(getMapPropertiesAddress());
        for (int i = 0x01; i < properties.length - 1; i++) {
            getBuffer().put(properties[i]);
        }

        // 写入世界地图属性
        MapProperties worldMapProperties = this.mapProperties.get(0x00);
        // 写入宽度、高度
        getBuffer().putPrg(0x28A87 - 0x10, worldMapProperties.width);
        // 写入可移动区域偏移量，同上
        getBuffer().putPrg(0x28A8D - 0x10, worldMapProperties.movableWidthOffset);
        // 写入可移动区域，同上
        getBuffer().putPrg(0x28A95 - 0x10, worldMapProperties.movableWidth);
        // 写入事件图块索引
        getBuffer().putPrg(0x28AC3 - 0x10, NumberR.at(worldMapProperties.eventTilesIndex, 0));
        getBuffer().putPrg(0x28AC7 - 0x10, NumberR.at(worldMapProperties.eventTilesIndex, 1));

        int end = position() - 1;
        if (end <= 0x0FBBD) {
            System.out.printf("地图属性编辑器：剩余%d个空闲字节\n", 0x0FBBD - end);
        } else {
            System.out.printf("地图属性编辑器：错误！超出了数据上限%d字节\n", end - 0x0FBBD);
        }
    }


    @Override
    public LinkedHashMap<Integer, MapProperties> getMapProperties() {
        return mapProperties;
    }

    @Override
    public MapProperties getMapProperties(int map) {
        return mapProperties.get(map & 0xFF);
    }

    @Override
    public WorldMapProperties getWorldMapProperties() {
        return ((WorldMapProperties) mapProperties.get(0x00));
    }

    @Override
    public DataAddress getMapPropertiesIndexUpRollAddress() {
        return mapPropertiesIndexUpRollAddress;
    }

    @Override
    public DataAddress getMapPropertiesIndexDownRollAddress() {
        return mapPropertiesIndexDownRollAddress;
    }

    @Override
    public DataAddress getMapPropertiesAddress() {
        return mapPropertiesAddress;
    }

    @Override
    public DataAddress getMapPropertiesRedirectAddress() {
        return mapPropertiesRedirectAddress;
    }

    @Override
    public DataAddress getMapPropertiesMonsterGroupIndexAddress() {
        return mapPropertiesMonsterGroupIndexAddress;
    }
}