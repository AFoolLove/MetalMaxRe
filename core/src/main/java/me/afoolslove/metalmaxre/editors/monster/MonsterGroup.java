package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;

/**
 * 怪物组合
 * <p>
 * 0x00-0x09 普通怪物，值为怪物ID
 * 0x0A-0x0D 特殊怪物，值为特殊怪物组合ID
 *
 * @author AFoolLove
 */
public class MonsterGroup {
    private final byte[] monsters = new byte[0x0E];

    private byte attributeIndex = 0x00;

    public MonsterGroup(byte[] monsters) {
        setMonsters(monsters);
    }

    /**
     * 获取普通怪物和特殊怪物组合
     *
     * @return 普通怪物和特殊怪物组合
     */
    public byte[] monsters() {
        return Arrays.copyOf(monsters, monsters.length);
    }

    /**
     * 获取怪物ID或特殊怪物组合ID
     * <p>
     * 0x00-0x09 普通怪物，值为怪物ID
     * 0x0A-0x0D 特殊怪物，值为特殊怪物组合ID
     */
    public byte getMonsterOrGroup(@Range(from = 0x00, to = 0x0D) int index) {
        return monsters[index];
    }

    /**
     * 获取普通怪物组
     *
     * @return 普通怪物组
     */
    public byte[] getMonsters() {
        return Arrays.copyOf(monsters, 0x0A);
    }

    /**
     * 获取普通怪物
     * <p>
     * 0x00-0x09 普通怪物，值为怪物ID
     *
     * @return 普通怪物
     */
    public byte getMonster(@Range(from = 0x00, to = 0x09) int index) {
        return monsters[index];
    }

    /**
     * 通过怪物编辑器将普通怪物转换为{@link Monster}对象
     * <p>
     * 0x00-0x09 普通怪物，值为怪物ID
     *
     * @param monsterEditor 怪物编辑器
     * @return 普通怪物
     */
    public Monster getMonster(@NotNull IMonsterEditor monsterEditor,
                              @Range(from = 0x00, to = 0x09) int index) {
        return monsterEditor.getMonster(getMonsterOrGroup(index));
    }

    /**
     * 通过怪物编辑器将普通怪物转换为{@link Monster}对象
     *
     * @param monsterEditor 怪物编辑器
     * @return 普通怪物组
     */
    public Monster[] getMonsters(@NotNull IMonsterEditor monsterEditor) {
        Monster[] monsters = new Monster[0x0A];
        for (int i = 0; i < monsters.length; i++) {
            monsters[i] = getMonster(monsterEditor, i);
        }
        return monsters;
    }

    /**
     * 获取所有特殊怪物组ID
     *
     * @return 所有特殊怪物组ID
     */
    public byte[] getSpecialMonsterGroups() {
        byte[] specialMonsters = new byte[0x04];
        System.arraycopy(monsters, 0x0A, specialMonsters, 0x00, specialMonsters.length);
        return specialMonsters;
    }

    /**
     * 获取特殊怪物组ID
     *
     * @param index 索引
     * @return 特殊怪物组ID
     */
    public byte getSpecialMonsterGroup(@Range(from = 0x00, to = 0x03) int index) {
        return monsters[0x0A + index];
    }

    /**
     * 通过怪物编辑器将特殊怪物组转换为{@link SpecialMonsterGroup}对象
     *
     * @param index 索引
     * @return 特殊怪物组
     */
    public SpecialMonsterGroup getSpecialMonsterGroup(@NotNull IMonsterEditor monsterEditor,
                                                      @Range(from = 0x00, to = 0x03) int index) {
        return monsterEditor.getSpecialMonsterGroup(index);
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
     * 设置普通怪物和特殊怪物组
     *
     * @param monsters 普通怪物和特殊怪物组数据
     * @param offset   偏移
     * @param length   长度
     */
    public void setMonsters(byte[] monsters,
                            @Range(from = 0x00, to = 0x0D) int offset,
                            @Range(from = 0x00, to = 0x0E) int length) {
        System.arraycopy(monsters, offset, this.monsters, 0x00, Math.max(length, monsters.length - offset));
    }

    /**
     * 设置普通怪物和特殊怪物组
     *
     * @param index   索引
     * @param monster 普通怪物ID或特殊怪物组ID
     */
    public void setMonsterOrGroup(@Range(from = 0x00, to = 0x0D) int index, byte monster) {
        monsters[index] = monster;
    }

    /**
     * 设置普通怪物和特殊怪物组
     *
     * @param monsters 普通怪物和特殊怪物组数据
     */
    public void setMonsters(byte[] monsters) {
        setMonsters(monsters, 0x00, monsters.length);
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
                                        @Range(from = 0x00, to = 0x03) int length) {
        System.arraycopy(specialMonsterGroups, offset, this.monsters, 0x0A, Math.max(length, specialMonsterGroups.length - offset));
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
     * @param specialMonsterGroup 特殊怪物组ID
     * @param index               哪组特殊怪物组
     */
    public void setSpecialMonsterGroup(@Range(from = 0x00, to = 0x03) int index, byte specialMonsterGroup) {
        this.monsters[0x0A + index] = specialMonsterGroup;
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
}
