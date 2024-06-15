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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
 * 地图属性索引不需要且不建议被编辑！！所以不提供修改功能！！
 *
 * @author AFoolLove
 */
@Editor.TargetVersions
public class MapPropertiesEditorImpl extends RomBufferWrapperAbstractEditor implements IMapPropertiesEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapPropertiesEditorImpl.class);
    private final DataAddress mapPropertiesIndexUpRollAddress;
    private final DataAddress mapPropertiesIndexDownRollAddress;
    private final DataAddress mapPropertiesAddress;
    private final DataAddress mapPropertiesRedirectAddress;
    private final DataAddress mapPropertiesMonsterRealmIndexAddress;
    private final DataAddress mapCustomWantedAddress;


    private final LinkedHashMap<Integer, MapProperties> mapProperties = new LinkedHashMap<>();

    /**
     * Key: map id
     * Value：monsterId*2
     */
    public Map<Integer, Byte> customWanted = new HashMap<>(0x0D);

    public MapPropertiesEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x0BE10 - 0x10, 0x0BE8F - 0x10),
                DataAddress.fromPRG(0x1DEB0 - 0x10, 0x1E00F - 0x10),
                DataAddress.fromPRG(0x0E510 - 0x10, 0x0FBBD - 0x10),
                DataAddress.fromPRG(0x28707 - 0x10, 0x2870F - 0x10),
                DataAddress.fromPRG(0x39343 - 0x10, 0x393B2 - 0x10),
                DataAddress.fromPRG(0x39FF4 - 0x10, 0x3A000 - 0x10)
        );
    }

    public MapPropertiesEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                   @NotNull DataAddress mapPropertiesIndexUpRollAddress,
                                   @NotNull DataAddress mapPropertiesIndexDownRollAddress,
                                   @NotNull DataAddress mapPropertiesAddress,
                                   @NotNull DataAddress mapPropertiesRedirectAddress,
                                   @NotNull DataAddress mapPropertiesMonsterRealmIndexAddress,
                                   @NotNull DataAddress mapCustomWantedAddress
    ) {
        super(metalMaxRe);
        this.mapPropertiesIndexUpRollAddress = mapPropertiesIndexUpRollAddress;
        this.mapPropertiesIndexDownRollAddress = mapPropertiesIndexDownRollAddress;
        this.mapPropertiesAddress = mapPropertiesAddress;
        this.mapPropertiesRedirectAddress = mapPropertiesRedirectAddress;
        this.mapPropertiesMonsterRealmIndexAddress = mapPropertiesMonsterRealmIndexAddress;
        this.mapCustomWantedAddress = mapCustomWantedAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getMapProperties().clear();
        getCustomMapWanted().clear();


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
        properties[0x0B] = getBuffer().getPrg(0x28ABB - 0x10); // 默认 0x80
        properties[0x0C] = getBuffer().getPrg(0x28ABF - 0x10); // 默认 0x89
        // 读取事件图块索引
        properties[0x1A] = getBuffer().getPrg(0x28AC3 - 0x10);
        properties[0x1B] = getBuffer().getPrg(0x28AC7 - 0x10);

        // 添加世界地图属性
        getMapProperties().put(0x00, new WorldMapProperties(properties));

        // 读取地图属性索引，上卷 0x40、下卷 0xB0
        // 地图属性索引不需要并且不建议被编辑！！所以不提供修改功能！！
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
            if (MapProperties.hasBeltConveyor(properties[0])) {
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
                // 读取地图怪物领域索引
                position(getMapPropertiesMonsterRealmIndexAddress(), mapId - 0x80);
                mapProperties.monsterRealmIndex = getBuffer().get();
            }
            this.mapProperties.put(mapId, mapProperties);

//            System.out.format("%02X : %05X [", mapId, mapPropertiesOffset + index);
//            for (byte b : mapProperties.toByteArray()) {
//                System.out.format("%02X ", b);
//            }
//            System.out.println();
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

        // 读取自定义通缉令的地图和数据
        position(getCustomMapWantedAddress());
        byte[][] customWantedData = new byte[0x02][0x0D];
        getBuffer().getAABytes(0, 0x0D, customWantedData);
        for (int i = 0; i < 0x0D; i++) {
            getCustomMapWanted().put(customWantedData[0x00][i] & 0xFF, customWantedData[0x01][i]);
        }
    }

    @Editor.Apply
    public void onApply(IMapEditor mapEditor,
                        IPaletteEditor paletteEditor,
                        IEventTilesEditor eventTilesEditor,
                        IMapEntranceEditor mapEntranceEditor) {
        // 写入地图属性索引，上卷 0x40、下卷 0xB0
        // 地图属性索引不建议被编辑！！所以不提供修改功能！！

        // 将地图属性整合为写入的格式，index=0无效
        byte[][] properties = new byte[mapEditor.getMapMaxCount()][];
        for (Map.Entry<Integer, MapProperties> entry : getMapProperties().entrySet()) {
            // 将地图属性转换为字节
            // 世界地图不包括在内
            properties[entry.getKey()] = entry.getValue().toByteArray();
        }

        List<byte[]> propertiesData = new ArrayList<>();

        // 索引起始固定值
        char mapPropertiesIndex = 0x8500;

        char[] mapIndexRoll = new char[0x40 + 0xB0];
        mapIndexRoll[0x00] = 0x0000;    // 固定数据，好像没用
        // 世界地图无效，所以从1开始
        for (int mapId = 0x01; mapId < properties.length; mapId++) {
            if (mapIndexRoll[mapId] != 0x0000) {
                // 已经设置过了
                continue;
            }

            byte[] property = properties[mapId];
            propertiesData.add(property);

            // 将后面相同数据的地图属性一同设置
            for (int afterMap = mapId; afterMap < properties.length; afterMap++) {
                if (Arrays.equals(properties[mapId], properties[afterMap])) {
                    mapIndexRoll[afterMap] = mapPropertiesIndex;
                }
            }
            mapPropertiesIndex += property.length;
        }
        // 应用上卷的索引
        byte[] mapIndexUpRoll = new byte[0x40 * 0x02];
        for (int i = 0; i < 0x40; i++) {
            char value = mapIndexRoll[i];
            mapIndexUpRoll[(i * 0x02) + 0x00] = NumberR.at(value, 0);
            mapIndexUpRoll[(i * 0x02) + 0x01] = NumberR.at(value, 1);
        }
        position(getMapPropertiesIndexUpRollAddress());
        getBuffer().put(mapIndexUpRoll);

        // 应用下卷的索引
        byte[] mapIndexDownRoll = new byte[0xB0 * 0x02];
        for (int i = 0x40; i < (0x40 + 0xB0); i++) {
            char value = mapIndexRoll[i];
            mapIndexDownRoll[((i - 0x40) * 0x02) + 0x00] = NumberR.at(value, 0);
            mapIndexDownRoll[((i - 0x40) * 0x02) + 0x01] = NumberR.at(value, 1);
        }
        position(getMapPropertiesIndexDownRollAddress());
        getBuffer().put(mapIndexDownRoll);

        // 写入地图属性
        position(getMapPropertiesAddress());
        for (byte[] property : propertiesData) {
            getBuffer().put(property);
        }

        int end = getMapPropertiesAddress().getEndAddress(-position() + 0x10 + 1);
        if (end >= 0) {
            LOGGER.info("地图属性编辑器：剩余{}个空闲字节", end);
        } else {
            LOGGER.error("地图属性编辑器：错误！超出了数据上限{}字节", -end);
        }

        // 写入重定向的条件数据和地图
        // data[0] = fromMaps
        // data[1] = data
        // data[2] = toMaps
        byte[][] redirectData = new byte[3][getMapPropertiesRedirectMaxCount()];
        int count = 0;
        for (Map.Entry<Integer, MapProperties> entry : getMapProperties().entrySet()) {
            if (count >= getMapPropertiesRedirectMaxCount()) {
                break;
            }
            SingleMapEntry<Byte, Byte> redirect = entry.getValue().redirect;
            if (redirect != null) {
                redirectData[0][count] = (byte) (entry.getKey() & 0xFF);
                redirectData[1][count] = redirect.getKey();
                redirectData[2][count] = redirect.getValue();
                count++;
            }
        }

        // 写入地图重定向数据
        position(getMapPropertiesRedirectAddress());
        getBuffer().putAABytes(0, getMapPropertiesRedirectMaxCount(), redirectData);

        // 写入地图怪物领域索引
        position(getMapPropertiesMonsterRealmIndexAddress());
        for (int mapId = 0x80; mapId < mapEditor.getMapMaxCount(); mapId++) {
            MapProperties mapProperties = this.mapProperties.get(mapId);
            getBuffer().put(mapProperties.monsterRealmIndex);
        }

        // 写入世界地图属性
        MapProperties worldMapProperties = this.mapProperties.get(0x00);
        // 写入宽度、高度
        getBuffer().putPrg(0x28A87 - 0x10, worldMapProperties.getWidth());
        // 写入可移动区域偏移量，同上
        getBuffer().putPrg(0x28A8D - 0x10, worldMapProperties.getMovableWidthOffset());
        // 写入可移动区域，同上
        getBuffer().putPrg(0x28A95 - 0x10, (byte) (worldMapProperties.intMovableWidth() + worldMapProperties.intMovableWidthOffset()));
        // 写入出入口初始地址
        getBuffer().putPrg(0x28ABB - 0x10, NumberR.at(worldMapProperties.getEntrance(), 0)); // 默认 0x80
        getBuffer().putPrg(0x28ABF - 0x10, NumberR.at(worldMapProperties.getEntrance(), 1)); // 默认 0x89
        // 写入事件图块索引
        getBuffer().putPrg(0x28AC3 - 0x10, NumberR.at(worldMapProperties.eventTilesIndex, 0));
        getBuffer().putPrg(0x28AC7 - 0x10, NumberR.at(worldMapProperties.eventTilesIndex, 1));

        // 写入自定义地图通缉令地图和数据
        List<Map.Entry<Integer, Byte>> customWantedDataEntries = getCustomMapWanted().entrySet()
                .stream()
                .limit(0x0D)
                .toList();
        byte[][] customWantedData = new byte[0x02][0x0D];
        for (int i = 0; i < 0x0D; i++) {
            Map.Entry<Integer, Byte> entry = customWantedDataEntries.get(i);
            customWantedData[0x00][i] = (byte) (entry.getKey() & 0xFF);
            customWantedData[0x01][i] = entry.getValue();
        }
        position(getCustomMapWantedAddress());
        getBuffer().put(customWantedData);
    }


    @Override
    public LinkedHashMap<Integer, MapProperties> getMapProperties() {
        return mapProperties;
    }

    @Override
    public Map<Integer, Byte> getCustomMapWanted() {
        return customWanted;
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
    public DataAddress getMapPropertiesMonsterRealmIndexAddress() {
        return mapPropertiesMonsterRealmIndexAddress;
    }

    @Override
    public DataAddress getCustomMapWantedAddress() {
        return mapCustomWantedAddress;
    }

    /**
     * SuperHack版本
     */
    @Editor.TargetVersion("super_hack")
    public static class SHMapPropertiesEditorImpl extends MapPropertiesEditorImpl {
        public SHMapPropertiesEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe);
        }

        @Override
        @Editor.Load
        public void onLoad() {
            super.onLoad();
            // SH的固定入口
            getWorldMapProperties().setEntrance(0x87A0);
        }

        @Override
        @Editor.Apply
        public void onApply(IMapEditor mapEditor,
                            IPaletteEditor paletteEditor,
                            IEventTilesEditor eventTilesEditor,
                            IMapEntranceEditor mapEntranceEditor) {
            super.onApply(mapEditor, paletteEditor, eventTilesEditor, mapEntranceEditor);
        }
    }

    /**
     * SuperHack通用版本
     */
    @Editor.TargetVersion("super_hack_general")
    public static class SHGMapPropertiesEditorImpl extends MapPropertiesEditorImpl {
        public SHGMapPropertiesEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe);
        }

        @Override
        @Editor.Load
        public void onLoad() {
            super.onLoad();
            // SH的固定入口
            getWorldMapProperties().setEntrance(0x87A0);
        }

        @Override
        @Editor.Apply
        public void onApply(IMapEditor mapEditor,
                            IPaletteEditor paletteEditor,
                            IEventTilesEditor eventTilesEditor,
                            IMapEntranceEditor mapEntranceEditor) {
            super.onApply(mapEditor, paletteEditor, eventTilesEditor, mapEntranceEditor);
        }
    }
}
