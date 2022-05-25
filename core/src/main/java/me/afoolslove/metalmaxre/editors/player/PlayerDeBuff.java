package me.afoolslove.metalmaxre.editors.player;

/**
 * 玩家的DeBuff状态
 *
 * @author AFoolLove
 */
public enum PlayerDeBuff {
    /**
     * 正常状态，无DeBuff
     */
    NORMAL((byte) 0B0000_0000),
    /**
     * 冷气包围
     */
    SURROUND_FROZEN((byte) 0B0000_0010),
    /**
     * 火焰包围
     */
    SURROUND_FLAMES((byte) 0B0000_0100),
    /**
     * 腐蚀
     */
    ERODING((byte) 0B0000_1000),
    /**
     * 恐慌
     */
    PANIC((byte) 0B0001_0000),
    /**
     * 凝固
     */
    STUCK((byte) 0B0010_0000),
    /**
     * 睡眠
     */
    SLEEPING((byte) 0B0100_0000),
    /**
     * 麻痹
     */
    PARALYSIS((byte) 0B1000_0000);

    private final byte deBuff;

    PlayerDeBuff(byte deBuff) {
        this.deBuff = deBuff;
    }

    public byte getDeBuff() {
        return deBuff;
    }

    public static PlayerDeBuff getDeBuff(int deBuff) {
        return switch (deBuff & 0xFF) {
            case 0B0000_0010 -> SURROUND_FROZEN;
            case 0B0000_0100 -> SURROUND_FLAMES;
            case 0B0000_1000 -> ERODING;
            case 0B0001_0000 -> PANIC;
            case 0B0010_0000 -> STUCK;
            case 0B0100_0000 -> SLEEPING;
            case 0B1000_0000 -> PARALYSIS;
            default -> NORMAL;
        };
    }
}
