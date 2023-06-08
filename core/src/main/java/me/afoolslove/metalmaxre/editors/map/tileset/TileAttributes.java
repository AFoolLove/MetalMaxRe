package me.afoolslove.metalmaxre.editors.map.tileset;

import org.jetbrains.annotations.Range;

public class TileAttributes {
    public static final int TILE_ATTRIBUTES_LENGTH = 0x40;
    private final byte[] attributes;

    public TileAttributes() {
        attributes = new byte[TILE_ATTRIBUTES_LENGTH];
    }

    public TileAttributes(byte[] attributes) {
        this.attributes = attributes;
    }

    public byte[] getAttributes() {
        return attributes;
    }

    /**
     * 判断是否为墙壁
     *
     * @return 是否为墙壁
     */
    public static boolean isWall(int attribute) {
        return (attribute & 0B1000_0000) != 0x00;
    }

    /**
     * 获取调色板索引
     *
     * @return 调色板索引
     */
    @Range(from = 0x00, to = 0x03)
    public static int getColor(int attribute) {
        return attribute & 0B0000_0011;
    }

    /**
     * 判断是否为入口
     *
     * @return 是否为入口
     */
    public static boolean isEntrance(int attribute) {
        return (attribute & 0B1111_1100) == 0B0011_0100;
    }

    /**
     * 判断是否为计算机
     * <p>
     * *售货机不使用该数据
     *
     * @return 是否为计算机
     */
    public static boolean isComputer(int attribute) {
        return (attribute & 0B1111_1100) == 0B1111_0000;
    }

    /**
     * 判断是否为井盖
     *
     * @return 是否为井盖
     */
    public static boolean isManholeCover(int attribute) {
        return (attribute & 0B1111_1100) == 0B0101_0100;
    }

    /**
     * 判断是否为门
     *
     * @return 是否为门
     */
    public static boolean isDoor(int attribute) {
        return (attribute & 0B1111_1100) == 0B0011_1100;
    }

    /**
     * 判断是否为电梯
     *
     * @return 是否为电梯
     */
    public static boolean isElevator(int attribute) {
        return (attribute & 0B1111_1100) == 0B0100_0100;
    }

    /**
     * 判断是否为传送装置
     *
     * @return 是否为传送装置
     */
    public static boolean isTeleport(int attribute) {
        return (attribute & 0B1111_1100) == 0B0100_1100;
    }

    /**
     * 判断是否为通缉令A
     *
     * @return 是否为通缉令A
     */
    public static boolean isWantedA(int attribute) {
        return (attribute & 0B1111_1100) == 0B1111_0100;
    }

    /**
     * 判断是否为通缉令B
     *
     * @return 是否为通缉令B
     */
    public static boolean isWantedB(int attribute) {
        return (attribute & 0B1111_1100) == 0B1111_1000;
    }

    /**
     * 判断是否为宝藏
     *
     * @return 是否为宝藏
     */
    public static boolean isTreasure(int attribute) {
        return (attribute & 0B1111_1100) == 0B1110_0100;
    }
}
