package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;

/**
 * 怪物领域
 * <p>
 * 普通怪物组：{@link MonsterGroup}
 * <p>
 * 特殊怪物组：{@link SpecialMonsterGroup}
 *
 * @author AFoolLove
 */
public class MonsterRealm {
    private MonsterGroup monsters;
    private byte[] specialMonsters;
    private byte attributeIndex;

    public MonsterRealm() {
    }

    public MonsterRealm(byte attributeIndex, byte[] monsters) {
        this.attributeIndex = attributeIndex;
        this.monsters = new MonsterGroup(Arrays.copyOfRange(monsters, 0x00, 0x0A));
        this.specialMonsters = Arrays.copyOfRange(monsters, 0x0A, 0x0E);
    }

    public MonsterRealm(byte attributeIndex, @NotNull MonsterGroup monsters, byte[] specialMonsters) {
        this.attributeIndex = attributeIndex;
        this.monsters = monsters;
        this.specialMonsters = specialMonsters;
    }

    /**
     * 获取普通怪物组
     *
     * @return 普通怪物组
     */
    public MonsterGroup getMonsterGroup() {
        return monsters;
    }

    /**
     * 获取怪物ID或特殊怪物组合ID
     * <p>
     * 0x00-0x09 普通怪物，值为怪物ID
     * 0x0A-0x0D 特殊怪物，值为特殊怪物组合ID
     */
    public byte getMonsterOrSpecial(@Range(from = 0x00, to = 0x0D) int index) {
        if (index <= 0x09) {
            return this.monsters.getMonster(index);
        } else {
            return this.specialMonsters[index - 0x09];
        }
    }

    /**
     * 获取所有特殊怪物组ID
     *
     * @return 所有特殊怪物组ID
     */
    public byte[] getSpecialMonsters() {
        return Arrays.copyOf(this.specialMonsters, this.specialMonsters.length);
    }

    /**
     * 获取特殊怪物组ID
     *
     * @param index 索引
     * @return 特殊怪物组ID
     */
    public byte getSpecialMonster(@Range(from = 0x00, to = 0x03) int index) {
        return this.specialMonsters[index];
    }

    /**
     * 通过怪物编辑器将特殊怪物组转换为{@link SpecialMonsterGroup}对象
     *
     * @param index 索引
     * @return 特殊怪物组
     */
    public SpecialMonsterGroup getSpecialMonsterGroup(@NotNull IMonsterEditor monsterEditor,
                                                      @Range(from = 0x00, to = 0x03) int index) {
        return monsterEditor.getSpecialMonsterGroup(getSpecialMonster(index));
    }

    /**
     * 通过怪物编辑器将特殊怪物组转换为{@link SpecialMonsterGroup}对象
     *
     * @return 特殊怪物组
     */
    public SpecialMonsterGroup[] getSpecialMonsterGroups(@NotNull IMonsterEditor monsterEditor) {
        SpecialMonsterGroup[] specialMonsterGroup = new SpecialMonsterGroup[0x04];
        for (int i = 0; i < 0x04; i++) {
            specialMonsterGroup[i] = getSpecialMonsterGroup(monsterEditor, i);
        }
        return specialMonsterGroup;
    }


    /**
     * 设置普通怪物和特殊怪物组
     *
     * @param index   索引
     * @param monster 普通怪物ID或特殊怪物组ID
     */
    public void setMonsterOrSpecial(@Range(from = 0x00, to = 0x0D) int index, byte monster) {
        if (index <= 0x09) {
            this.monsters.setMonster(index, monster);
        } else {
            this.specialMonsters[index - 0x09] = monster;
        }
    }

    /**
     * 设置特殊怪物组ID
     *
     * @param specialMonsterGroups 特殊怪物组ID
     */
    public void setSpecialMonsterGroups(byte[] specialMonsterGroups) {
        setSpecialMonsterGroups(specialMonsterGroups, 0x00, specialMonsterGroups.length);
    }

    /**
     * 设置特殊怪物组ID
     *
     * @param specialMonsterGroups 特殊怪物组ID
     * @param offset               偏移
     * @param length               长度
     */
    public void setSpecialMonsterGroups(byte[] specialMonsterGroups,
                                        @Range(from = 0x00, to = 0x03) int offset,
                                        @Range(from = 0x00, to = 0x04) int length) {
        System.arraycopy(this.specialMonsters, 0x00, specialMonsterGroups, offset, length);
    }


    /**
     * 获取属性索引
     * <p>
     * 0B1100_0000  乘坐战车时概率出现战车道具
     * <p>
     * 0B0011_0000  战斗后或刚进入，初始累积遇敌几率
     * <p>
     * 0B0000_1100  怪物袭击概率
     * <p>
     * 0B0000_0011  怪物组合使用哪组概率
     *
     * @return 属性索引
     */
    public byte getAttributeIndex() {
        return attributeIndex;
    }

    /**
     * 获取属性索引
     * <p>
     * 0B1100_0000  乘坐战车时概率出现战车道具
     * <p>
     * 0B0011_0000  战斗后或刚进入，初始累积遇敌几率
     * <p>
     * 0B0000_1100  怪物袭击概率
     * <p>
     * 0B0000_0011  怪物组合使用哪组概率
     *
     * @return 属性索引
     */
    public int intAttributeIndex() {
        return getAttributeIndex() & 0xFF;
    }

    /**
     * 获取乘坐战车时获得战车道具的索引
     *
     * @return 乘坐战车时获得战车道具的索引
     */
    public int getRidingGiveItemIndex() {
        return NumberR.at(intAttributeIndex(), 7, 2, true);
    }

    /**
     * 获取进入该区域后的初始累积遇敌概率
     *
     * @return 进入该区域后的初始累积遇敌概率
     */
    public int getInitEncounterRateIndex() {
        return NumberR.at(intAttributeIndex(), 5, 2, true);
    }

    /**
     * 获取该区域玩家被突袭的概率
     *
     * @return 该区域玩家被突袭的概率
     */
    public int getSuddenlyAttackRateIndex() {
        return NumberR.at(intAttributeIndex(), 3, 2, true);
    }

    /**
     * 获取该区域使用哪组概率
     *
     * @return 该区域使用哪组概率
     * @see MonsterProbability
     */
    public int getProbabilityIndex() {
//        return NumberR.at(intAttributeIndex(), 0, 2, true);
        return intAttributeIndex() & 0B0000_0011;
    }


    /**
     * 设置属性索引
     *
     * @param attributeIndex 属性索引
     */
    public void setAttributeIndex(byte attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    /**
     * 设置属性索引
     *
     * @param attributeIndex 属性索引
     */
    public void setAttributeIndex(int attributeIndex) {
        setAttributeIndex((byte) (attributeIndex & 0xFF));
    }

    /**
     * 设置乘坐战车时获得战车道具的索引
     *
     * @param ridingGiveItemIndex 乘坐战车时获得战车道具的索引
     */
    public void setRidingGiveItemIndex(int ridingGiveItemIndex) {
        ridingGiveItemIndex &= 0B0000_0011;
        ridingGiveItemIndex <<= 6;

        int temp = intAttributeIndex();
        temp &= 0B0011_1111;
        temp |= ridingGiveItemIndex;
        setAttributeIndex(temp);
    }

    /**
     * 设置进入该区域后的初始累积遇敌概率
     *
     * @param initEncounterRateIndex 进入该区域后的初始累积遇敌概率
     */
    public void setInitEncounterRateIndex(int initEncounterRateIndex) {
        initEncounterRateIndex &= 0B0000_0011;
        initEncounterRateIndex <<= 4;

        int temp = intAttributeIndex();
        temp &= 0B1100_1111;
        temp |= initEncounterRateIndex;
        setAttributeIndex(temp);
    }

    /**
     * 设置该区域玩家被突袭的概率
     *
     * @param suddenlyAttackRateIndex 该区域玩家被突袭的概率
     */
    public void setSuddenlyAttackRateIndex(int suddenlyAttackRateIndex) {
        suddenlyAttackRateIndex &= 0B0000_0011;
        suddenlyAttackRateIndex <<= 2;

        int temp = intAttributeIndex();
        temp &= 0B1111_0011;
        temp |= suddenlyAttackRateIndex;
        setAttributeIndex(temp);
    }

    /**
     * 设置该区域使用哪组概率
     *
     * @param probabilityIndex 该区域使用哪组概率
     */
    public void setProbabilityIndex(int probabilityIndex) {
        probabilityIndex &= 0B0000_0011;

        int temp = intAttributeIndex();
        temp &= 0B1111_1100;
        temp |= probabilityIndex;
        setAttributeIndex(temp);
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[0x0E];
        for (int i = 0; i < 0x0A; i++) {
            bytes[i] = this.monsters.getMonster(i);
        }
        for (int i = 0; i < 0x04; i++) {
            bytes[0x0A + i] = this.specialMonsters[i];
        }
        return bytes;
    }
}
