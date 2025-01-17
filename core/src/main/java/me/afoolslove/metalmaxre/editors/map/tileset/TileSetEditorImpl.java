package me.afoolslove.metalmaxre.editors.map.tileset;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * 地图和精灵的图块集
 *
 * @author AFoolLove
 */
public class TileSetEditorImpl extends RomBufferWrapperAbstractEditor implements ITileSetEditor {
    private final DataAddress tileSetsAddress;
    private final DataAddress tileSetCombinationsAddress;
    private final DataAddress tileSetAttributesAddress;
    private final DataAddress worldTileSetCombinationsAddress;
    private final DataAddress worldTileSetAttributesAddress;

    private XXTileSet[] tiles = new XXTileSet[0xD4]; // 0x04 = CHR表的四分之一
    private TileCombinationSet[] combinations = new TileCombinationSet[0x37]; // 每4byte一组，0x37个组合，0x40个4byte组
    private TileAttributeSet[] attributes = new TileAttributeSet[0x37]; // 每0x40byte一组，0x37个组合，每byte对应一个图块的特性和调色板索引

    /**
     * 世界地图图块的组合数据，全局固定
     */
    private TileCombinationSet[] worldCombinations = new TileCombinationSet[0x04];
    /**
     * 世界地图图块的特性和调色板
     */
    private TileAttributeSet[] worldAttributes = new TileAttributeSet[0x04];


    private byte[] xA597 = new byte[0x004]; // 精灵的朝向帧，全局属性（移动和未移动的图像
    private byte[] xA59B = new byte[0x004]; // 精灵的姿态，全局属性（翻转、调色板等
    private byte[] xA59E = new byte[0x040]; // 精灵图像值 value + spriteId = $0150，该数据大小待验证
    private byte[] xA5DD = new byte[0x040]; // 精灵的姿态和调色板的值，(value + spriteId) | xA59B = $0160，该数据大小待验证

    private byte[] x83F2 = new byte[0x008]; // 精灵上半部分两个图像块的差值
    private byte[] x83FA = new byte[0x008]; // 精灵下半部分两个图像块的差值
    private byte[] x847B = new byte[0x0D9]; // 精灵图像块差值索引等，该数据大小待验证
    private byte[] x8552 = new byte[0x0D9]; // 精灵图像上半部分的图像索引，该数据大小待验证
    private byte[] x8629 = new byte[0x0D9]; // 精灵图像下半部分的图像索引，该数据大小待验证

    public TileSetEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromCHR(0x00000, 0x34FFF),
                DataAddress.fromCHR(0x35000, 0x386FF),
                DataAddress.fromCHR(0x38700, 0x394C0),
                DataAddress.fromPRG(0x0FBBE - 0x10, 0x1000F - 0x10),
                DataAddress.fromPRG(0x0FEAA - 0x10, 0x0FFA9 - 0x10));
    }

    public TileSetEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                             @NotNull DataAddress tileSetsAddress,
                             @NotNull DataAddress tileSetCombinationsAddress,
                             @NotNull DataAddress tileSetAttributesAddress,
                             @NotNull DataAddress worldTileSetCombinationsAddress,
                             @NotNull DataAddress worldTileSetAttributesAddress) {
        super(metalMaxRe);
        this.tileSetsAddress = tileSetsAddress;
        this.tileSetCombinationsAddress = tileSetCombinationsAddress;
        this.tileSetAttributesAddress = tileSetAttributesAddress;
        this.worldTileSetCombinationsAddress = worldTileSetCombinationsAddress;
        this.worldTileSetAttributesAddress = worldTileSetAttributesAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        // 所有tile
        for (int i = 0; i < tiles.length; i++) {
            XXTileSet xxTileSet = tiles[i];
            if (xxTileSet == null) {
                tiles[i] = new XXTileSet();
            }
            tiles[i].fillZero();
        }
        // tile组合
        for (int i = 0; i < combinations.length; i++) {
            TileCombinationSet combinationSet = combinations[i];
            if (combinationSet == null) {
                combinations[i] = new TileCombinationSet();
            }
            combinations[i].fillZero();
        }
        // tile特性和调色板
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = new TileAttributeSet();
        }
        // 世界地图的tile组合
        for (int i = 0; i < worldCombinations.length; i++) {
            TileCombinationSet worldCombination = worldCombinations[i];
            if (worldCombination == null) {
                worldCombinations[i] = new TileCombinationSet();
            }
            worldCombinations[i].fillZero();
        }
        // 世界地图的特性和调色板
        for (int i = 0; i < worldAttributes.length; i++) {
            worldAttributes[i] = new TileAttributeSet();
        }

        // 精灵相关数据
        Arrays.fill(xA597, (byte) 0x00);
        Arrays.fill(xA59B, (byte) 0x00);
        Arrays.fill(xA59E, (byte) 0x00);
        Arrays.fill(xA5DD, (byte) 0x00);
        Arrays.fill(x83F2, (byte) 0x00);
        Arrays.fill(x83FA, (byte) 0x00);
        Arrays.fill(x847B, (byte) 0x00);
        Arrays.fill(x8552, (byte) 0x00);
        Arrays.fill(x8629, (byte) 0x00);


        // 读取所有 tile：0x00-0xFF
        // 0x10byte = 1tile
        // 0x40tile = x40（0x00、0x80、0xC0）
        position(getTileSetsAddress());
        // 一共0x100个 x40
        for (XXTileSet x40 : tiles) {
            // 读取 x40
            for (int i = 0; i < 0x40; i++) {
                // 读取 tile
                TileImage tile = x40.getTile(i);
                // 读取
                getBuffer().get(tile.getTileData());
            }
        }

        // 读取所有Tile组合 0x00-0x37
        // 0x04byte = 1tile combination
        // 0x40tile = x40（0x00、0x80、0xC0）
        position(getTileSetCombinationsAddress());
        for (TileCombinationSet tileCombinations : combinations) {
            // 0x40 tile combinationSet
            for (int i = 0; i < TileCombinationSet.TILE_COMBINATIONS_LENGTH; i++) {
                // tile combination
                TileCombination tileCombination = tileCombinations.getCombination(i);
                getBuffer().get(tileCombination.getCombinationData());
            }
        }

        // 读取tile的特性和调色板 0x00-0x37
        // 0x01byte = 1tile特性和调色板
        // 0x40tile = 0x40（0x00、0x80、0xC0）
        position(getTileSetAttributesAddress());
        for (TileAttributeSet attribute : attributes) {
            // 读取 x40 tile特性和调色板
            getBuffer().get(attribute.getAttributes());
        }

        // 读取世界地图的tile组合
        position(getWorldTileSetCombinationsAddress());
        for (TileCombinationSet tileCombinations : worldCombinations) {
            // 0x40 tile combination
            for (int i = 0; i < 0x40; i++) {
                // tile combination
                TileCombination tileCombination = tileCombinations.getCombination(i);
                getBuffer().get(tileCombination.getCombinationData());
            }
        }

        // 读取世界地图的特性和调色板
        position(getWorldTileSetAttributesAddress());
        for (TileAttributeSet worldAttribute : worldAttributes) {
            // 读取 x40 tile特性和调色板
            getBuffer().get(worldAttribute.getAttributes());
        }

        // 读取精灵相关数据，太杂乱了，未命名

        // 精灵朝向帧
        prgPosition(0x34597);
        getBuffer().get(xA597);
        // 精灵的姿态
        getBuffer().get(xA59B);

        // 精灵图像值
        // 起始值最小为1，所以需要减1
        // 但避免使用 索引0 这个无效数据
        prgPosition(0x3459F - 0x01);
        getBuffer().get(xA59E);

        // 精灵的姿态和调色板值
        // 起始值最小为1，所以需要减1
        // 但避免使用 索引0 这个无效数据
        prgPosition(0x345DE - 0x01);
        getBuffer().get(xA5DD);

        // 精灵的上半部分图像块差值
        prgPosition(0x263F2);
        getBuffer().get(x83F2);
        // 精灵的下半部分图像块差值
        getBuffer().get(x83FA);

        // 精灵图像块差值索引等
        prgPosition(0x2647B);
        getBuffer().get(x847B);

        // 精灵的图像上半部分索引
        // 起始值最小为1，所以需要减1
        // 但避免使用 索引0 这个无效数据
        prgPosition(0x26553 - 0x01);
        getBuffer().get(x8552);
        // 精灵的图像下半部分索引
        // 起始值最小为1，所以需要减1
        // 但避免使用 索引0 这个无效数据
        prgPosition(0x2662A - 0x01);
        getBuffer().get(x8629);
    }

    @Editor.Apply
    public void onApply() {
        // 写入所有tile
        position(getTileSetsAddress());
        for (XXTileSet xxTileSet : tiles) {
            for (TileImage t : xxTileSet.getTiles()) {
                getBuffer().put(t.getTileData());
            }
        }

        // 写入tile组合
        position(getTileSetCombinationsAddress());
        for (TileCombinationSet combinationSet : combinations) {
            for (TileCombination c : combinationSet.getCombinations()) {
                getBuffer().put(c.getCombinationData());
            }
        }

        // 写入tile的特性和调色板
        position(getTileSetAttributesAddress());
        for (TileAttributeSet tileAttributeSet : attributes) {
            getBuffer().put(tileAttributeSet.getAttributes());
        }

        // 写入世界地图tile组合
        position(getWorldTileSetCombinationsAddress());
        for (TileCombinationSet combinationSet : worldCombinations) {
            for (TileCombination c : combinationSet.getCombinations()) {
                getBuffer().put(c.getCombinationData());
            }
        }

        // 写入世界地图tile的特性和调色板
        position(getWorldTileSetAttributesAddress());
        for (TileAttributeSet worldAttribute : worldAttributes) {
            getBuffer().put(worldAttribute.getAttributes());
        }

        // 写入精灵相关数据

        // 写入精灵朝向帧
        prgPosition(0x34597);
        getBuffer().put(xA597);
        // 写入精灵的姿态
        getBuffer().put(xA59B);

        // 写入精灵图像值
        prgPosition(0x3459F);
        getBuffer().put(xA59E, 1, xA59E.length - 1);
        // 写入精灵的姿态和调色板值
        prgPosition(0x345DE);
        getBuffer().put(xA5DD, 1, xA5DD.length - 1);

        // 写入精灵的上半部分图像块差值
        prgPosition(0x263F2);
        getBuffer().put(x83F2);
        // 写入精灵的下半部分图像块差值
        getBuffer().put(x83FA);

        // 写入精灵图像块差值索引等
        prgPosition(0x2647B);
        getBuffer().put(x847B);

        // 精灵的图像上半部分索引
        // 起始值最小为1
        // 第一个数据为无效数据
        prgPosition(0x26553);
        getBuffer().put(x8552, 1, x8552.length - 1);
        // 精灵的图像下半部分索引
        // 起始值最小为1
        // 第一个数据为无效数据
        prgPosition(0x2662A);
        getBuffer().put(x8629, 1, x8629.length - 1);
    }

    @Override
    public DataAddress getTileSetsAddress() {
        return tileSetsAddress;
    }

    @Override
    public DataAddress getTileSetCombinationsAddress() {
        return tileSetCombinationsAddress;
    }

    @Override
    public DataAddress getTileSetAttributesAddress() {
        return tileSetAttributesAddress;
    }

    @Override
    public DataAddress getWorldTileSetCombinationsAddress() {
        return worldTileSetCombinationsAddress;
    }

    @Override
    public DataAddress getWorldTileSetAttributesAddress() {
        return worldTileSetAttributesAddress;
    }

    @Override
    public XXTileSet[] getTiles() {
        return tiles;
    }

    @Override
    public TileCombinationSet[] getCombinations() {
        return combinations;
    }

    @Override
    public TileCombinationSet[] getCombinations(int... indexes) {
        TileCombinationSet[] tileCombinations = new TileCombinationSet[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            tileCombinations[i] = getCombinations()[indexes[i]];
        }
        return tileCombinations;
    }

    @Override
    public TileAttributeSet[] getAttributes() {
        return attributes;
    }

    @Override
    public TileCombinationSet[] getWorldCombinations() {
        return worldCombinations;
    }

    @Override
    public TileAttributeSet[] getWorldAttributes() {
        return worldAttributes;
    }

    @Override
    public TileAttributeSet[] getAttributes(int... xXXs) {
        TileAttributeSet[] tileAttributes = new TileAttributeSet[xXXs.length];
        for (int i = 0; i < xXXs.length; i++) {
            tileAttributes[i] = getAttributes()[xXXs[i]];
        }
        return tileAttributes;
    }

    @Override
    public byte[] getXA597() {
        return xA597;
    }

    @Override
    public byte[] getXA59B() {
        return xA59B;
    }

    @Override
    public byte[] getXA59E() {
        return xA59E;
    }

    @Override
    public byte[] getXA5DD() {
        return xA5DD;
    }

    @Override
    public byte[] getX83F2() {
        return x83F2;
    }

    @Override
    public byte[] getX83FA() {
        return x83FA;
    }

    @Override
    public byte[] getX847B() {
        return x847B;
    }

    @Override
    public byte[] getX8552() {
        return x8552;
    }

    @Override
    public byte[] getX8629() {
        return x8629;
    }

}
