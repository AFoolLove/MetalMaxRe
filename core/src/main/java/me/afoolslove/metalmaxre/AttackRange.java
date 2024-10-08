package me.afoolslove.metalmaxre;

/**
 * 攻击范围
 *
 * @author AFoolLove
 */
public enum AttackRange {
    /**
     * 单体攻击
     */
    ONE(0B0000_0000),
    /**
     * 一组攻击
     */
    GROUP(0B0000_1000),
    /**
     * 全体攻击
     */
    ALL(0B0001_0000),
    /**
     * 未知
     */
    UNKNOWN(0B0001_1000);

    private final byte value;

    AttackRange(int value) {
        this.value = (byte) (value & 0xFF);
    }

    public byte getValue() {
        return value;
    }

    /**
     * 通过值获取
     */
    public static AttackRange fromValue(int value) {
        for (AttackRange attackRange : values()) {
            if ((attackRange.value & 0xFF) == value) {
                return attackRange;
            }
        }
        return null;
    }
}
