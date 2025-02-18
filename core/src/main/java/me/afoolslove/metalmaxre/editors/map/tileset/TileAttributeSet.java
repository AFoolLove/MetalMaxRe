package me.afoolslove.metalmaxre.editors.map.tileset;

import org.jetbrains.annotations.Range;

/**
 * 图块属性
 * <p>
 * 包含0x40(64)个图块的属性
 *
 * @author AFoolLove
 */
public class TileAttributeSet {
    public static final int TILE_ATTRIBUTES_LENGTH = 0x40;

    public static final int TILE_ATTRIBUTE_WALL = 0B1000_0000;
    public static final int TILE_ATTRIBUTE_COLOR = 0B0000_0011;
    public static final int TILE_ATTRIBUTE_ENTRANCE = 0B0011_0100;
    public static final int TILE_ATTRIBUTE_COMPUTER = 0B1111_0000;
    public static final int TILE_ATTRIBUTE_MANHOLE_COVER = 0B0101_0100;
    public static final int TILE_ATTRIBUTE_DOOR = 0B0011_1100;
    public static final int TILE_ATTRIBUTE_ELEVATOR = 0B0100_0100;
    public static final int TILE_ATTRIBUTE_TELEPORT = 0B0100_1100;
    public static final int TILE_ATTRIBUTE_WANTED_A = 0B1111_0100;
    public static final int TILE_ATTRIBUTE_WANTED_B = 0B1111_1000;
    public static final int TILE_ATTRIBUTE_TREASURE = 0B1110_0100;
    public static final int TILE_ATTRIBUTE_FUNCTION = 0B0111_1100;


    private final byte[] attributes;

    public TileAttributeSet() {
        attributes = new byte[TILE_ATTRIBUTES_LENGTH];
    }

    public TileAttributeSet(byte[] attributes) {
        this.attributes = attributes;
    }

    /**
     * 获取所有图块的属性
     *
     * @return 所有图块的属性
     */
    public byte[] getAttributes() {
        return attributes;
    }

    /**
     * 获取指定图块的属性
     *
     * @param tileId 图块ID
     * @return 图块属性
     */
    public byte getAttribute(int tileId) {
        return attributes[tileId & 0x3F];
    }

    /**
     * 获取指定图块的属性
     *
     * @param tileId 图块ID
     * @return 图块属性
     */
    public byte getAttribute(byte tileId) {
        return attributes[tileId & 0x3F];
    }

    /**
     * 设置指定图块的属性
     *
     * @param tileId    图块ID
     * @param attribute 图块属性
     */
    public void setAttribute(int tileId, byte attribute) {
        attributes[tileId & 0x3F] = attribute;
    }

    /**
     * 添加指定图块的属性
     *
     * @param tileId    图块ID
     * @param attribute 图块属性
     */
    public void addAttribute(byte tileId, byte attribute) {
        attributes[tileId & 0x3F] |= attribute;
    }

    /**
     * 判断是否为墙壁
     *
     * @return 是否为墙壁
     */
    public static boolean isWall(int attribute) {
        return (attribute & TILE_ATTRIBUTE_WALL) != 0x00;
    }

    /**
     * 获取调色板索引
     *
     * @return 调色板索引
     */
    @Range(from = 0x00, to = 0x03)
    public static int getColor(int attribute) {
        return attribute & TILE_ATTRIBUTE_COLOR;
    }

    /**
     * 判断是否为入口
     *
     * @return 是否为入口
     */
    public static boolean isEntrance(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_ENTRANCE;
    }

    /**
     * 判断是否为计算机
     * <p>
     * *售货机不使用该数据
     *
     * @return 是否为计算机
     */
    public static boolean isComputer(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_COMPUTER;
    }

    /**
     * 判断是否为井盖
     *
     * @return 是否为井盖
     */
    public static boolean isManholeCover(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_MANHOLE_COVER;
    }

    /**
     * 判断是否为门
     *
     * @return 是否为门
     */
    public static boolean isDoor(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_DOOR;
    }

    /**
     * 判断是否为电梯
     *
     * @return 是否为电梯
     */
    public static boolean isElevator(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_ELEVATOR;
    }

    /**
     * 判断是否为传送装置
     *
     * @return 是否为传送装置
     */
    public static boolean isTeleport(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_TELEPORT;
    }

    /**
     * 判断是否为通缉令A
     *
     * @return 是否为通缉令A
     */
    public static boolean isWantedA(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_WANTED_A;
    }

    /**
     * 判断是否为通缉令B
     *
     * @return 是否为通缉令B
     */
    public static boolean isWantedB(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_WANTED_B;
    }

    /**
     * 判断是否为宝藏
     *
     * @return 是否为宝藏
     */
    public static boolean isTreasure(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_TREASURE;
    }

    /**
     * 判断是否为功能图块
     * <p>
     * *原版功能：白菜地、裂缝、是否开炮、部分告示等需要调查的图块
     *
     * @return 是否为功能图块
     */
    public static boolean isFunction(int attribute) {
        return (attribute & 0B1111_1100) == TILE_ATTRIBUTE_FUNCTION;
    }
}
