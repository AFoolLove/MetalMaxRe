package me.afoolslove.metalmaxre.editor.monster;

import org.jetbrains.annotations.Range;

/**
 * 怪物属性
 *
 * @author AFoolLove
 */
public class Monster {
    /**
     * 生命值
     */
    public char health;
    /**
     * 攻击力
     */
    public char attack;
    /**
     * 防御力
     */
    public char defense;
    /**
     * 护甲
     */
    public byte armor;
    /**
     * 速度
     */
    public byte speed;
    /**
     * 命中率
     */
    public byte hitRate;
    /**
     * 战斗等级
     */
    public byte battleLevel;
    /**
     * 经验值
     */
    public char experience;
    /**
     * 掉落金钱
     */
    public char gold;
    /**
     * 掉落物
     */
    public byte dropsItem;


    public Monster() {
    }

    /**
     * 设置怪物的出手攻击速度
     */
    public void setSpeed(byte speed) {
        this.speed = speed;
    }

    public void setSpeed(@Range(from = 0x00, to = 0xFF) int speed) {
        this.speed = (byte) (speed & 0xFF);
    }

    /**
     * 设置怪物的命中率
     * 0B0111_1111  命中率（0x00-0x7F）
     * 0B1000_0000 怪物的高位攻击力左移1bit（高位攻击力*2）
     * 如果高位攻击力超过0xFF，怪物的低位防御力左移1bit（低位防御力*2）
     * 如果存在0B0100_0000，上面的内容再来一次，不影响命中率
     */
    public void setHitRate(byte hitRate) {
        this.hitRate = hitRate;
    }

    public void setHitRate(@Range(from = 0x00, to = 0xFF) int hitRate) {
        this.hitRate = (byte) (hitRate & 0xFF);
    }

    /**
     * 设置掉落物
     * 注：只有怪物ID范围在0x18-0x82内才能设置为有效的战利品
     */
    public void setDropsItem(byte dropsItem) {
        this.dropsItem = dropsItem;
    }

    public void setDropsItem(@Range(from = 0x00, to = 0xFF) int dropsItem) {
        this.dropsItem = (byte) (dropsItem & 0xFF);
    }

    // --------------

    /**
     * @return 怪物出手攻击速度
     */
    public byte getSpeed() {
        return speed;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intSpeed() {
        return getSpeed() & 0xFF;
    }

    /**
     * @return 怪物的命中率
     */
    public byte getHitRate() {
        return hitRate;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intHitRate() {
        return getHitRate() & 0xFF;
    }

    /**
     * @return 战利品
     */
    public byte getDropsItem() {
        return dropsItem;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intDropsItem() {
        return getDropsItem() & 0xFF;
    }

}
