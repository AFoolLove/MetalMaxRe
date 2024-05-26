package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * 地图属性
 * <p>
 * 不支持世界地图
 *
 * @author AFoolLove
 */
public class MapProperties {
    /**
     * ベルトコンベアー
     * <p>
     * 皮带输送机，传送带 2byte
     */
    public static final byte FLAG_BELT_CONVEYOR = 0B0000_0001;
    /**
     * 地图迷雾
     */
    public static final byte FLAG_FOG_OF_MAP = 0B0000_0010;
    /**
     * 地图是否存在事件图块
     */
    public static final byte FLAG_EVENT_TILE = 0B0000_0100;
    /**
     * 地图是否为地下地图
     * 使用犬系统显示返回地面
     */
    public static final byte FLAG_UNDERGROUND = 0B0000_1000;
    /**
     * 地图是否为高层建筑
     * 走出边界时会播放摔落声音
     */
    public static final byte FLAG_HIGH_RISE = 0B0001_0000;

    /**
     * 单个地图属性的最大长度
     */
    public static final int PROPERTIES_MAX_LENGTH = 0x1C;

    /**
     * 单个地图属性的基础长度
     */
    public static final int PROPERTIES_BASE_LENGTH = PROPERTIES_MAX_LENGTH - 2 - 2;


    private byte head;
    private byte width, height;
    public byte movableWidthOffset, movableHeightOffset;
    public byte movableWidth, movableHeight;
    public char mapIndex;
    public byte combinationA, combinationB;
    private char entrance;
    private char palette;
    private byte spriteIndex;
    public byte tilesIndexA, tilesIndexB, tilesIndexC, tilesIndexD;
    private byte hideTile;
    private byte fogOfMapTile;
    private byte fillTile;
    private byte music;
    /**
     * 传送带，扶梯等
     */
    public byte beltConveyorDirection, beltConveyorStepBack;
    /**
     * 事件图块索引
     * <p>
     * 可能不存在
     */
    public char eventTilesIndex;

    /**
     * 根据key的数据，在进入该地图时，是否重定向为value地图
     * <p>
     * 注：目标坐标无法变更
     * <p>
     * K：map
     * <p>
     * V: data
     */
    @Nullable
    public SingleMapEntry<Byte, Byte> redirect;

    /**
     * 怪物组合索引
     * <p>
     * 地图ID 0x80及之后的地图才会拥有
     */
    public byte monsterGroupIndex;

    public MapProperties(byte[] properties) {
        setProperties(properties);
    }

    public void setProperties(byte[] properties) {
        this.head = properties[0x00];
        this.width = properties[0x01];
        this.height = properties[0x02];
        this.movableWidthOffset = properties[0x03];
        this.movableHeightOffset = properties[0x04];
        this.movableWidth = (byte) ((properties[0x05] & 0xFF) - (this.movableWidthOffset & 0xFF));
        this.movableHeight = (byte) ((properties[0x06] & 0xFF) - (this.movableHeightOffset & 0xFF));
        this.mapIndex = (char) NumberR.toInt(properties[0x07], properties[0x08]);
        this.combinationA = properties[0x09];
        this.combinationB = properties[0x0A];
        this.entrance = (char) NumberR.toInt(properties[0x0B], properties[0x0C]);
        this.palette = (char) NumberR.toInt(properties[0x0D], properties[0x0E]);
        this.spriteIndex = properties[0x0F];
        this.tilesIndexA = properties[0x10];
        this.tilesIndexB = properties[0x11];
        this.tilesIndexC = properties[0x12];
        this.tilesIndexD = properties[0x13];
        this.hideTile = properties[0x14];
        this.fogOfMapTile = properties[0x15];
        this.fillTile = properties[0x16];
        this.music = properties[0x17];

        if (hasBeltConveyor()) {
            this.beltConveyorDirection = properties[0x18];
            this.beltConveyorStepBack = properties[0x19];
        }
        if (hasEventTile()) {
            this.eventTilesIndex = (char) NumberR.toInt(properties[0x1A], properties[0x1B]);
        }
    }

    /**
     * 将tilesIndexA、tilesIndexB、tilesIndexC、tilesIndexD合并为一个int
     */
    public int getIntTiles() {
        return NumberR.toInt(tilesIndexD, tilesIndexC, tilesIndexB, tilesIndexA);
    }

    /**
     * 将0x04、0x05、spriteIndex、spriteIndex+1合并为一个int
     */
    public int getIntSpriteTiles() {
        return NumberR.toInt(intSpriteIndex() + 1, intSpriteIndex(), 0x05, 0x04);
    }

    public byte getIntTile(int index) {
        return switch (index) {
            case 0x00 -> tilesIndexA;
            case 0x40 -> tilesIndexB;
            case 0x80 -> tilesIndexC;
            case 0xC0 -> tilesIndexD;
            default -> 0x00;
        };
    }

    public int intIntTile(int index) {
        return switch (index) {
            case 0x00 -> tilesIndexA & 0xFF;
            case 0x40 -> tilesIndexB & 0xFF;
            case 0x80 -> tilesIndexC & 0xFF;
            case 0xC0 -> tilesIndexD & 0xFF;
            default -> 0x00;
        };
    }

    /**
     * 将int分为4个byte，并设置到tilesIndexA、tilesIndexB、tilesIndexC、tilesIndexD
     */
    public void setIntTiles(int xXX) {
        this.tilesIndexA = NumberR.at(xXX, 3);
        this.tilesIndexB = NumberR.at(xXX, 2);
        this.tilesIndexC = NumberR.at(xXX, 1);
        this.tilesIndexD = NumberR.at(xXX, 0);
    }

    public void setCombination(byte combinationA, byte combinationB) {
        this.combinationA = combinationA;
        this.combinationB = combinationB;
    }

    public void setCombination(int combination) {
        setCombination(NumberR.at(combination, 1), NumberR.at(combination, 0));
    }

    public void setEntrance(char entrance) {
        this.entrance = entrance;
    }

    public void setEntrance(int entrance) {
        setEntrance((char) entrance);
    }

    public void setPalette(char palette) {
        this.palette = palette;
    }

    public void setPalette(int palette) {
        setPalette((char) palette);
    }

    public void setSpriteIndex(byte spriteIndex) {
        this.spriteIndex = spriteIndex;
    }

    /**
     * 设置地图的宽度
     *
     * @param width 宽度
     */
    public void setWidth(byte width) {
        this.width = width;
    }

    public void setWidth(@Range(from = 0x00, to = 0xFF) int width) {
        setWidth((byte) (width & 0xFF));
    }

    /**
     * 设置地图的高度
     *
     * @param height 高度
     */
    public void setHeight(byte height) {
        this.height = height;
    }

    public void setHeight(@Range(from = 0x00, to = 0xFF) int height) {
        setHeight((byte) (height & 0xFF));
    }

    /**
     * 设置地图的宽高
     */
    public void setSize(@Range(from = 0x00, to = 0xFF) int width, @Range(from = 0x00, to = 0xFF) int height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * 设置地图的宽高
     */
    public void setSize(byte width, byte height) {
        setWidth(width);
        setHeight(height);
    }


    /**
     * 设置可移动区域X轴偏移
     *
     * @param movableWidthOffset X偏移
     */
    public void setMovableWidthOffset(byte movableWidthOffset) {
        this.movableWidthOffset = movableWidthOffset;
    }

    public void setMovableWidthOffset(@Range(from = 0x00, to = 0xFF) int movableWidthOffset) {
        setMovableWidthOffset((byte) (movableWidthOffset & 0xFF));
    }

    /**
     * 设置可移动区域Y轴偏移
     *
     * @param movableHeightOffset Y偏移
     */
    public void setMovableHeightOffset(byte movableHeightOffset) {
        this.movableHeightOffset = movableHeightOffset;
    }

    public void setMovableHeightOffset(@Range(from = 0x00, to = 0xFF) int movableHeightOffset) {
        setMovableHeightOffset((byte) (movableHeightOffset & 0xFF));
    }

    /**
     * 设置可移动区域X和Y轴偏移
     */
    public void setMovableOffset(@Range(from = 0x00, to = 0xFF) int movableWidthOffset, @Range(from = 0x00, to = 0xFF) int movableHeightOffset) {
        setMovableWidthOffset(movableWidthOffset);
        setMovableHeightOffset(movableHeightOffset);
    }

    /**
     * 设置可移动区域X和Y轴偏移
     */
    public void setMovableOffset(byte movableWidthOffset, byte movableHeightOffset) {
        setMovableWidthOffset(movableWidthOffset);
        setMovableHeightOffset(movableHeightOffset);
    }

    /**
     * 设置可移动区域宽度
     *
     * @param movableWidth 可移动区域的宽度
     */
    public void setMovableWidth(byte movableWidth) {
        this.movableWidth = movableWidth;
    }

    public void setMovableWidth(@Range(from = 0x00, to = 0xFF) int movableWidth) {
        setMovableWidth((byte) (movableWidth & 0xFF));
    }

    /**
     * 设置可移动区域高度
     *
     * @param movableHeight 可移动区域的高度
     */
    public void setMovableHeight(byte movableHeight) {
        this.movableHeight = movableHeight;
    }

    public void setMovableHeight(@Range(from = 0x00, to = 0xFF) int movableHeight) {
        setMovableHeight((byte) (movableHeight & 0xFF));
    }

    /**
     * 设置可移动区域的宽度和高度
     */
    public void setMovable(@Range(from = 0x00, to = 0xFF) int movableWidth, @Range(from = 0x00, to = 0xFF) int movableHeight) {
        setMovableWidth(movableWidth);
        setMovableHeight(movableHeight);
    }

    /**
     * 设置可移动区域的宽度和高度
     */
    public void setMovable(byte movableWidth, byte movableHeight) {
        setMovableWidth(movableWidth);
        setMovableHeight(movableHeight);
    }


    /**
     * 设置地图的填充图块
     *
     * @param fillTile 用于填充的图块
     */
    public void setFillTile(@Range(from = 0x00, to = 0x7F) byte fillTile) {
        this.fillTile = fillTile;
    }

    public void setFillTile(@Range(from = 0x00, to = 0x7F) int fillTile) {
        setFillTile((byte) (fillTile & 0x7F));
    }

    /**
     * 设置地图的隐藏图块（门下图块
     *
     * @param hideTile 隐藏的图块
     */
    public void setHideTile(byte hideTile) {
        this.hideTile = hideTile;
    }

    public void setHideTile(@Range(from = 0x00, to = 0xFF) int hideTile) {
        setHideTile((byte) (hideTile & 0xFF));
    }

    /**
     * 设置背景音乐
     * <p>
     * 进入该地图后切换音乐，如果与上一个地图的音乐一致，不会重新开始播放
     *
     * @param music 音乐
     */
    public void setMusic(byte music) {
        this.music = music;
    }

    public void setMusic(int music) {
        this.music = (byte) music;
    }

    /**
     * 设置地图的头属性
     */
    public void setHead(byte head) {
        this.head = head;
    }

    /**
     * 添加头属性flag
     *
     * @param flags 头属性flags
     */
    public void addHeadFlags(int flags) {
        setHead((byte) (getHead() | flags));
    }

    /**
     * 移除头属性flags
     *
     * @param flags 头属性flags
     */
    public void removeHeadFlags(int flags) {
        // 将flags反码，得到需要保留的数据
        setHead((byte) (getHead() & ~flags));
    }

    /**
     * 判断是否存在某些头属性
     *
     * @return 如果存在其中一个头属性flag则返回{@code true}，否则返回{@code false}
     */
    public boolean hasHeadFlags(int flags) {
        // 通过移除头属性中的目标flags后，与头属性比较判断是否存在，不一致表示至少存在一个
        return (getHead() & ~flags) != getHead();
    }

    /**
     * 判断是否存在指定头属性flag
     *
     * @param flag 头属性flag
     * @return 是否存在指定头属性flag
     */
    public boolean hasHeadFlag(int flag) {
        return (getHead() & flag) != 0x00;
    }

    /**
     * @return 地图的宽度
     */
    public byte getWidth() {
        return width;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intWidth() {
        return getWidth() & 0xFF;
    }

    /**
     * @return 地图的高度
     */
    public byte getHeight() {
        return height;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intHeight() {
        return getHeight() & 0xFF;
    }


    public char getEntrance() {
        return entrance;
    }

    public int intEntrance() {
        return getEntrance() & 0xFFFF;
    }

    public char getPalette() {
        return palette;
    }

    public int intPalette() {
        return getPalette() & 0xFFFF;
    }


    public byte getSpriteIndex() {
        return spriteIndex;
    }

    public int intSpriteIndex() {
        return getSpriteIndex() & 0xFF;
    }


    /**
     * @return 可移动区域宽度
     */
    public byte getMovableWidth() {
        return movableWidth;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intMovableWidth() {
        return getMovableWidth() & 0xFF;
    }

    /**
     * @return 可移动区域X轴偏移
     */
    public byte getMovableWidthOffset() {
        return movableWidthOffset;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intMovableWidthOffset() {
        return getMovableWidthOffset() & 0xFF;
    }

    /**
     * @return 可移动区域高度
     */
    public byte getMovableHeight() {
        return movableHeight;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intMovableHeight() {
        return getMovableHeight() & 0xFF;
    }

    /**
     * @return 可移动区域Y轴偏移
     */
    public byte getMovableHeightOffset() {
        return movableHeightOffset;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intMovableHeightOffset() {
        return getMovableHeightOffset() & 0xFF;
    }

    /**
     * @return 用于填充的图块
     */
    public byte getFillTile() {
        return fillTile;
    }

    @Range(from = 0x00, to = 0x7F)
    public int intFillTile() {
        return getFillTile() & 0x7F;
    }

    /**
     * @return 地图迷雾图块
     */
    public byte getFogOfMapTile() {
        return fogOfMapTile;
    }

    public int intFogOfMapTile() {
        return getFogOfMapTile() & 0xFF;
    }

    /**
     * @return 隐藏的图块（门下图块
     */
    public byte getHideTile() {
        return hideTile;
    }

    @Range(from = 0x00, to = 0x7F)
    public int intHideTile() {
        return getHideTile() & 0x7F;
    }

    /**
     * 获取当前地图的背景音乐
     *
     * @return 音乐
     */
    public byte getMusic() {
        return music;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intMusic() {
        return getMusic() & 0xFF;
    }


    /**
     * 获取地图的头属性
     *
     * @return 地图的头属性
     * @see #hasBeltConveyor(int)
     * @see #hasEventTile(int)
     * @see #isUnderground(int)
     */
    public byte getHead() {
        return head;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intHead() {
        return getHead() & 0xFF;
    }

    public boolean hasEventTile() {
        return hasEventTile(this.head);
    }

    public boolean hasBeltConveyor() {
        return hasBeltConveyor(this.head);
    }

    public boolean hasFogOfMap() {
        return hasFogOfMap(this.head);
    }

    public boolean isUnderground() {
        return isUnderground(this.head);
    }

    public boolean isHighRise() {
        return isHighRise(this.head);
    }

    /**
     * @return 将基本属性打包为数组，写入数据时直接写入该数组
     */
    public byte[] toByteArray() {
        int length = PROPERTIES_MAX_LENGTH;
        if (!hasBeltConveyor()) {
            length -= 2;
        }
        if (!hasEventTile()) {
            length -= 2;
        }

        byte[] attributes = new byte[length];
        attributes[0x00] = this.head;
        attributes[0x01] = this.width;
        attributes[0x02] = this.height;
        attributes[0x03] = this.movableWidthOffset;
        attributes[0x04] = this.movableHeightOffset;
        attributes[0x05] = (byte) ((this.movableWidth & 0xFF) + (this.movableWidthOffset & 0xFF));
        attributes[0x06] = (byte) ((this.movableHeight & 0xFF) + (this.movableHeightOffset & 0xFF));
        attributes[0x07] = NumberR.at(this.mapIndex, 0);
        attributes[0x08] = NumberR.at(this.mapIndex, 1);
        attributes[0x09] = this.combinationA;
        attributes[0x0A] = this.combinationB;
        attributes[0x0B] = NumberR.at(this.entrance, 0);
        attributes[0x0C] = NumberR.at(this.entrance, 1);
        attributes[0x0D] = NumberR.at(this.palette, 0);
        attributes[0x0E] = NumberR.at(this.palette, 1);
        attributes[0x0F] = this.spriteIndex;
        attributes[0x10] = this.tilesIndexA;
        attributes[0x11] = this.tilesIndexB;
        attributes[0x12] = this.tilesIndexC;
        attributes[0x13] = this.tilesIndexD;
        attributes[0x14] = this.hideTile;
        attributes[0x15] = this.fogOfMapTile;
        attributes[0x16] = this.fillTile;
        attributes[0x17] = this.music;

        // 后面的属性是紧凑的方式储存
        int index = 0x18;
        if (hasBeltConveyor(this.head)) {
            attributes[index++] = this.beltConveyorDirection;
            attributes[index++] = this.beltConveyorStepBack;
        }
        if (hasEventTile(this.head)) {
            attributes[index++] = NumberR.at(this.eventTilesIndex, 0);
            attributes[index] = NumberR.at(this.eventTilesIndex, 1);
        }
        return attributes;
    }

    /**
     * @return 是否拥有事件图块
     */
    public static boolean hasEventTile(int head) {
        return (head & FLAG_EVENT_TILE) == FLAG_EVENT_TILE;
    }

    public static boolean hasBeltConveyor(int head) {
        return (head & FLAG_BELT_CONVEYOR) == FLAG_BELT_CONVEYOR;
    }

    public static boolean hasFogOfMap(int head) {
        return (head & FLAG_FOG_OF_MAP) == FLAG_FOG_OF_MAP;
    }

    /**
     * @return 是否为地下地图（传送时提示是否返回到地面）
     */
    public static boolean isUnderground(int head) {
        return (head & FLAG_UNDERGROUND) == FLAG_UNDERGROUND;
    }

    /**
     * @return 是否为高层建筑（走出边界时会播放摔落声音）
     */
    public static boolean isHighRise(int head) {
        return (head & FLAG_HIGH_RISE) == FLAG_HIGH_RISE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapProperties that)) {
            return false;
        }
        return head == that.head
               && width == that.width
               && height == that.height
               && movableWidthOffset == that.movableWidthOffset
               && movableHeightOffset == that.movableHeightOffset
               && movableWidth == that.movableWidth
               && movableHeight == that.movableHeight
               && mapIndex == that.mapIndex
               && combinationA == that.combinationA
               && combinationB == that.combinationB
               && entrance == that.entrance
               && palette == that.palette
               && spriteIndex == that.spriteIndex
               && tilesIndexA == that.tilesIndexA
               && tilesIndexB == that.tilesIndexB
               && tilesIndexC == that.tilesIndexC
               && tilesIndexD == that.tilesIndexD
               && hideTile == that.hideTile
               && fogOfMapTile == that.fogOfMapTile
               && fillTile == that.fillTile
               && music == that.music
               && beltConveyorDirection == that.beltConveyorDirection
               && beltConveyorStepBack == that.beltConveyorStepBack
               && eventTilesIndex == that.eventTilesIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, width, height, movableWidthOffset, movableHeightOffset, movableWidth, movableHeight, mapIndex, combinationA, combinationB, entrance, palette, spriteIndex, tilesIndexA, tilesIndexB, tilesIndexC, tilesIndexD, hideTile, fogOfMapTile, fillTile, music, beltConveyorDirection, beltConveyorStepBack, eventTilesIndex);
    }
}
