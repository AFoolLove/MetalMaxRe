package me.afoolslove.metalmaxre.editor.player;

/**
 * @author AFoolLove
 */

public enum Player {
    /**
     * 猎人
     */
    HANTA(0x00),
    /**
     * 机械师
     */
    TAMPER(0x01),
    /**
     * 佣兵
     */
    ANNE(0x02);

    private final byte id;

    Player(int id) {
        this.id = (byte) (id & 0xFF);
    }

    public byte getId() {
        return id;
    }

    /**
     * @return 玩家的名称
     */
    public String getName() {
        // 暂时使用枚举名称
        return this.name();
    }
}
