package me.afoolslove.metalmaxre.editors.map;

import org.jetbrains.annotations.Nullable;

/**
 * 地图边界传送类型
 *
 * @author AFoolLove
 */
public enum MapBorderType {
    /**
     * 回到上一个地图位置
     */
    LAST((byte) 0xFF),
    /**
     * 固定地图位置
     */
    FIXED((byte) 0xFE),
    /**
     * 根据方向前往不同的地图位置
     */
    DIRECTION(null);

    private final Byte value;

    MapBorderType(@Nullable Byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return value;
    }

    /**
     * @return 转换为枚举类型
     */
    public static MapBorderType getType(int type) {
        return switch (type) {
            case 0xFF -> LAST;
            case 0xFE -> FIXED;
            default -> DIRECTION;
        };
    }
}
