package me.afoolslove.metalmaxre.editors.monster;

import org.jetbrains.annotations.NotNull;
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
    public byte health;
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
     * <p>
     * 可能没有
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
    public byte experience;
    /**
     * 掉落金钱
     */
    public byte gold;
    /**
     * 掉落物
     */
    public byte dropsItem;

    /**
     * 怪物的类型，等其它属性
     */
    public byte attribute;

    /**
     * 属性抗性和自动恢复HP
     */
    public byte resistance;

    public Monster() {
    }

    /**
     * 设置怪物的属性抗性和战斗回合自动恢复HP
     *
     * @param resistance 属性
     */
    public void setResistance(byte resistance) {
        this.resistance = resistance;
    }

    public void setResistance(@Range(from = 0x00, to = 0xFF) int resistance) {
        this.resistance = (byte) (resistance & 0xFF);
    }

    /**
     * 设置怪物的生命值
     * *生命值会根据怪物的类型不同而不同
     *
     * @param health 生命值
     */
    public void setHealth(byte health) {
        this.health = health;
    }

    public void setHealth(@Range(from = 0x00, to = 0xFF) int health) {
        this.health = (byte) (health & 0xFF);
    }

    /**
     * 设置怪物的护甲
     *
     * @param armor 护甲值
     */
    public void setArmor(byte armor) {
        this.armor = armor;
    }

    public void setArmor(@Range(from = 0x00, to = 0xFF) int armor) {
        this.armor = (byte) (armor & 0xFF);
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
     * 设置怪物的战斗等级
     * 0B0111_1111 战斗等级（0x00-0x7F）
     * 0B1000_0000 影响 $72CC（未测试淦什么的），效果与命中率数据一致
     */
    public void setBattleLevel(byte battleLevel) {
        this.battleLevel = battleLevel;
    }

    public void setBattleLevel(@Range(from = 0x00, to = 0xFF) int battleLevel) {
        this.battleLevel = (byte) (battleLevel & 0xFF);
    }

    /**
     * 设置怪物被击败后玩家获取的经验值
     * 可以通过 {@link #setHundredfoldExp(boolean)} 将该值*100
     *
     * @param experience 经验值
     * @see #setHundredfoldExp(boolean)
     */
    public void setExperience(byte experience) {
        this.experience = experience;
    }

    public void setExperience(@Range(from = 0x00, to = 0xFF) int experience) {
        this.experience = (byte) (experience & 0xFF);
    }

    /**
     * 设置怪物的类型等数据
     * 0B1100_0000 怪物的类型
     * 0B0010_0000 怪物的经验值*100
     * 0B0001_0000 怪物的金钱值*100
     *
     * @param attribute 属性
     */
    public void setAttribute(byte attribute) {
        this.attribute = attribute;
    }

    public void setAttribute(@Range(from = 0x00, to = 0xFF) int attribute) {
        this.attribute = (byte) (attribute & 0xFF);
    }

    public void setType(@NotNull MonsterType monsterType) {
        this.attribute &= 0B0011_1111;
        this.attribute |= monsterType.getValue();
    }

    /**
     * 设置被被击败后玩家获取的金钱 *100(当前怪物的金钱)
     *
     * @param hundredfoldGold 是否*100
     */
    public void setHundredfoldGold(boolean hundredfoldGold) {
        this.attribute &= 0B1110_1111;
        if (hundredfoldGold) {
            this.attribute |= 0B0001_0000;
        }
    }

    /**
     * 设置被被击败后玩家获取的经验值 *100(当前怪物的经验值)
     *
     * @param hundredfoldExp 是否*100
     */
    public void setHundredfoldExp(boolean hundredfoldExp) {
        this.attribute &= 0B1101_1111;
        if (hundredfoldExp) {
            this.attribute |= 0B0010_0000;
        }
    }

    /**
     * 设置被击败后玩家获取的金钱
     *
     * @param gold 金钱
     */
    public void setGold(byte gold) {
        this.gold = gold;
    }

    public void setGold(@Range(from = 0x00, to = 0xFF) int gold) {
        this.gold = (byte) (gold & 0xFF);
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
     * @return 属性抗性和自动恢复HP
     */
    public byte getResistance() {
        return resistance;
    }

    public int intResistance() {
        return getResistance() & 0xFF;
    }

    /**
     * @return 怪物的生命值
     */
    public byte getHealth() {
        return health;
    }

    public int intHealth() {
        return getHealth() & 0xFF;
    }

    /**
     * @return 怪物的护甲值
     */
    public byte getArmor() {
        return armor;
    }

    public int intArmor() {
        return getArmor() & 0xFF;
    }

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
     * @return 怪物的战斗等级
     */
    public byte getBattleLevel() {
        return battleLevel;
    }

    public int intBattleLevel() {
        return getBattleLevel() & 0xFF;
    }

    /**
     * @return 怪物被击破后的玩家获得的经验值
     */
    public byte getExperience() {
        return experience;
    }

    public int intExperience() {
        return getExperience() & 0xFF;
    }

    /**
     * @return 怪物的类型等属性
     */
    public byte getAttribute() {
        return attribute;
    }

    public int intAttribute() {
        return getAttribute() & 0xFF;
    }

    /**
     * @return 怪物的类型
     */
    public MonsterType getType() {
        return MonsterType.getType(attribute);
    }

    /**
     * @return 怪物的金钱是否*100
     */
    public boolean isHundredfoldGold() {
        return (this.attribute & 0B1110_1111) != 0x00;
    }

    /**
     * @return 怪物的经验值是否*100
     */
    public boolean isHundredfoldExp() {
        return (this.attribute & 0B1101_1111) != 0x00;
    }

    /**
     * @return 被击败后的金钱
     */
    public byte getGold() {
        return gold;
    }

    /**
     * @return 被击败后的金钱
     */
    public int intGold() {
        return getGold() & 0xFF;
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
