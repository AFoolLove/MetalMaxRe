package me.afoolslove.metalmaxre.editor.map;

/**
 * 地图背景音乐
 * <p>
 * 命名使用罗马字或英文
 *
 * @author AFoolLove
 */
public enum MapBackgroundMusic {
    /**
     * 游戏标题的音乐
     */
    TITLE(0x03),
    /**
     * 無限軌道
     * <p>
     * 命名时的音乐
     */
    MUGEN_KIDO(0x04),
    /**
     * 未知の荒野へ
     * <p>
     * 世界地图未乘坐战车时的音乐
     */
    MICHI_NO_KOYA_E(0x05),
    /**
     * ノアとの戦い
     * <p>
     * 与诺亚战斗时的音乐
     */
    NOA_TO_NOTATAKAI(0x06),

    /**
     * 战斗时的音乐
     */
    BATTLE(0x07),
    /**
     * お寻ね者との戦い
     * <p>
     * 与赏金首战斗时的音乐
     */
    BOUNTY_MONSTER(0x08);

    /* TODO 我编不下去了 */

    private final int music;

    MapBackgroundMusic(int music) {
        this.music = music;
    }

    public byte getMusic() {
        return (byte) (music & 0xFF);
    }
}
