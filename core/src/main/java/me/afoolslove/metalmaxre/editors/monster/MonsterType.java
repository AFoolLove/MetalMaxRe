package me.afoolslove.metalmaxre.editors.monster;

/**
 * 怪物的类型
 *
 * @author AFoolLove
 */

public enum MonsterType {
    /**
     * 电子类
     */
    CYBERNETIC((byte) 0B0000_0000),
    /**
     * 仿生类
     */
    BIONIC((byte) 0B0100_0000),
    /**
     * 坦克类
     */
    TANK((byte) 0B1000_0000),
    ;

    public final byte value;

    MonsterType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    /**
     * 通过怪物的 {@link Monster#attribute} 获取类型
     *
     * @param attribute 怪物的属性
     * @return 怪物的类型
     */
    public static MonsterType getType(byte attribute) {
        return switch (attribute & 0B1100_0000) {
            case 0B0000_0000 -> CYBERNETIC;
            case 0B0100_0000 -> BIONIC;
            case 0B1000_0000 -> TANK;
//            case 0B1100_0000 -> null; TODO 该值未确认会发生什么，但怪物被击败后三种类型的击破数都不会增加
            default -> throw new IllegalStateException("Unexpected value: " + attribute);
        };
    }
}
