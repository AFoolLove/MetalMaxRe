package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonsterEditorImpl extends RomBufferWrapperAbstractEditor implements IMonsterEditor {
    private final DataAddress monsterDropItemsAddress;
    private final DataAddress monsterAttributesAddress;
    private final DataAddress monsterResistanceAddress;
    private final DataAddress monsterArmorsAddress;
    private final DataAddress monsterHasArmorsAddress;
    private final DataAddress monsterHealthsAddress;
    private final DataAddress monsterSpeedsAddress;
    private final DataAddress monsterHitRatesAddress;
    private final DataAddress monsterBattleLevelAddress;
    private final DataAddress monsterBattleExperienceAddress;
    private final DataAddress monsterBattleGoldAddress;
    private final DataAddress monsterGroupAddress;
    private final DataAddress specialMonsterGroupAddress;
    private final DataAddress wantedMonsterBountyAddress;
    private final DataAddress worldMapMonsterRealmsAddress;


    /**
     * 世界地图的怪物领域
     */
    private final List<Byte> worldMapRealms = new ArrayList<>(getWorldMapMonsterRealmMaxCount());

    /**
     * 所有怪物
     */
    private final HashMap<Integer, Monster> monsters = new HashMap<>();

    /**
     * 所有怪物组合
     */
    public final MonsterGroup[] monsterGroups = new MonsterGroup[getMonsterGroupMaxCount()];

    /**
     * 所有特殊怪物组合
     */
    public final SpecialMonsterGroup[] specialMonsterGroups = new SpecialMonsterGroup[getSpecialMonsterGroupMaxCount()];

    public MonsterEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x2253F - 0x10, 0x225AA - 0x10),
                DataAddress.fromPRG(0x3886E - 0x10, 0x388EF - 0x10),
                DataAddress.fromPRG(0x388F1 - 0x10, 0x38973 - 0x10),
                DataAddress.fromPRG(0x389F9 - 0x10, 0x38A07 - 0x10),
                DataAddress.fromPRG(0x38A08 - 0x10, 0x38A16 - 0x10),
                DataAddress.fromPRG(0x38A17 - 0x10, 0x38A99 - 0x10),
                DataAddress.fromPRG(0x38BA0 - 0x10, 0x38C22 - 0x10),
                DataAddress.fromPRG(0x38C23 - 0x10, 0x38CA5 - 0x10),
                DataAddress.fromPRG(0x38CA6 - 0x10, 0x38D28 - 0x10),
                DataAddress.fromPRG(0x38D29 - 0x10, 0x38DAB - 0x10),
                DataAddress.fromPRG(0x38DAC - 0x10, 0x38E2E - 0x10),
                DataAddress.fromPRG(0x393B3 - 0x10, 0x398C8 - 0x10),
                DataAddress.fromPRG(0x39979 - 0x10, 0x39B38 - 0x10),
//                DataAddress.fromPRG(0x7EBE0 - 0x10, 0x7EBEB - 0x10),
                DataAddress.fromPRG(metalMaxRe.getBuffer().getHeader().getLastPrgRomLength() + 0x02BE0 - 0x10),
                DataAddress.fromPRG(0x39243 - 0x10, 0x39342 - 0x10)
        );
    }

    public MonsterEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                             DataAddress monsterDropItemsAddress,
                             DataAddress monsterAttributesAddress,
                             DataAddress monsterResistanceAddress,
                             DataAddress monsterArmorsAddress,
                             DataAddress monsterHasArmorsAddress,
                             DataAddress monsterHealthsAddress,
                             DataAddress monsterSpeedsAddress,
                             DataAddress monsterHitRatesAddress,
                             DataAddress monsterBattleLevelAddress,
                             DataAddress monsterBattleExperienceAddress,
                             DataAddress monsterBattleGoldAddress,
                             DataAddress monsterGroupAddress,
                             DataAddress specialMonsterGroupAddress,
                             DataAddress wantedMonsterBountyAddress,
                             DataAddress worldMapMonsterRealmsAddress) {
        super(metalMaxRe);
        this.monsterDropItemsAddress = monsterDropItemsAddress;
        this.monsterAttributesAddress = monsterAttributesAddress;
        this.monsterResistanceAddress = monsterResistanceAddress;
        this.monsterArmorsAddress = monsterArmorsAddress;
        this.monsterHasArmorsAddress = monsterHasArmorsAddress;
        this.monsterHealthsAddress = monsterHealthsAddress;
        this.monsterSpeedsAddress = monsterSpeedsAddress;
        this.monsterHitRatesAddress = monsterHitRatesAddress;
        this.monsterBattleLevelAddress = monsterBattleLevelAddress;
        this.monsterBattleExperienceAddress = monsterBattleExperienceAddress;
        this.monsterBattleGoldAddress = monsterBattleGoldAddress;
        this.monsterGroupAddress = monsterGroupAddress;
        this.specialMonsterGroupAddress = specialMonsterGroupAddress;
        this.wantedMonsterBountyAddress = wantedMonsterBountyAddress;
        this.worldMapMonsterRealmsAddress = worldMapMonsterRealmsAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        monsters.clear();
        worldMapRealms.clear();

        byte[] attributes = new byte[getMonsterMaxCount()];
        byte[] resistances = new byte[getMonsterMaxCount()];
        byte[] armors = new byte[getMonsterHasArmorMaxCount()];
        byte[] hasArmorMonsters = new byte[getMonsterHasArmorMaxCount()];
        byte[] healths = new byte[getMonsterMaxCount()];
        byte[] speeds = new byte[getMonsterMaxCount()];
        byte[] hitRates = new byte[getMonsterMaxCount()];
        byte[] battleLevels = new byte[getMonsterMaxCount()];
        byte[] experiences = new byte[getMonsterMaxCount()];
        byte[] golds = new byte[getMonsterMaxCount()];
        byte[] bounty = new byte[getWantedMonsterBountyMaxCount()];
        byte[] dropsItems = new byte[getMonsterMaxCount() - 0x18];

        // 读取怪物的属性
        getBuffer().get(getMonsterAttributesAddress(), attributes);

        // 读取怪物的抗性和自动恢复HP
        getBuffer().get(getMonsterResistanceAddress(), resistances);

        // 读取怪物的护甲值
        getBuffer().get(getMonsterArmorsAddress(), armors);

        // 读取拥有护甲的怪物
        getBuffer().get(getMonsterHasArmorsAddress(), hasArmorMonsters);
        // 读取怪物的生命值
        getBuffer().get(getMonsterHealthsAddress(), healths);
        // 读取怪物出手攻击速度
        getBuffer().get(getMonsterSpeedsAddress(), speeds);
        // 读取怪物命中率
        getBuffer().get(getMonsterHitRatesAddress(), hitRates);
        // 读取怪物的战斗等级
        getBuffer().get(getMonsterBattleLevelAddress(), battleLevels);
        // 读取击败怪物后获得的经验值
        getBuffer().get(getMonsterBattleExperienceAddress(), experiences);
        // 读取击败怪物后获得的金钱
        getBuffer().get(getMonsterBattleGoldAddress(), golds);
        // 读取赏金首的赏金
        getBuffer().get(getWantedMonsterBountyAddress(), bounty);
        // 读取怪物掉落物
        getBuffer().get(getMonsterDropItemsAddress(), dropsItems);


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
            // 设置属性
            monster.setAttribute(attributes[monsterId]);
            // 设置抗性和自动恢复HP
            monster.setResistance(resistances[monsterId]);
            // 设置护甲
            for (int index = 0; index < hasArmorMonsters.length; index++) {
                if (hasArmorMonsters[index] == monsterId) {
                    monster.setArmor(armors[index]);
                    break;
                }
            }
            // 设置生命值
            monster.setHealth(healths[monsterId]);
            // 设置速度
            monster.setSpeed(speeds[monsterId]);
            // 设置命中率
            monster.setHitRate(hitRates[monsterId]);
            // 设置战斗等级
            monster.setBattleLevel(battleLevels[monsterId]);
            // 设置经验值
            monster.setExperience(experiences[monsterId]);
            // 设置金钱值
            monster.setGold(golds[monsterId]);

            monsters.put(monsterId, monster);
        }

        // 读取领域索引
        position(getWorldMapMonsterRealmsAddress());
        for (int i = 0; i < getWorldMapMonsterRealmMaxCount(); i++) {
            worldMapRealms.add(getBuffer().get());
        }

        // 读取怪物组合
        position(getMonsterGroupAddress());
        for (int i = 0; i < getMonsterGroupMaxCount(); i++) {
            byte[] monsters = new byte[0x0E];
            getBuffer().get(monsters);
            monsterGroups[i] = new MonsterGroup(monsters);
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
    }

    @Editor.Apply
    public void onApply() {
        byte[] attributes = new byte[getMonsterMaxCount()];
        byte[] resistances = new byte[getMonsterMaxCount()];
        byte[] healths = new byte[getMonsterMaxCount()];
        byte[] speeds = new byte[getMonsterMaxCount()];
        byte[] hitRates = new byte[getMonsterMaxCount()];
        byte[] battleLevels = new byte[getMonsterMaxCount()];
        byte[] experiences = new byte[getMonsterMaxCount()];
        byte[] golds = new byte[getMonsterMaxCount()];

        byte[] dropsItems = new byte[getMonsterMaxCount() - 0x18];
        byte[] bounty = new byte[getWantedMonsterBountyMaxCount()];

        for (Map.Entry<Integer, Monster> entry : monsters.entrySet()) {
            Monster monster = entry.getValue();
            int monsterId = entry.getKey();
            resistances[monsterId] = monster.resistance;
            healths[monsterId] = monster.health;
            speeds[monsterId] = monster.speed;
            hitRates[monsterId] = monster.hitRate;
            battleLevels[monsterId] = monster.battleLevel;
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
        }

        // 写入怪物的属性
        getBuffer().put(getMonsterAttributesAddress(), attributes);

        // 写入怪物的抗性和自动恢复HP
        getBuffer().put(getMonsterResistanceAddress(), resistances);

        // 写入怪物的生命值
        getBuffer().put(getMonsterHealthsAddress(), healths);

        // 写入怪物出手攻击的速度
        getBuffer().put(getSpecialMonsterGroupMaxCount(), speeds);
        // 写入怪物的命中率
        getBuffer().put(getMonsterHitRatesAddress(), hitRates);
        // 写入怪物的战斗等级
        getBuffer().put(getMonsterBattleLevelAddress(), battleLevels);
        // 写入怪物被击败后玩家获取的经验
        getBuffer().put(getMonsterBattleExperienceAddress(), experiences);
        // 写入怪物被击败后玩家获取的金钱
        getBuffer().put(getMonsterBattleGoldAddress(), golds);

        // 写入怪物的掉落物
        getBuffer().put(getMonsterDropItemsAddress(), dropsItems);

        // 写入领域索引
        position(getWorldMapMonsterRealmsAddress());
        for (int i = 0; i < getWorldMapMonsterRealmMaxCount(); i++) {
            getBuffer().put(worldMapRealms.get(i));
        }

        // 写入怪物组合
        position(getMonsterGroupAddress());
        for (int i = 0; i < getMonsterGroupMaxCount(); i++) {
            MonsterGroup monsterGroup = monsterGroups[i];
            getBuffer().put(monsterGroup.monsters);
        }

        // 写入特殊怪物组合数据
        position(getSpecialMonsterGroupAddress());
        for (int i = 0; i < getSpecialMonsterGroupMaxCount(); i++) {
            SpecialMonsterGroup specialMonsterGroup = specialMonsterGroups[i];
            for (int j = 0; j < 0x04; j++) {
                getBuffer().put(specialMonsterGroup.monsters[j]);
                getBuffer().put(specialMonsterGroup.counts[j]);
            }
        }

        // 写入赏金首的赏金数据
        getBuffer().put(getWantedMonsterBountyAddress(), bounty);
    }

    @Override
    public HashMap<Integer, Monster> getMonsters() {
        return monsters;
    }

    @Override
    public MonsterGroup[] getMonsterGroups() {
        return monsterGroups;
    }

    @Override
    public SpecialMonsterGroup[] getSpecialMonsterGroups() {
        return specialMonsterGroups;
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
    public DataAddress getMonsterAttributesAddress() {
        return monsterAttributesAddress;
    }

    @Override
    public DataAddress getMonsterResistanceAddress() {
        return monsterResistanceAddress;
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
    public DataAddress getMonsterSpeedsAddress() {
        return monsterSpeedsAddress;
    }

    @Override
    public DataAddress getMonsterHitRatesAddress() {
        return monsterHitRatesAddress;
    }

    @Override
    public DataAddress getMonsterBattleLevelAddress() {
        return monsterBattleLevelAddress;
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
    public DataAddress getMonsterGroupAddress() {
        return monsterGroupAddress;
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
}
