package me.afoolslove.metalmaxre.editor.map;

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
     * 暂时不知道叫什么好，可以影响传送带等的运动方式 2byte
     */
    public static final byte FLAG_DY_TILE = 0B0000_0001;
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
     * 单个地图属性的最大长度
     */
    public static final int PROPERTIES_MAX_LENGTH = 0x1C;

    /**
     * 单个地图属性的基础长度
     */
    public static final int PROPERTIES_BASE_LENGTH = PROPERTIES_MAX_LENGTH - 2 - 2;


    public byte head;
    public byte width, height;
    public byte movableWidthOffset, movableHeightOffset;
    public byte movableWidth, movableHeight;
    public char mapIndex;
    public byte combinationA, combinationB;
    public char entrance;
    public char palette;
    public byte spriteIndex;
    public byte tilesIndexA, tilesIndexB, tilesIndexC, tilesIndexD;
    public byte hideTile;
    public byte unknown;
    public byte fillTile;
    public byte music;
    /**
     * 可能不存在
     */
    public byte dyTileSpeed, dyTile;
    /**
     * 可能不存在
     */
    public char eventTilesIndex;

    public MapProperties(byte[] properties) {
        setProperties(properties);
    }

    public void setProperties(byte[] properties) {
        this.head = properties[0x00];
        this.width = properties[0x01];
        this.height = properties[0x02];
        this.movableWidthOffset = properties[0x03];
        this.movableHeightOffset = properties[0x04];
        this.movableWidth = properties[0x05];
        this.movableHeight = properties[0x06];
        this.mapIndex = (char) (properties[0x07] & 0xFF);
        this.mapIndex += (properties[0x08] & 0xFF) << 8;
        this.combinationA = properties[0x09];
        this.combinationB = properties[0x0A];
        this.entrance = (char) (properties[0x0B] & 0xFF);
        this.entrance += (properties[0x0C] & 0xFF) << 8;
        this.palette = (char) (properties[0x0D] & 0xFF);
        this.palette += (properties[0x0E] & 0xFF) << 8;
        this.spriteIndex = properties[0x0F];
        this.tilesIndexA = properties[0x10];
        this.tilesIndexB = properties[0x11];
        this.tilesIndexC = properties[0x12];
        this.tilesIndexD = properties[0x13];
        this.hideTile = properties[0x14];
        this.unknown = properties[0x15];
        this.fillTile = properties[0x16];
        this.music = properties[0x17];

        if (hasDyTile()) {
            this.dyTileSpeed = properties[0x18];
            this.dyTile = properties[0x19];
        }
        if (hasEventTile()) {
            this.eventTilesIndex = (char) (properties[0x1A] & 0xFF);
            this.eventTilesIndex += (char) ((properties[0x1B] & 0xFF) << 8);
        }
    }

    /**
     * 将tilesIndexA、tilesIndexB、tilesIndexC、tilesIndexD合并为一个int
     */
    public int getIntTiles() {
        return (tilesIndexD & 0xFF)
                + ((tilesIndexC & 0xFF) << 8)
                + ((tilesIndexB & 0xFF) << 16)
                + ((tilesIndexA & 0xFF) << 24);
    }

    public boolean hasEventTile() {
        return hasEventTile(this.head);
    }

    public boolean hasDyTile() {
        return hasDyTile(this.head);
    }

    public boolean isUnderground() {
        return isUnderground(this.head);
    }

    public boolean isUnderground(int head) {
        return (head & FLAG_UNDERGROUND) == FLAG_UNDERGROUND;
    }


    public byte[] toByteArray() {
        int length = PROPERTIES_MAX_LENGTH;
        if (!hasDyTile()) {
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
        attributes[0x05] = this.movableWidth;
        attributes[0x06] = this.movableHeight;
        attributes[0x07] = (byte) (this.mapIndex & 0x00FF);
        attributes[0x08] = (byte) ((this.mapIndex & 0xFF00) >> 8);
        attributes[0x09] = this.combinationA;
        attributes[0x0A] = this.combinationB;
        attributes[0x0B] = (byte) (this.entrance & 0x00FF);
        attributes[0x0C] = (byte) ((this.entrance & 0xFF00) >> 8);
        attributes[0x0D] = (byte) (this.palette & 0x00FF);
        attributes[0x0E] = (byte) ((this.palette & 0xFF00) >> 8);
        attributes[0x0F] = this.spriteIndex;
        attributes[0x10] = this.tilesIndexA;
        attributes[0x11] = this.tilesIndexB;
        attributes[0x12] = this.tilesIndexC;
        attributes[0x13] = this.tilesIndexD;
        attributes[0x14] = this.hideTile;
        attributes[0x15] = this.unknown;
        attributes[0x16] = this.fillTile;
        attributes[0x17] = this.music;

        // 后面的属性是紧凑的方式储存
        int index = 0x18;
        if (hasDyTile(this.head)) {
            attributes[index++] = this.dyTileSpeed;
            attributes[index++] = this.dyTile;
        }
        if (hasEventTile(this.head)) {
            attributes[index++] = (byte) (this.eventTilesIndex & 0x00FF);
            attributes[index] = (byte) ((this.eventTilesIndex & 0xFF00) >> 8);
        }
        return attributes;
    }


    public static boolean hasEventTile(int head) {
        return (head & FLAG_EVENT_TILE) == FLAG_EVENT_TILE;
    }

    public static boolean hasDyTile(int head) {
        return (head & FLAG_DY_TILE) == FLAG_DY_TILE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MapProperties that)) {
            return false;
        }
        return head == that.head && width == that.width && height == that.height && movableWidthOffset == that.movableWidthOffset && movableHeightOffset == that.movableHeightOffset && movableWidth == that.movableWidth && movableHeight == that.movableHeight && mapIndex == that.mapIndex && combinationA == that.combinationA && combinationB == that.combinationB && entrance == that.entrance && palette == that.palette && spriteIndex == that.spriteIndex && tilesIndexA == that.tilesIndexA && tilesIndexB == that.tilesIndexB && tilesIndexC == that.tilesIndexC && tilesIndexD == that.tilesIndexD && hideTile == that.hideTile && unknown == that.unknown && fillTile == that.fillTile && music == that.music && dyTileSpeed == that.dyTileSpeed && dyTile == that.dyTile && eventTilesIndex == that.eventTilesIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, width, height, movableWidthOffset, movableHeightOffset, movableWidth, movableHeight, mapIndex, combinationA, combinationB, entrance, palette, spriteIndex, tilesIndexA, tilesIndexB, tilesIndexC, tilesIndexD, hideTile, unknown, fillTile, music, dyTileSpeed, dyTile, eventTilesIndex);
    }
}
