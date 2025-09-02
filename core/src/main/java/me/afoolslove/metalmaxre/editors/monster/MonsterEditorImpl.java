package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MonsterEditorImpl extends RomBufferWrapperAbstractEditor implements IMonsterEditor {
    /**
     * 怪物的掉落物地址
     * <p>
     * 注：怪物ID范围为0x18-0x82才能设置有效的战利品
     */
    public static final String MONSTER_DROP_ITEMS_ADDRESS = "monsterDropItems";
    /**
     * 怪物的攻击组合索引地址
     */
    public static final String MONSTER_ATTACK_MODE_GROUP_INDEX_ADDRESS = "monsterAttackModeGroupIndex";
    /**
     * 怪物的攻击组合地址
     */
    public static final String MONSTER_ATTACK_MODE_GROUPS_ADDRESS = "monsterAttackModeGroups";
    /**
     * 怪物属性中的每回合自动恢复量
     */
    public static final String MONSTER_RESISTANCE_AUTO_RESTORE_ADDRESS = "monsterResistanceAutoRestore";
    /**
     * 怪物的属性地址
     *
     * @see Monster#getAttribute()
     * @see Monster#setAttribute(int)
     */
    public static final String MONSTER_ATTRIBUTES_ADDRESS = "monsterAttributes";
    /**
     * 怪物的抗性
     * <p>
     * 怪物自动恢复HP
     */
    public static final String MONSTER_RESISTANCE_ADDRESS = "monsterResistance";
    /**
     * 怪物的特殊能力地址
     */
    public static final String MONSTER_ABILITY_ADDRESS = "monsterAbility";
    /**
     * 怪物的护甲值地址
     */
    public static final String MONSTER_ARMORS_ADDRESS = "monsterArmors";
    /**
     * 拥有护甲值的怪物id地址
     */
    public static final String MONSTER_HAS_ARMORS_ADDRESS = "monsterHasArmors";
    /**
     * 怪物的生命值地址
     * <p>
     * *生命值根据怪物类型不同而不同
     */
    public static final String MONSTER_HEALTHS_ADDRESS = "monsterHealths";
    /**
     * 怪物的攻击力地址
     * <p>
     * *攻击力根据命中值D7变化
     */
    public static final String MONSTER_ATTACKS_ADDRESS = "monsterAttacks";
    /**
     * 怪物的防御力地址
     * <p>
     * *防御力根据回避值D7变化
     */
    public static final String MONSTER_DEFENSES_ADDRESS = "monsterDefenses";
    /**
     * 怪物的出手速度地址
     */
    public static final String MONSTER_SPEEDS_ADDRESS = "monsterSpeeds";
    /**
     * 怪物的命中值地址
     */
    public static final String MONSTER_HIT_VALUES_ADDRESS = "monsterHitValues";
    /**
     * 怪物的回避值地址
     */
    public static final String MONSTER_EVASION_VALUES_ADDRESS = "monsterEvasionValues";
    /**
     * 战斗结束玩家获得的经验值地址
     */
    public static final String MONSTER_BATTLE_EXPERIENCE_ADDRESS = "monsterBattleExperience";
    /**
     * 战斗结束玩家获得的金钱地址
     */
    public static final String MONSTER_BATTLE_GOLD_ADDRESS = "monsterBattleGold";
    /**
     * 怪物领域地址
     */
    public static final String MONSTER_REALM_ADDRESS = "monsterRealm";
    /**
     * 特殊怪物组合地址
     */
    public static final String SPECIAL_MONSTER_GROUP_ADDRESS = "specialMonsterGroup";
    /**
     * 赏金首赏金地址
     *
     * @see IDataValueEditor#get3ByteValues()
     */
    public static final String WANTED_MONSTER_BOUNTY_ADDRESS = "wantedMonsterBounty";
    /**
     * 获取世界地图的怪物领域地址
     * <p>
     * 1Byte = 16*16小块 = 256个领域，固定无法变更
     *
     * @return 世界地图的怪物领域地址
     */
    public static final String WORLD_MAP_MONSTER_REALMS_ADDRESS = "worldMapMonsterRealms";
    /**
     * 获取怪物领域的领域属性中的属性索引数据
     *
     * @return 怪物领域的领域属性中的属性索引数据
     */
    public static final String MONSTER_REALM_ATTRIBUTE_INDEX_ADDRESS = "monsterRealmAttributeIndex";
    /**
     * 获取怪物组的四个组属性地址
     *
     * @return 怪物组的四个组属性地址
     */
    public static final String MONSTER_REALM_ATTRIBUTE_LIST_ADDRESS = "monsterRealmAttribute";


    /**
     * 世界地图的怪物领域
     */
    private final List<Byte> worldMapRealms = new ArrayList<>(getWorldMapMonsterRealmMaxCount());

    /**
     * 所有怪物
     */
    private final HashMap<Integer, Monster> monsters = new HashMap<>();

    /**
     * 所有怪物领域
     */
    public final MonsterRealm[] monsterRealms = new MonsterRealm[getMonsterRealmMaxCount()];

    /**
     * 所有特殊怪物组合
     */
    public final SpecialMonsterGroup[] specialMonsterGroups = new SpecialMonsterGroup[getSpecialMonsterGroupMaxCount()];

    public final List<byte[]> attackModeGroups = new ArrayList<>();

    public final List<SingleMapEntry<Integer, Integer>> autoRestores = new ArrayList<>();

    public final List<byte[]> realmAttributes = new ArrayList<>(0x04);

    public MonsterEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x2253F - 0x10, 0x225AA - 0x10),
                DataAddress.fromPRG(0x226FA - 0x10, 0x2277C - 0x10),
                DataAddress.fromPRG(0x2277D - 0x10),
                DataAddress.fromPRG(0x2F328 - 0x10, 0x2F32D - 0x10),
                DataAddress.fromPRG(0x3886E - 0x10, 0x388EF - 0x10),
                DataAddress.fromPRG(0x388F1 - 0x10, 0x38973 - 0x10),
                DataAddress.fromPRG(0x38974 - 0x10, 0x389F6 - 0x10),
                DataAddress.fromPRG(0x389F9 - 0x10, 0x38A07 - 0x10),
                DataAddress.fromPRG(0x38A08 - 0x10, 0x38A16 - 0x10),
                DataAddress.fromPRG(0x38A17 - 0x10, 0x38A99 - 0x10),
                DataAddress.fromPRG(0x38A9A - 0x10, 0x38B1C - 0x10),
                DataAddress.fromPRG(0x38B1D - 0x10, 0x38B9F - 0x10),
                DataAddress.fromPRG(0x38BA0 - 0x10, 0x38C22 - 0x10),
                DataAddress.fromPRG(0x38C23 - 0x10, 0x38CA5 - 0x10),
                DataAddress.fromPRG(0x38CA6 - 0x10, 0x38D28 - 0x10),
                DataAddress.fromPRG(0x38D29 - 0x10, 0x38DAB - 0x10),
                DataAddress.fromPRG(0x38DAC - 0x10, 0x38E2E - 0x10),
                DataAddress.fromPRG(0x393B3 - 0x10, 0x398C8 - 0x10),
                DataAddress.fromPRG(0x39979 - 0x10, 0x39B38 - 0x10),
//                DataAddress.fromPRG(0x7EBE0 - 0x10, 0x7EBEB - 0x10),
                DataAddress.fromPRG(metalMaxRe.getBuffer().getHeader().getLastPrgRomLength() + 0x02BE0 - 0x10),
                DataAddress.fromPRG(0x39243 - 0x10, 0x39342 - 0x10),
                DataAddress.fromPRG(0x391E6 - 0x10, 0x39242 - 0x10),
                Arrays.asList(
                        DataAddress.fromPRG(0x38FC3 - 0x10, 0x38FC5 - 0x10),
                        DataAddress.fromPRG(0x39037 - 0x10, 0x3903A - 0x10),
                        DataAddress.fromPRG(0x358F2 - 0x10, 0x358F5 - 0x10),
                        DataAddress.fromPRG(0x398C9 - 0x10, 0x39900 - 0x10)
                )
        );
    }

    public MonsterEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                             DataAddress monsterDropItemsAddress,
                             DataAddress monsterAttackModeGroupIndexAddress,
                             DataAddress monsterAttackModeGroupsAddress,
                             DataAddress monsterResistanceAutoRestoreAddress,
                             DataAddress monsterAttributesAddress,
                             DataAddress monsterResistanceAddress,
                             DataAddress monsterAbilityAddress,
                             DataAddress monsterArmorsAddress,
                             DataAddress monsterHasArmorsAddress,
                             DataAddress monsterHealthsAddress,
                             DataAddress monsterAttacksAddress,
                             DataAddress monsterDefensesAddress,
                             DataAddress monsterSpeedsAddress,
                             DataAddress monsterHitValuesAddress,
                             DataAddress monsterEvasionValuesAddress,
                             DataAddress monsterBattleExperienceAddress,
                             DataAddress monsterBattleGoldAddress,
                             DataAddress monsterRealmAddress,
                             DataAddress specialMonsterGroupAddress,
                             DataAddress wantedMonsterBountyAddress,
                             DataAddress worldMapMonsterRealmsAddress,
                             DataAddress monsterRealmAttributeIndexAddress,
                             List<DataAddress> monsterRealmAttributeAddressList) {
        super(metalMaxRe);
        putDataAddress(MONSTER_DROP_ITEMS_ADDRESS, monsterDropItemsAddress);
        putDataAddress(MONSTER_ATTACK_MODE_GROUP_INDEX_ADDRESS, monsterAttackModeGroupIndexAddress);
        putDataAddress(MONSTER_ATTACK_MODE_GROUPS_ADDRESS, monsterAttackModeGroupsAddress);
        putDataAddress(MONSTER_RESISTANCE_AUTO_RESTORE_ADDRESS, monsterResistanceAutoRestoreAddress);
        putDataAddress(MONSTER_ATTRIBUTES_ADDRESS, monsterAttributesAddress);
        putDataAddress(MONSTER_RESISTANCE_ADDRESS, monsterResistanceAddress);
        putDataAddress(MONSTER_ABILITY_ADDRESS, monsterAbilityAddress);
        putDataAddress(MONSTER_ARMORS_ADDRESS, monsterArmorsAddress);
        putDataAddress(MONSTER_HAS_ARMORS_ADDRESS, monsterHasArmorsAddress);
        putDataAddress(MONSTER_HEALTHS_ADDRESS, monsterHealthsAddress);
        putDataAddress(MONSTER_ATTACKS_ADDRESS, monsterAttacksAddress);
        putDataAddress(MONSTER_DEFENSES_ADDRESS, monsterDefensesAddress);
        putDataAddress(MONSTER_SPEEDS_ADDRESS, monsterSpeedsAddress);
        putDataAddress(MONSTER_HIT_VALUES_ADDRESS, monsterHitValuesAddress);
        putDataAddress(MONSTER_EVASION_VALUES_ADDRESS, monsterEvasionValuesAddress);
        putDataAddress(MONSTER_BATTLE_EXPERIENCE_ADDRESS, monsterBattleExperienceAddress);
        putDataAddress(MONSTER_BATTLE_GOLD_ADDRESS, monsterBattleGoldAddress);
        putDataAddress(MONSTER_REALM_ADDRESS, monsterRealmAddress);
        putDataAddress(SPECIAL_MONSTER_GROUP_ADDRESS, specialMonsterGroupAddress);
        putDataAddress(WANTED_MONSTER_BOUNTY_ADDRESS, wantedMonsterBountyAddress);
        putDataAddress(WORLD_MAP_MONSTER_REALMS_ADDRESS, worldMapMonsterRealmsAddress);
        putDataAddress(MONSTER_REALM_ATTRIBUTE_INDEX_ADDRESS, monsterRealmAttributeIndexAddress);
        putDataAddress(MONSTER_REALM_ATTRIBUTE_LIST_ADDRESS, monsterRealmAttributeAddressList);
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        monsters.clear();
        worldMapRealms.clear();
        autoRestores.clear();
        realmAttributes.clear();

        byte[] autoRestores = new byte[0x06];
        getBuffer().get(getDataAddress(MONSTER_RESISTANCE_AUTO_RESTORE_ADDRESS), autoRestores);
        for (int i = 0; i < 0x03; i++) {
            this.autoRestores.add(SingleMapEntry.create(autoRestores[i * 2] & 0xFF, autoRestores[i * 2 + 1] & 0xFF));
        }

        byte[] attributes = new byte[getMonsterMaxCount()];
        byte[] resistances = new byte[getMonsterMaxCount()];
        byte[] abilities = new byte[getMonsterMaxCount()];
        byte[] armors = new byte[getMonsterHasArmorMaxCount()];
        byte[] hasArmorMonsters = new byte[getMonsterHasArmorMaxCount()];
        byte[] healths = new byte[getMonsterMaxCount()];
        byte[] attacks = new byte[getMonsterMaxCount()];
        byte[] defenses = new byte[getMonsterMaxCount()];
        byte[] speeds = new byte[getMonsterMaxCount()];
        byte[] hitValues = new byte[getMonsterMaxCount()];
        byte[] evasionValues = new byte[getMonsterMaxCount()];
        byte[] experiences = new byte[getMonsterMaxCount()];
        byte[] golds = new byte[getMonsterMaxCount()];
        byte[] bounty = new byte[getWantedMonsterBountyMaxCount()];
        byte[] dropsItems = new byte[getMonsterMaxCount() - 0x18];
        byte[] attackModeGroupIndexes = new byte[getMonsterMaxCount()];

        // 读取怪物的属性
        getBuffer().get(getDataAddress(MONSTER_ATTRIBUTES_ADDRESS), attributes);

        // 读取怪物的抗性和自动恢复HP
        getBuffer().get(getDataAddress(MONSTER_RESISTANCE_ADDRESS), resistances);
        // 读取怪物的特殊能力
        getBuffer().get(getDataAddress(MONSTER_ABILITY_ADDRESS), abilities);

        // 读取怪物的护甲值
        getBuffer().get(getDataAddress(MONSTER_ARMORS_ADDRESS), armors);

        // 读取拥有护甲的怪物
        getBuffer().get(getDataAddress(MONSTER_HAS_ARMORS_ADDRESS), hasArmorMonsters);
        // 读取怪物的生命值
        getBuffer().get(getDataAddress(MONSTER_HEALTHS_ADDRESS), healths);
        // 读取怪物的攻击力
        getBuffer().get(getDataAddress(MONSTER_ATTACKS_ADDRESS), attacks);
        // 读取怪物的防御力
        getBuffer().get(getDataAddress(MONSTER_DEFENSES_ADDRESS), defenses);
        // 读取怪物出手攻击速度
        getBuffer().get(getDataAddress(MONSTER_SPEEDS_ADDRESS), speeds);
        // 读取怪物命中值
        getBuffer().get(getDataAddress(MONSTER_HIT_VALUES_ADDRESS), hitValues);
        // 读取怪物的回避值
        getBuffer().get(getDataAddress(MONSTER_EVASION_VALUES_ADDRESS), evasionValues);
        // 读取击败怪物后获得的经验值
        getBuffer().get(getDataAddress(MONSTER_BATTLE_EXPERIENCE_ADDRESS), experiences);
        // 读取击败怪物后获得的金钱
        getBuffer().get(getDataAddress(MONSTER_BATTLE_GOLD_ADDRESS), golds);
        // 读取赏金首的赏金
        getBuffer().get(getDataAddress(WANTED_MONSTER_BOUNTY_ADDRESS), bounty);
        // 读取怪物掉落物
        getBuffer().get(getDataAddress(MONSTER_DROP_ITEMS_ADDRESS), dropsItems);
        // 读取怪物的攻击模式组索引
        getBuffer().get(getDataAddress(MONSTER_ATTACK_MODE_GROUP_INDEX_ADDRESS), attackModeGroupIndexes);

        for (int monsterId = 0; monsterId < getMonsterMaxCount(); monsterId++) {
            Monster monster;
            if (monsterId > 0x00 && monsterId <= getWantedMonsterBountyMaxCount()) {
                // 通缉怪物
                monster = new WantedMonster();
                // 赏金怪物的ID从1开始，所以要-1
                ((WantedMonster) monster).setBounty(bounty[monsterId - 1]);
            } else {
                monster = new Monster();
                if (monsterId >= 0x18) {
                    // 普通怪物
                    monster.setDropsItem(dropsItems[monsterId - 0x18]);
                }
            }
            // 设置怪物的攻击模式组索引
            monster.setAttackMode(attackModeGroupIndexes[monsterId]);
            // 设置属性
            monster.setAttribute(attributes[monsterId]);
            // 设置抗性和自动恢复HP
            monster.setResistance(resistances[monsterId]);
            // 设置怪物的特殊能力
            monster.setAbility(abilities[monsterId]);
            // 设置护甲
            for (int index = 0; index < hasArmorMonsters.length; index++) {
                if (hasArmorMonsters[index] == monsterId) {
                    monster.setArmor(armors[index]);
                    break;
                }
            }
            // 设置生命值
            monster.setHealth(healths[monsterId]);
            // 设置攻击力
            monster.setAttack(attacks[monsterId]);
            // 设置防御力
            monster.setDefense(defenses[monsterId]);
            // 设置速度
            monster.setSpeed(speeds[monsterId]);
            // 设置命中值
            monster.setRawHitValue(hitValues[monsterId]);
            // 设置回避值
            monster.setRawEvasionValue(evasionValues[monsterId]);
            // 设置经验值
            monster.setExperience(experiences[monsterId]);
            // 设置金钱值
            monster.setGold(golds[monsterId]);

            monsters.put(monsterId, monster);
        }

        // 读取世界地图领域索引
        position(getDataAddress(WORLD_MAP_MONSTER_REALMS_ADDRESS));
        for (int i = 0; i < getWorldMapMonsterRealmMaxCount(); i++) {
            worldMapRealms.add(getBuffer().get());
        }

        // 读取领域属性索引
        byte[] monsterRealmAttributeIndexes = new byte[getMonsterRealmMaxCount()];
        getBuffer().get(getDataAddress(MONSTER_REALM_ATTRIBUTE_INDEX_ADDRESS), monsterRealmAttributeIndexes);
        // 读取领域属性
        position(getDataAddress(MONSTER_REALM_ADDRESS));
        for (int i = 0; i < getMonsterRealmMaxCount(); i++) {
            byte[] monsters = new byte[0x0E];
            getBuffer().get(monsters);
            byte attributeIndex = monsterRealmAttributeIndexes[i];
            monsterRealms[i] = new MonsterRealm(attributeIndex, monsters);
            monsterRealms[i].setAttributeIndex(attributeIndex);
        }

        // 读取特殊怪物组合数据
        // 覆盖式读取，不需要保留现有的数据
        position(getDataAddress(SPECIAL_MONSTER_GROUP_ADDRESS));
        for (int i = 0; i < getSpecialMonsterGroupMaxCount(); i++) {
            byte[] monsters = new byte[0x04];
            byte[] counts = new byte[0x04];
            for (int j = 0; j < 0x04; j++) {
                monsters[j] = getBuffer().get();
                counts[j] = getBuffer().get();
            }
            specialMonsterGroups[i] = new SpecialMonsterGroup(monsters, counts);
        }

        // 读取领域属性的实际属性
        List<DataAddress> monsterRealmAttributeAddressList = getDataAddressList(MONSTER_REALM_ATTRIBUTE_LIST_ADDRESS);
        int[] realmAttributeLengths = {6, 4, 4, 4};
        for (int i = 0; i < realmAttributeLengths.length; i++) {
            DataAddress monsterRealmAttributeAddress = monsterRealmAttributeAddressList.get(i);
            byte[] bytes = new byte[realmAttributeLengths[i]];
            getBuffer().get(monsterRealmAttributeAddress, bytes);
            realmAttributes.add(bytes);
        }
    }

    @Editor.Apply
    public void onApply() {
        byte[] autoRestores = new byte[0x06];
        for (int i = 0; i < 0x03; i++) {
            SingleMapEntry<Integer, Integer> entry = this.autoRestores.get(i);
            autoRestores[i * 2] = ((Number) entry.getKey()).byteValue();
            autoRestores[i * 2 + 1] = ((Number) entry.getValue()).byteValue();
        }
        position(getDataAddress(MONSTER_RESISTANCE_AUTO_RESTORE_ADDRESS));
        getBuffer().put(autoRestores);

        byte[] attributes = new byte[getMonsterMaxCount()];
        byte[] resistances = new byte[getMonsterMaxCount()];
        byte[] abilities = new byte[getMonsterMaxCount()];
        byte[] healths = new byte[getMonsterMaxCount()];
        byte[] attacks = new byte[getMonsterMaxCount()];
        byte[] defenses = new byte[getMonsterMaxCount()];
        byte[] speeds = new byte[getMonsterMaxCount()];
        byte[] hitValues = new byte[getMonsterMaxCount()];
        byte[] evasionValue = new byte[getMonsterMaxCount()];
        byte[] experiences = new byte[getMonsterMaxCount()];
        byte[] golds = new byte[getMonsterMaxCount()];

        byte[] dropsItems = new byte[getMonsterMaxCount() - 0x18];
        byte[] attackModeGroupIndexes = new byte[getMonsterMaxCount()];
        byte[] bounty = new byte[getWantedMonsterBountyMaxCount()];

        for (Map.Entry<Integer, Monster> entry : monsters.entrySet()) {
            Monster monster = entry.getValue();
            int monsterId = entry.getKey();
            resistances[monsterId] = monster.resistance;
            abilities[monsterId] = monster.ability;
            healths[monsterId] = monster.health;
            attacks[monsterId] = monster.attack;
            defenses[monsterId] = monster.defense;
            speeds[monsterId] = monster.speed;
            hitValues[monsterId] = monster.hitValue;
            evasionValue[monsterId] = monster.evasionValue;
            experiences[monsterId] = monster.experience;
            golds[monsterId] = monster.gold;

            attributes[monsterId] = monster.attribute;
            if (monsterId > 0x00 && monsterId <= getWantedMonsterBountyMaxCount() && monster instanceof WantedMonster wantedMonster) {
                bounty[monsterId - 1] = wantedMonster.getBounty();
            } else {
                if (monsterId >= 0x18) {
                    dropsItems[monsterId - 0x18] = monster.dropsItem;
                }
            }
            attackModeGroupIndexes[monsterId] = monster.attackMode;
        }

        // 写入怪物的属性
        getBuffer().put(getDataAddress(MONSTER_ATTRIBUTES_ADDRESS), attributes);

        // 写入怪物的抗性和自动恢复HP
        getBuffer().put(getDataAddress(MONSTER_RESISTANCE_ADDRESS), resistances);
        // 写入怪物的特殊能力
        getBuffer().put(getDataAddress(MONSTER_ABILITY_ADDRESS), abilities);

        // 写入怪物的生命值
        getBuffer().put(getDataAddress(MONSTER_HEALTHS_ADDRESS), healths);
        // 写入怪物的攻击力
        getBuffer().put(getDataAddress(MONSTER_ATTACKS_ADDRESS), attacks);
        // 写入怪物的防御力
        getBuffer().put(getDataAddress(MONSTER_DEFENSES_ADDRESS), defenses);

        // 写入怪物出手攻击的速度
        getBuffer().put(getDataAddress(MONSTER_SPEEDS_ADDRESS), speeds);
        // 写入怪物的命中值
        getBuffer().put(getDataAddress(MONSTER_HIT_VALUES_ADDRESS), hitValues);
        // 写入怪物的回避值
        getBuffer().put(getDataAddress(MONSTER_EVASION_VALUES_ADDRESS), evasionValue);
        // 写入怪物被击败后玩家获取的经验
        getBuffer().put(getDataAddress(MONSTER_BATTLE_EXPERIENCE_ADDRESS), experiences);
        // 写入怪物被击败后玩家获取的金钱
        getBuffer().put(getDataAddress(MONSTER_BATTLE_GOLD_ADDRESS), golds);

        // 写入怪物的掉落物
        getBuffer().put(getDataAddress(MONSTER_DROP_ITEMS_ADDRESS), dropsItems);
        // 写入怪物的攻击模式组索引
        getBuffer().put(getDataAddress(MONSTER_ATTACK_MODE_GROUP_INDEX_ADDRESS), attackModeGroupIndexes);

        // 写入世界地图的领域索引
        position(getDataAddress(WORLD_MAP_MONSTER_REALMS_ADDRESS));
        for (int i = 0; i < getWorldMapMonsterRealmMaxCount(); i++) {
            getBuffer().put(worldMapRealms.get(i));
        }

        byte[] monsterRealmAttributeIndexes = new byte[getMonsterRealmMaxCount()];
        // 写入领域属性
        position(getDataAddress(MONSTER_REALM_ADDRESS));
        for (int i = 0; i < getMonsterRealmMaxCount(); i++) {
            MonsterRealm monsterRealm = monsterRealms[i];
            getBuffer().put(monsterRealm.toByteArray());
            monsterRealmAttributeIndexes[i] = monsterRealm.getAttributeIndex();
        }
        // 写入领域属性索引
        getBuffer().put(getDataAddress(MONSTER_REALM_ATTRIBUTE_INDEX_ADDRESS), monsterRealmAttributeIndexes);

        // 写入特殊怪物组合数据
        position(getDataAddress(SPECIAL_MONSTER_GROUP_ADDRESS));
        for (int i = 0; i < getSpecialMonsterGroupMaxCount(); i++) {
            SpecialMonsterGroup specialMonsterGroup = specialMonsterGroups[i];
            for (int j = 0; j < 0x04; j++) {
                getBuffer().put(specialMonsterGroup.getMonster(j));
                getBuffer().put(specialMonsterGroup.getCount(j));
            }
        }

        // 写入赏金首的赏金数据
        getBuffer().put(getDataAddress(WANTED_MONSTER_BOUNTY_ADDRESS), bounty);

        // 写入领域属性的实际属性
        List<DataAddress> monsterRealmAttributeAddressList = getDataAddressList(MONSTER_REALM_ATTRIBUTE_LIST_ADDRESS);
        for (int i = 0; i < 0x04; i++) {
            DataAddress monsterRealmAttributeAddress = monsterRealmAttributeAddressList.get(i);
            getBuffer().put(monsterRealmAttributeAddress, getMonsterRealmAttribute().get(i));
        }
    }

    @Override
    public HashMap<Integer, Monster> getMonsters() {
        return monsters;
    }

    @Override
    public MonsterRealm[] getMonsterRealms() {
        return monsterRealms;
    }

    @Override
    public SpecialMonsterGroup[] getSpecialMonsterGroups() {
        return specialMonsterGroups;
    }

    @Override
    public List<SingleMapEntry<Integer, Integer>> getAutoRestores() {
        return autoRestores;
    }

    @Override
    public List<byte[]> getMonsterRealmAttribute() {
        return realmAttributes;
    }

    @Override
    public List<Byte> getWorldMapRealms() {
        return worldMapRealms;
    }
}
