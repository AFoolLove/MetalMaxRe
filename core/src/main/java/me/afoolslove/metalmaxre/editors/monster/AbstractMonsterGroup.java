package me.afoolslove.metalmaxre.editors.monster;

import java.util.Arrays;

/**
 * 抽象怪物组类
 *
 * @author AFoolLove
 */
public abstract class AbstractMonsterGroup {
    private final byte[] monsters;

    protected AbstractMonsterGroup() {
        this.monsters = null;
    }

    public AbstractMonsterGroup(byte[] monsters) {
        this.monsters = monsters;
    }

    /**
     * 获取所有怪物ID
     *
     * @return 所有怪物ID
     */
    public byte[] getMonsters() {
        return Arrays.copyOf(this.monsters, this.monsters.length);
    }

    /**
     * 获取指定怪物ID
     *
     * @param index 索引
     * @return 怪物ID
     */
    public byte getMonster(int index) {
        return this.monsters[index];
    }

    /**
     * 设置所有怪物
     *
     * @param monsters 所有怪物
     */
    public void setMonsters(byte[] monsters) {
        setMonsters(monsters, 0x00, monsters.length);
    }

    /**
     * 截取一段数据设置到怪物
     *
     * @param monsters 怪物
     * @param offset   偏移
     * @param length   长度
     */
    public void setMonsters(byte[] monsters, int offset, int length) {
        System.arraycopy(monsters, offset, this.monsters, 0x00, Math.max(length, monsters.length - offset));
    }

    /**
     * 设置怪物ID
     *
     * @param index   索引
     * @param monster 怪物ID
     */
    public void setMonster(int index, byte monster) {
        this.monsters[index] = monster;
    }

    /**
     * 是否为空怪物组，不会遇敌的区域
     *
     * @return 是否为空怪物组
     */
    public abstract boolean isEmptyMonsterGroup();
}
