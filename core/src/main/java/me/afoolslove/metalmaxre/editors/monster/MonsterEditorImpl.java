package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MonsterEditorImpl extends RomBufferWrapperAbstractEditor implements IMonsterEditor {
    private final DataAddress monsterDropItemsAddress;
    private final DataAddress monsterAttackModeGroupIndexAddress;
    private final DataAddress monsterAttackModeGroupsAddress;
    private final DataAddress monsterResistanceAutoRestoreAddress;
    private final DataAddress monsterAttributesAddress;
    private final DataAddress monsterResistanceAddress;
    private final DataAddress monsterAbilityAddress;
    private final DataAddress monsterArmorsAddress;
    private final DataAddress monsterHasArmorsAddress;
    private final DataAddress monsterHealthsAddress;
    private final DataAddress monsterAttacksAddress;
    private final DataAddress monsterDefensesAddress;
    private final DataAddress monsterSpeedsAddress;
    private final DataAddress monsterHitRatesAddress;
    private final DataAddress monsterEvasionRateAddress;
    private final DataAddress monsterBattleExperienceAddress;
    private final DataAddress monsterBattleGoldAddress;
    private final DataAddress monsterRealmAddress;
    private final DataAddress specialMonsterGroupAddress;
    private final DataAddress wantedMonsterBountyAddress;
    private final DataAddress worldMapMonsterRealmsAddress;

    private final List<DataAddress> monsterRealmAttributeAddresses;


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
                Arrays.asList(
                        DataAddress.fromPRG(0x391E5 - 0x10, 0x39242 - 0x10),
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
                             DataAddress monsterHitRatesAddress,
                             DataAddress monsterEvasionRateAddress,
                             DataAddress monsterBattleExperienceAddress,
                             DataAddress monsterBattleGoldAddress,
                             DataAddress monsterRealmAddress,
                             DataAddress specialMonsterGroupAddress,
                             DataAddress wantedMonsterBountyAddress,
                             DataAddress worldMapMonsterRealmsAddress,
                             List<DataAddress> monsterGroupAttributeAddresses) {
        super(metalMaxRe);
        this.monsterDropItemsAddress = monsterDropItemsAddress;
        this.monsterAttackModeGroupIndexAddress = monsterAttackModeGroupIndexAddress;
        this.monsterAttackModeGroupsAddress = monsterAttackModeGroupsAddress;
        this.monsterResistanceAutoRestoreAddress = monsterResistanceAutoRestoreAddress;
        this.monsterAttributesAddress = monsterAttributesAddress;
        this.monsterResistanceAddress = monsterResistanceAddress;
        this.monsterAbilityAddress = monsterAbilityAddress;
        this.monsterArmorsAddress = monsterArmorsAddress;
        this.monsterHasArmorsAddress = monsterHasArmorsAddress;
        this.monsterHealthsAddress = monsterHealthsAddress;
        this.monsterAttacksAddress = monsterAttacksAddress;
        this.monsterDefensesAddress = monsterDefensesAddress;
        this.monsterSpeedsAddress = monsterSpeedsAddress;
        this.monsterHitRatesAddress = monsterHitRatesAddress;
        this.monsterEvasionRateAddress = monsterEvasionRateAddress;
        this.monsterBattleExperienceAddress = monsterBattleExperienceAddress;
        this.monsterBattleGoldAddress = monsterBattleGoldAddress;
        this.monsterRealmAddress = monsterRealmAddress;
        this.specialMonsterGroupAddress = specialMonsterGroupAddress;
        this.wantedMonsterBountyAddress = wantedMonsterBountyAddress;
        this.worldMapMonsterRealmsAddress = worldMapMonsterRealmsAddress;
        this.monsterRealmAttributeAddresses = monsterGroupAttributeAddresses;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        monsters.clear();
        worldMapRealms.clear();
        autoRestores.clear();
        realmAttributes.clear();

        byte[] autoRestores = new byte[0x06];
        getBuffer().get(getMonsterResistanceAutoRestoreAddress(), autoRestores);
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
        byte[] hitRates = new byte[getMonsterMaxCount()];
        byte[] evasionRate = new byte[getMonsterMaxCount()];
        byte[] experiences = new byte[getMonsterMaxCount()];
        byte[] golds = new byte[getMonsterMaxCount()];
        byte[] bounty = new byte[getWantedMonsterBountyMaxCount()];
        byte[] dropsItems = new byte[getMonsterMaxCount() - 0x18];
        byte[] attackModeGroupIndexes = new byte[getMonsterMaxCount()];

        // 读取怪物的属性
        getBuffer().get(getMonsterAttributesAddress(), attributes);

        // 读取怪物的抗性和自动恢复HP
        getBuffer().get(getMonsterResistanceAddress(), resistances);
        // 读取怪物的特殊能力
        getBuffer().get(getMonsterAbilityAddress(), abilities);

        // 读取怪物的护甲值
        getBuffer().get(getMonsterArmorsAddress(), armors);

        // 读取拥有护甲的怪物
        getBuffer().get(getMonsterHasArmorsAddress(), hasArmorMonsters);
        // 读取怪物的生命值
        getBuffer().get(getMonsterHealthsAddress(), healths);
        // 读取怪物的攻击力
        getBuffer().get(getMonsterAttacksAddress(), attacks);
        // 读取怪物的防御力
        getBuffer().get(getMonsterDefensesAddress(), defenses);
        // 读取怪物出手攻击速度
        getBuffer().get(getMonsterSpeedsAddress(), speeds);
        // 读取怪物命中率
        getBuffer().get(getMonsterHitRatesAddress(), hitRates);
        // 读取怪物的回避率
        getBuffer().get(getMonsterEvasionRateAddress(), evasionRate);
        // 读取击败怪物后获得的经验值
        getBuffer().get(getMonsterBattleExperienceAddress(), experiences);
        // 读取击败怪物后获得的金钱
        getBuffer().get(getMonsterBattleGoldAddress(), golds);
        // 读取赏金首的赏金
        getBuffer().get(getWantedMonsterBountyAddress(), bounty);
        // 读取怪物掉落物
        getBuffer().get(getMonsterDropItemsAddress(), dropsItems);
        // 读取怪物的攻击模式组索引
        getBuffer().get(getMonsterAttackModeGroupIndexAddress(), attackModeGroupIndexes);

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
            // 设置命中率
            monster.setRawHitRate(hitRates[monsterId]);
            // 设置回避率
            monster.setRawEvasionRate(evasionRate[monsterId]);
            // 设置经验值
            monster.setExperience(experiences[monsterId]);
            // 设置金钱值
            monster.setGold(golds[monsterId]);

            monsters.put(monsterId, monster);
        }

        // 读取世界地图领域索引
        position(getWorldMapMonsterRealmsAddress());
        for (int i = 0; i < getWorldMapMonsterRealmMaxCount(); i++) {
            worldMapRealms.add(getBuffer().get());
        }

        // 读取领域属性
        position(getMonsterRealmAddress());
        for (int i = 0; i < getMonsterRealmMaxCount(); i++) {
            byte[] monsters = new byte[0x0E];
            getBuffer().get(monsters);
            byte attributeIndex = getBuffer().get(getMonsterRealmAttributeAddresses().get(0x00), i + 1); // 跳过默认怪物组的组属性
            monsterRealms[i] = new MonsterRealm(attributeIndex, monsters);
            monsterRealms[i].setAttributeIndex(attributeIndex);
        }

        // 读取特殊怪物组合数据
        // 覆盖式读取，不需要保留现有的数据
        position(getSpecialMonsterGroupAddress());
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
        int[] realmAttributeLengths = {getMonsterRealmAttributeAddresses().get(0x00).length(), 6, 4, 4, 4};
        for (int i = 0; i < realmAttributeLengths.length; i++) {
            DataAddress monsterRealmAttributeAddress = getMonsterRealmAttributeAddresses().get(i);
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
        position(getMonsterResistanceAutoRestoreAddress());
        getBuffer().put(autoRestores);

        byte[] attributes = new byte[getMonsterMaxCount()];
        byte[] resistances = new byte[getMonsterMaxCount()];
        byte[] abilities = new byte[getMonsterMaxCount()];
        byte[] healths = new byte[getMonsterMaxCount()];
        byte[] attacks = new byte[getMonsterMaxCount()];
        byte[] defenses = new byte[getMonsterMaxCount()];
        byte[] speeds = new byte[getMonsterMaxCount()];
        byte[] hitRates = new byte[getMonsterMaxCount()];
        byte[] evasionRate = new byte[getMonsterMaxCount()];
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
            hitRates[monsterId] = monster.hitRate;
            evasionRate[monsterId] = monster.evasionRate;
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
        getBuffer().put(getMonsterAttributesAddress(), attributes);

        // 写入怪物的抗性和自动恢复HP
        getBuffer().put(getMonsterResistanceAddress(), resistances);
        // 写入怪物的特殊能力
        getBuffer().put(getMonsterAbilityAddress(), abilities);

        // 写入怪物的生命值
        getBuffer().put(getMonsterHealthsAddress(), healths);
        // 写入怪物的攻击力
        getBuffer().put(getMonsterAttacksAddress(), attacks);
        // 写入怪物的防御力
        getBuffer().put(getMonsterDefensesAddress(), defenses);

        // 写入怪物出手攻击的速度
        getBuffer().put(getMonsterSpeedsAddress(), speeds);
        // 写入怪物的命中率
        getBuffer().put(getMonsterHitRatesAddress(), hitRates);
        // 写入怪物的回避率
        getBuffer().put(getMonsterEvasionRateAddress(), evasionRate);
        // 写入怪物被击败后玩家获取的经验
        getBuffer().put(getMonsterBattleExperienceAddress(), experiences);
        // 写入怪物被击败后玩家获取的金钱
        getBuffer().put(getMonsterBattleGoldAddress(), golds);

        // 写入怪物的掉落物
        getBuffer().put(getMonsterDropItemsAddress(), dropsItems);
        // 写入怪物的攻击模式组索引
        getBuffer().put(getMonsterAttackModeGroupIndexAddress(), attackModeGroupIndexes);

        // 写入世界地图的领域索引
        position(getWorldMapMonsterRealmsAddress());
        for (int i = 0; i < getWorldMapMonsterRealmMaxCount(); i++) {
            getBuffer().put(worldMapRealms.get(i));
        }

        // 写入领域属性
        position(getMonsterRealmAddress());
        for (int i = 0; i < getMonsterRealmMaxCount(); i++) {
            MonsterRealm monsterRealm = monsterRealms[i];
            getBuffer().put(monsterRealm.getMonsterGroup().getMonsters());
            getBuffer().put(getMonsterRealmAttributeAddresses().get(0x00).getStartAddress(i + 1), monsterRealm.getAttributeIndex());
        }

        // 写入特殊怪物组合数据
        position(getSpecialMonsterGroupAddress());
        for (int i = 0; i < getSpecialMonsterGroupMaxCount(); i++) {
            SpecialMonsterGroup specialMonsterGroup = specialMonsterGroups[i];
            for (int j = 0; j < 0x04; j++) {
                getBuffer().put(specialMonsterGroup.getMonster(j));
                getBuffer().put(specialMonsterGroup.getCount(j));
            }
        }

        // 写入赏金首的赏金数据
        getBuffer().put(getWantedMonsterBountyAddress(), bounty);

        // 写入领域属性的实际属性
        for (int i = 0; i < 0x05; i++) {
            DataAddress monsterRealmAttributeAddress = getMonsterRealmAttributeAddresses().get(i);
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

    @Override
    public DataAddress getMonsterDropItemsAddress() {
        return monsterDropItemsAddress;
    }

    @Override
    public DataAddress getMonsterAttackModeGroupIndexAddress() {
        return monsterAttackModeGroupIndexAddress;
    }

    @Override
    public DataAddress getMonsterAttackModeGroupsAddress() {
        return monsterAttackModeGroupsAddress;
    }

    @Override
    public DataAddress getMonsterResistanceAutoRestoreAddress() {
        return monsterResistanceAutoRestoreAddress;
    }

    @Override
    public DataAddress getMonsterAttributesAddress() {
        return monsterAttributesAddress;
    }

    @Override
    public DataAddress getMonsterResistanceAddress() {
        return monsterResistanceAddress;
    }

    @Override
    public DataAddress getMonsterAbilityAddress() {
        return monsterAbilityAddress;
    }

    @Override
    public DataAddress getMonsterArmorsAddress() {
        return monsterArmorsAddress;
    }

    @Override
    public DataAddress getMonsterHasArmorsAddress() {
        return monsterHasArmorsAddress;
    }

    @Override
    public DataAddress getMonsterHealthsAddress() {
        return monsterHealthsAddress;
    }

    @Override
    public DataAddress getMonsterAttacksAddress() {
        return monsterAttacksAddress;
    }

    @Override
    public DataAddress getMonsterDefensesAddress() {
        return monsterDefensesAddress;
    }

    @Override
    public DataAddress getMonsterSpeedsAddress() {
        return monsterSpeedsAddress;
    }

    @Override
    public DataAddress getMonsterHitRatesAddress() {
        return monsterHitRatesAddress;
    }

    @Override
    public DataAddress getMonsterEvasionRateAddress() {
        return monsterEvasionRateAddress;
    }

    @Override
    public DataAddress getMonsterBattleExperienceAddress() {
        return monsterBattleExperienceAddress;
    }

    @Override
    public DataAddress getMonsterBattleGoldAddress() {
        return monsterBattleGoldAddress;
    }

    @Override
    public DataAddress getMonsterRealmAddress() {
        return monsterRealmAddress;
    }

    @Override
    public DataAddress getSpecialMonsterGroupAddress() {
        return specialMonsterGroupAddress;
    }

    @Override
    public DataAddress getWantedMonsterBountyAddress() {
        return wantedMonsterBountyAddress;
    }

    @Override
    public DataAddress getWorldMapMonsterRealmsAddress() {
        return worldMapMonsterRealmsAddress;
    }

    @Override
    public List<DataAddress> getMonsterRealmAttributeAddresses() {
        return monsterRealmAttributeAddresses;
    }
}
