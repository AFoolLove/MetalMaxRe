package me.afoolslove.metalmaxre.editors.player;

/**
 * @author AFoolLove
 */

public enum Player {
    /**
     * 主角
     */
    PLAYER_0(0x00),
    /**
     * 伙伴1
     */
    PLAYER_1(0x01),
    /**
     * 伙伴2
     */
    PLAYER_2(0x02);

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
