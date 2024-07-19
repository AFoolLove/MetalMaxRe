package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public interface IMonsterEditor extends IRomEditor {
    @Override
    default String getId() {
        return "monsterEditor";
    }

    /**
     * 获取怪物的最大数量
     *
     * @return 怪物的最大数量
     */
    default int getMonsterMaxCount() {
        return 0x83;
    }

    /**
     * 获取拥有护甲值的怪物数量
     *
     * @return 拥有护甲值的怪物数量
     */
    default int getMonsterHasArmorMaxCount() {
        return 0x0F;
    }

    /**
     * 获取怪物领域的最大数量
     *
     * @return 怪物领域的最大数量
     */
    default int getMonsterRealmMaxCount() {
        return 0x5D;
    }

    /**
     * 获取特殊怪物组合的最大数量
     *
     * @return 特殊怪物组合的最大数量
     */
    default int getSpecialMonsterGroupMaxCount() {
        return 0x38;
    }

    /**
     * 获取赏金首的赏金数量，也是赏金首的数量
     *
     * @return 赏金首的赏金数量，也是赏金首的数量
     */
    default int getWantedMonsterBountyMaxCount() {
        return 0x0B;
    }

    /**
     * 获取世界地图的怪物领域数量
     *
     * @return 世界地图的怪物领域数量
     */
    default int getWorldMapMonsterRealmMaxCount() {
        return 0x100;
    }


    /**
     * @return 所有怪物属性
     */
    HashMap<Integer, Monster> getMonsters();


    /**
     * 通过怪物ID获取怪物的属性
     *
     * @param monsterId 怪物ID
     * @return 怪物属性
     * @see #getMonster(byte)
     */
    default Monster getMonster(int monsterId) {
        return getMonsters().get(monsterId);
    }

    /**
     * 通过怪物ID获取怪物的属性
     *
     * @param monsterId 怪物ID
     * @return 怪物属性
     * @see #getMonster(int)
     */
    default Monster getMonster(byte monsterId) {
        return getMonsters().get(monsterId & 0xFF);
    }

    /**
     * @return 所有怪物领域
     */
    MonsterRealm[] getMonsterRealms();

    /**
     * 获取指定位置的怪物领域
     * <p>
     * *如果使用世界地图领域数据，需要将index-1，如果在-1之前为0时，不需要获取领域，也就是无怪区域
     *
     * @param index 索引
     * @return 怪物领域
     */
    default MonsterRealm getMonsterRealm(int index) {
        return getMonsterRealms()[index];
    }

    /**
     * 获取所有特殊怪物组合集
     *
     * @return 所有特殊怪物组合集
     */
    SpecialMonsterGroup[] getSpecialMonsterGroups();

    /**
     * 获取自动恢复各个阶段的量
     *
     * @return 自动恢复各个阶段的量
     */
    List<SingleMapEntry<Integer, Integer>> getAutoRestores();


    /**
     * 获取领域属性的实际属性
     *
     * @return 领域属性的实际属性
     */
    List<byte[]> getMonsterRealmAttribute();

    /**
     * 获取指定位置的特殊怪物组合集
     *
     * @param index 索引
     * @return 特殊怪物组合集
     */
    default SpecialMonsterGroup getSpecialMonsterGroup(int index) {
        if (index == 0x00) {
            return SpecialMonsterGroup.EmptySpecialMonsterGroup.INSTANCE;
        }
        return getSpecialMonsterGroups()[index - 1];
    }

    /**
     * @return 所有赏金首怪物的属性
     */
    default List<WantedMonster> getWantedMonsters() {
        return getMonsters().values().parallelStream()
                .filter(monster -> monster instanceof WantedMonster)
                .map(monster -> ((WantedMonster) monster))
                .collect(Collectors.toList());
    }

    /**
     * 获取赏金首怪物
     *
     * @param monsterId 赏金首怪物id
     * @return 赏金首属性
     */
    default WantedMonster getWantedMonster(int monsterId) {
        return ((WantedMonster) getMonsters().get(monsterId & 0xFF));
    }

    /**
     * 获取世界地图所有怪物领域
     * <p>
     * 值为怪物组ID
     *
     * @return 世界地图怪物领域
     */
    List<Byte> getWorldMapRealms();

    /**
     * 怪物的掉落物地址
     * <p>
     * 注：怪物ID范围为0x18-0x82才能设置有效的战利品
     */
    DataAddress getMonsterDropItemsAddress();

    /**
     * 怪物的攻击组合索引地址
     */
    DataAddress getMonsterAttackModeGroupIndexAddress();

    /**
     * 怪物的攻击组合地址
     */
    DataAddress getMonsterAttackModeGroupsAddress();

    /**
     * 怪物属性中的每回合自动恢复量
     */
    DataAddress getMonsterResistanceAutoRestoreAddress();

    /**
     * 怪物的属性地址
     * <p>
     * 怪物的经验值和金钱值是否 *100
     * <p>
     * 等其它未测试的数据
     */
    DataAddress getMonsterAttributesAddress();

    /**
     * 怪物的抗性
     * <p>
     * 怪物自动恢复HP
     */
    DataAddress getMonsterResistanceAddress();

    /**
     * 怪物的特殊能力地址
     */
    DataAddress getMonsterAbilityAddress();

    /**
     * 怪物的护甲值地址
     */
    DataAddress getMonsterArmorsAddress();

    /**
     * 拥有护甲值的怪物id地址
     */
    DataAddress getMonsterHasArmorsAddress();

    /**
     * 怪物的生命值地址
     * <p>
     * *生命值根据怪物类型不同而不同
     */
    DataAddress getMonsterHealthsAddress();

    /**
     * 怪物的攻击力地址
     * <p>
     * *攻击力根据命中值D7变化
     */
    DataAddress getMonsterAttacksAddress();

    /**
     * 怪物的防御力地址
     * <p>
     * *防御力根据回避值D7变化
     */
    DataAddress getMonsterDefensesAddress();

    /**
     * 怪物的出手速度地址
     */
    DataAddress getMonsterSpeedsAddress();

    /**
     * 怪物的命中值地址
     */
    DataAddress getMonsterHitValuesAddress();

    /**
     * 怪物的回避值地址
     */
    DataAddress getMonsterEvasionValuesAddress();

    /**
     * 战斗结束玩家获得的经验值地址
     */
    DataAddress getMonsterBattleExperienceAddress();

    /**
     * 战斗结束玩家获得的金钱地址
     */
    DataAddress getMonsterBattleGoldAddress();

    /**
     * 怪物领域地址
     */
    DataAddress getMonsterRealmAddress();

    /**
     * 特殊怪物组合地址
     */
    DataAddress getSpecialMonsterGroupAddress();

    /**
     * 赏金首赏金地址
     *
     * @see IDataValueEditor#get3ByteValues()
     */
    DataAddress getWantedMonsterBountyAddress();

    /**
     * 获取世界地图的怪物领域地址
     * <p>
     * 1Byte = 16*16小块 = 256个领域，固定无法变更
     *
     * @return 世界地图的怪物领域地址
     */

    DataAddress getWorldMapMonsterRealmsAddress();

    /**
     * 获取怪物领域的领域属性中的属性索引数据
     *
     * @return 怪物领域的领域属性中的属性索引数据
     */
    DataAddress getMonsterRealmAttributeIndexAddress();

    /**
     * 获取怪物组的四个组属性地址
     *
     * @return 怪物组的四个组属性地址
     */
    List<DataAddress> getMonsterRealmAttributeAddresses();
}
