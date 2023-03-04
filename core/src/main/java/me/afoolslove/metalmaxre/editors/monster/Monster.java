package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.editors.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    public byte attack;
    /**
     * 防御力
     */
    public byte defense;
    /**
     * 护甲
     * <p>
     * 可能没有
     */
    public Byte armor;
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
    public Byte dropsItem;

    /**
     * 怪物的类型，等其它属性
     */
    public byte attribute;

    /**
     * 属性抗性和自动恢复HP
     */
    public byte resistance;

    /**
     * 特殊能力
     */
    public byte ability;

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
     * 设置怪物的特殊能力
     * <p>
     * 0B0000_0000 攻击1次<p>
     * 0B0100_0000 攻击1次+概率攻击1次<p>
     * 0B1000_0000 攻击2次<p>
     * 0B1100_0000 攻击2次+概率攻击1次<p>
     * 0B0001_0000 优先攻击老大<p>
     * 0B0010_0000 优先攻击老二<p>
     * 0B0011_0000 优先攻击老三<p>
     * 0B0000_1000 分裂<p>
     * 0B0000_0100 死亡后爆炸波及附近怪物，对其造成伤害<p>
     * 0B0000_0011 闪避概率（默认00 08 40 80）
     *
     * @param ability 特殊能力
     */
    public void setAbility(byte ability) {
        this.ability = ability;
    }

    public void setAbility(int ability) {
        setAbility((byte) (ability & 0xFF));
    }

    /**
     * 设置攻击次数<p>
     * <p>
     * 0 攻击1次<p>
     * 1 攻击1次+概率攻击1次<p>
     * 2 攻击2次<p>
     * 3 攻击2次+概率攻击1次<p>
     *
     * @param attackNumber 攻击次数
     */
    public void setAttackNumber(@Range(from = 0x00, to = 0x03) int attackNumber) {
        // 将值位移到D7D6
        attackNumber &= 0B0000_0011;
        attackNumber <<= 6;

        int ability = this.ability;
        // 清除原来的攻击次数
        ability &= 0B0011_1111;
        // 设置新的攻击次数
        ability |= attackNumber;

        setAbility(ability);
    }

    /**
     * 设置攻击优先级
     * <p>
     * 传入 {@code null} 没有攻击优先级
     *
     * @param attackPriority 攻击优先级
     */
    public void setAttackPriority(@Nullable Player attackPriority) {
        int ability = this.ability;
        // 清除优先级
        ability &= 0B1100_1111;
        if (attackPriority != null) {
            switch (attackPriority) {
                case PLAYER_0 -> ability |= 0B0001_0000; //优先攻击老大
                case PLAYER_1 -> ability |= 0B0010_0000; //优先攻击老二
                case PLAYER_2 -> ability |= 0B0011_0000; //优先攻击老三
            }
        }

        setAbility(ability);
    }

    /**
     * 设置是否可以分裂
     *
     * @param canSplit 是否可以分裂
     */
    public void setCanSplit(boolean canSplit) {
        int ability = this.ability;
        // 清除可分裂
        ability &= 0B1111_0111;

        if (canSplit) {
            // 添加可分裂
            ability |= 0B0000_1000;
        }

        setAbility(ability);
    }

    /**
     * 设置怪物死亡后是否波及附近怪物，对被波及的怪物造成伤害
     *
     * @param deathExplosion 死亡爆炸波及附近怪物
     */
    public void setDeathExplosion(boolean deathExplosion) {
        int ability = this.ability;
        // 清除死亡爆炸波及附近怪物
        ability &= 0B1111_1011;

        if (deathExplosion) {
            // 添加死亡爆炸波及附近怪物
            ability |= 0B0000_0100;
        }

        setAbility(ability);
    }

    /**
     * 设置怪物的闪避率
     * <p>
     * 0（默认00<p>
     * 1（默认08<p>
     * 2（默认40<p>
     * 3（默认80
     *
     * @param dodgeRate 闪避率
     */
    public void setDodgeRate(int dodgeRate) {
        dodgeRate &= 0B0000_0011;

        int ability = this.ability;
        // 清除闪避率
        ability &= 0B1111_1100;
        // 添加闪避率
        ability |= dodgeRate;

        setAbility(ability);
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
     * 设置怪物的真实生命值，怪物类型会根据生命值被更改
     *
     * @param health 真实生命值
     */
    public void setHealthValue(int health) {
        if (health > 0xFF) {
            health &= 0B11_1111_1100;
            health >>>= 0x02;
            setType(MonsterType.CYBERNETIC);
        }
        setHealth(health);
    }


    /**
     * 设置怪物的攻击力，实际攻击力会根据命中率的D7变化
     *
     * @param attack 攻击力
     */
    public void setAttack(byte attack) {
        this.attack = attack;
    }

    public void setAttack(@Range(from = 0x00, to = 0xFF) int attack) {
        setAttack((byte) (attack & 0xFF));
    }

    /**
     * 设置怪物的真实攻击力，会影响命中率D7
     *
     * @param attack 真实攻击力
     */
    public void setAttackValue(int attack) {
        if (attack > 0xFF) {
            attack &= 0B11_1111_1100;
            attack >>= 0x02;

            setRawHitRate(getRawHitRate() | 0B1000_0000);
        } else {
            setHitRate(getHitRate());
        }
        setAttack(attack);
    }

    /**
     * 设置怪物的防御力，实际防御力会根据战斗等级D7变化
     *
     * @param defense 防御力
     */
    public void setDefense(byte defense) {
        this.defense = defense;
    }

    public void setDefense(@Range(from = 0x00, to = 0xFF) int defense) {
        setDefense((byte) defense);
    }

    /**
     * 设置真实防御力，会影响战斗等级D7
     *
     * @param defense 真实防御力
     */
    public void setDefenseValue(int defense) {
        if (defense > 0xFF) {
            defense &= 0B11_1111_1100;
            defense >>>= 0x02;
            setBattleLevel(getBattleLevel() | 0B1000_0000);
        } else {
            setBattleLevel(getBattleLevel() & 0B0111_1111);
        }
        setDefense(defense);
    }

    /**
     * 设置怪物的护甲
     *
     * @param armor 护甲值
     */
    public void setArmor(Byte armor) {
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
    public void setRawHitRate(@Range(from = 0x00, to = 0xFF) int hitRate) {
        this.hitRate = (byte) (hitRate & 0xFF);
    }

    public void setHitRate(@Range(from = 0x00, to = 0x7F) byte hitRate) {
        setRawHitRate(hitRate & 0B0111_1111);
    }

    public void setHitRate(@Range(from = 0x00, to = 0x7F) int hitRate) {
        setHitRate((byte) (hitRate & 0B0111_1111));
    }

    /**
     * 设置怪物的战斗等级
     * 0B0111_1111 战斗等级（0x00-0x7F）
     * 0B1000_0000 影响 $72CC（未测试淦什么的），效果与命中率数据一致
     */
    public void setRawBattleLevel(@Range(from = 0x00, to = 0xFF) int battleLevel) {
        this.battleLevel = (byte) (battleLevel & 0xFF);
    }

    public void setBattleLevel(@Range(from = 0x00, to = 0x7F) byte battleLevel) {
        setRawBattleLevel(battleLevel & 0B0111_1111);
    }

    public void setBattleLevel(@Range(from = 0x00, to = 0x7F) int battleLevel) {
        setBattleLevel((byte) (battleLevel & 0B0111_1111));
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
/*

7BA4
D2A4
D2A4-
B7A4
44A4
4BA4
63A4
6FA4
76A4
7FA4
B3A4
8AA4
A7A4

 */

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
    public void setDropsItem(Byte dropsItem) {
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
     * @return 怪物的特殊能力
     */
    public byte getAbility() {
        return ability;
    }

    public int intAbility() {
        return getAbility() & 0xFF;
    }

    /**
     * @return 攻击次数
     */
    public int getAttackNumber() {
        int ability = intAbility();
        ability &= 0B1100_0000;
        ability >>>= 6;
        return ability;
    }

    /**
     * @return 攻击优先级
     */
    public int getAttackPriority() {
        int ability = intAbility();
        ability &= 0B0011_0000;
        ability >>>= 4;

        return ability;
    }

    /**
     * @return 攻击优先级，返回{#code null}时无攻击优先级
     */
    @Nullable
    public Player getAttackPriorityToPlayer() {
        int attackPriority = getAttackPriority();
        if (attackPriority == 0) {
            // 无优先级
            return null;
        }
        // 减一后得到玩家id
        return Player.formId(attackPriority - 1);
    }

    /**
     * @return 是否可以分裂
     */
    public boolean isCanSplit() {
        return (intAbility() & 0B000_1000) != 0x00;
    }

    /**
     * @return 死亡后爆炸波及附近怪物，对被波及的怪物造成伤害
     */
    public boolean isDeathExplosion() {
        return (intAbility() & 0B000_0100) != 0x00;
    }

    /**
     * @return 闪避率
     */
    public int getDodgeRate() {
        return intAbility() & 0B0000_0011;
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
     * @return 怪物的真实生命值
     */
    public int getHealthValue() {
        if (getType() == MonsterType.CYBERNETIC) {
            return intHealth() << 2;
        }
        return intHealth();
    }

    /**
     * @return 怪物的攻击力
     */
    public byte getAttack() {
        return attack;
    }

    public int intAttack() {
        return getAttack() & 0xFF;
    }

    /**
     * @return 怪物的真实攻击力
     */
    public int getAttackValue() {
        if ((getRawHitRate() & 0B1000_0000) != 0x00) {
            return intAttack() << 2;
        }
        return intAttack();
    }

    /**
     * @return 怪物的防御力
     */
    public byte getDefense() {
        return defense;
    }

    public int intDefense() {
        return getDefense() & 0xFF;
    }

    /**
     * @return 怪物的真实防御力
     */
    public int getDefenseValue() {
        if ((getBattleLevel() & 0B1000_0000) != 0x00) {
            return intDefense() << 2;
        }
        return intDefense();
    }

    /**
     * @return 怪物的护甲值
     */
    public Byte getArmor() {
        return armor;
    }

    public int intArmor() {
        return getArmor() & 0xFF;
    }

    /**
     * @return 是否拥有护甲值
     */
    public boolean hasArmor() {
        return getArmor() != null;
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
     * @return 怪物的命中率（全
     */
    public byte getRawHitRate() {
        return hitRate;
    }

    /**
     * @return 怪物的命中率（仅
     */
    public byte getHitRate() {
        return (byte) (hitRate & 0x7F);
    }

    @Range(from = 0x00, to = 0x7F)
    public int intHitRate() {
        return getHitRate() & 0x7F;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intRawHitRate() {
        return getRawHitRate() & 0xFF;
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
        return (this.attribute & 0B0001_0000) != 0x00;
    }

    /**
     * @return 怪物的经验值是否*100
     */
    public boolean isHundredfoldExp() {
        return (this.attribute & 0B0010_0000) != 0x00;
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
    public Byte getDropsItem() {
        return dropsItem;
    }

    @Range(from = 0x00, to = 0xFF)
    public int intDropsItem() {
        return getDropsItem() & 0xFF;
    }

    /**
     * @return 是否拥有战利品
     */
    public boolean hasDropsItem() {
        return getDropsItem() != null;
    }

}
