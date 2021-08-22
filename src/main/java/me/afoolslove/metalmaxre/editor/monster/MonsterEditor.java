package me.afoolslove.metalmaxre.editor.monster;

import me.afoolslove.metalmaxre.DataValues;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 怪物编辑器
 * <p>
 * 怪物种类只有 0x83(131)
 *
 * @author AFoolLove
 */
public class MonsterEditor extends AbstractEditor<MonsterEditor> {
    /**
     * 怪物种类的总数量
     */
    public static final int MONSTER_COUNT = 0x83;
    /**
     * 怪物出手攻击的速度
     */
    public static final int MONSTER_SPEEDS_OFFSET = 0x38BA0 - 0x10;
    /**
     * 怪物命中率
     */
    public static final int MONSTER_HIT_RATES_OFFSET = 0x38C23 - 0x10;
    /**
     * 怪物战斗等级
     */
    public static final int MONSTER_BATTLE_LEVEL_OFFSET = 0x38CA6 - 0x10;

    /**
     * 怪物的掉落物
     * 注：ID范围为0x18-0x82才能设置有效的战利品
     */
    public static final int MONSTER_DROPS_ITEMS_OFFSET = 0x2253F - 0x10;

    /**
     * 特殊怪物组合数据起始
     */
    public static final int MONSTER_GROUP_OFFSET = 0x393B3 - 0x10;

    /**
     * 特殊怪物组合数据起始
     */
    public static final int SPECIAL_MONSTER_GROUP_OFFSET = 0x39979 - 0x10;

    /**
     * 怪物组合最大数量
     */
    public static final int MONSTER_GROUP_MAX_COUNT = 0x5D;

    /**
     * 特殊怪物组合最大数量
     */
    public static final int SPECIAL_MONSTER_GROUP_MAX_COUNT = 0x38;

    /**
     * 赏金首的赏金起始
     *
     * @see DataValues#get3ByteValue()
     */
    public static final int WANTED_MONSTER_BOUNTY_OFFSET = 0x7EBE0 - 0x10;
    /**
     * 赏金首的赏金数量，也是赏金首的数量
     */
    public static final int WANTED_MONSTER_BOUNTY_MAX_COUNT = 0x0B;

    /**
     * 世界地图的怪物领域
     * 1Byte = 16*16小块 = 256个领域，固定无法变更
     */
    public static final int WORLD_MAP_MONSTERS_REALM_INDEX_START = 0x39243 - 0x10;
    public static final int WORLD_MAP_MONSTERS_REALM_INDEX_END = 0x39342 - 0x10;
    public static final int WORLD_MAP_MONSTERS_REALM_INDEX_MAX_COUNT = 0x100;

    private final List<Byte> worldRealms = new ArrayList<>(WORLD_MAP_MONSTERS_REALM_INDEX_MAX_COUNT);

    private final HashMap<Integer, Monster> monsters = new HashMap<>();

    /**
     * 所有怪物组合
     */
    public final MonsterGroup[] monsterGroups = new MonsterGroup[MONSTER_GROUP_MAX_COUNT];

    /**
     * 所有特殊怪物组合
     */
    public final SpecialMonsterGroup[] specialMonsterGroups = new SpecialMonsterGroup[SPECIAL_MONSTER_GROUP_MAX_COUNT];

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        monsters.clear();
        worldRealms.clear();

        byte[] speeds = new byte[MONSTER_COUNT];
        byte[] hitRates = new byte[MONSTER_COUNT];
        byte[] dropsItems = new byte[MONSTER_COUNT - 0x18];
        byte[] battleLevels = new byte[MONSTER_COUNT];

        // 读取怪物出手攻击速度
        setPrgRomPosition(MONSTER_SPEEDS_OFFSET);
        get(buffer, speeds);

        // 读取怪物命中率
//        setPrgRomPosition(MONSTER_HIT_RATES_OFFSET);
        get(buffer, hitRates);
//        setPrgRomPosition(MONSTER_BATTLE_LEVEL_OFFSET);
        get(buffer, battleLevels);

        // 读取怪物掉落物
        setPrgRomPosition(MONSTER_DROPS_ITEMS_OFFSET);
        get(buffer, dropsItems);


        for (int monsterId = 0; monsterId < MONSTER_COUNT; monsterId++) {
            Monster monster;
            if (monsterId > 0x00 && monsterId <= WANTED_MONSTER_BOUNTY_MAX_COUNT) {
                // 通缉怪物
                monster = new WantedMonster();
                // 读取赏金数据
                // 赏金怪物的ID从1开始，所以要-1
                setPrgRomPosition(WANTED_MONSTER_BOUNTY_OFFSET + (monsterId - 1));
                ((WantedMonster) monster).setBounty(get(buffer));
            } else {
                monster = new Monster();
                if (monsterId >= 0x18) {
                    // 普通怪物
                    monster.setDropsItem(dropsItems[monsterId - 0x18]);
                }
            }
            // 设置速度
            monster.setSpeed(speeds[monsterId]);
            // 设置命中率
            monster.setHitRate(hitRates[monsterId]);
            // 设置战斗等级
            monster.setBattleLevel(battleLevels[monsterId]);

            monsters.put(monsterId, monster);
        }

        // 读取领域索引
        setPrgRomPosition(WORLD_MAP_MONSTERS_REALM_INDEX_START);
        for (int i = 0; i < WORLD_MAP_MONSTERS_REALM_INDEX_MAX_COUNT; i++) {
            worldRealms.add(get(buffer));
        }

        // 读取怪物组合
        setPrgRomPosition(MONSTER_GROUP_OFFSET);
        for (int i = 0; i < MONSTER_GROUP_MAX_COUNT; i++) {
            byte[] monsters = new byte[0x0E];
            get(buffer, monsters);
            monsterGroups[i] = new MonsterGroup(monsters);
        }

        // 读取特殊怪物组合数据
        // 覆盖式读取，不需要保留现有的数据
        setPrgRomPosition(SPECIAL_MONSTER_GROUP_OFFSET);
        for (int i = 0; i < SPECIAL_MONSTER_GROUP_MAX_COUNT; i++) {
            byte[] monsters = new byte[0x04];
            byte[] counts = new byte[0x04];
            for (int j = 0; j < 0x04; j++) {
                monsters[j] = get(buffer);
                counts[j] = get(buffer);
            }
            specialMonsterGroups[i] = new SpecialMonsterGroup(monsters, counts);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        byte[] speeds = new byte[MONSTER_COUNT];
        byte[] hitRates = new byte[MONSTER_COUNT];
        byte[] dropsItems = new byte[MONSTER_COUNT - 0x18];
        byte[] bounty = new byte[WANTED_MONSTER_BOUNTY_MAX_COUNT];

        for (Map.Entry<Integer, Monster> entry : monsters.entrySet()) {
            Monster monster = entry.getValue();
            int monsterId = entry.getKey();
            speeds[monsterId] = monster.speed;
            hitRates[monsterId] = monster.hitRate;
            if (monsterId > 0x00 && monsterId <= WANTED_MONSTER_BOUNTY_MAX_COUNT && monster instanceof WantedMonster wantedMonster) {
                bounty[monsterId - 1] = wantedMonster.getBounty();
            } else {
                if (monsterId >= 0x18) {
                    dropsItems[monsterId - 0x18] = monster.dropsItem;
                }
            }
        }

        // 写入怪物出手攻击的速度
        setPrgRomPosition(MONSTER_SPEEDS_OFFSET);
        put(buffer, speeds);
        // 写入怪物的命中率
//        setPrgRomPosition(MONSTER_HIT_RATES_OFFSET);
        put(buffer, hitRates);
        // 写入怪物的掉落物
        setPrgRomPosition(MONSTER_DROPS_ITEMS_OFFSET);
        put(buffer, dropsItems);

        // 写入领域索引
        setPrgRomPosition(WORLD_MAP_MONSTERS_REALM_INDEX_START);
        for (int i = 0; i < WORLD_MAP_MONSTERS_REALM_INDEX_MAX_COUNT; i++) {
            put(buffer, worldRealms.get(i));
        }

        // 写入怪物组合
        setPrgRomPosition(MONSTER_GROUP_OFFSET);
        for (int i = 0; i < MONSTER_GROUP_MAX_COUNT; i++) {
            MonsterGroup monsterGroup = monsterGroups[i];
            put(buffer, monsterGroup.monsters);
        }

        // 写入特殊怪物组合数据
        setPrgRomPosition(SPECIAL_MONSTER_GROUP_OFFSET);
        for (int i = 0; i < SPECIAL_MONSTER_GROUP_MAX_COUNT; i++) {
            SpecialMonsterGroup specialMonsterGroup = specialMonsterGroups[i];
            for (int j = 0; j < 0x04; j++) {
                put(buffer, specialMonsterGroup.monsters[j]);
                put(buffer, specialMonsterGroup.counts[j]);
            }
        }

        // 写入赏金首的赏金数据
        setPrgRomPosition(WANTED_MONSTER_BOUNTY_OFFSET);
        put(buffer, bounty);
        return true;
    }

    /**
     * @return 所有怪物属性
     */
    public HashMap<Integer, Monster> getMonsters() {
        return monsters;
    }


    /**
     * 通过怪物ID获取怪物的属性
     *
     * @param monsterId 怪物ID
     * @return 怪物属性
     * @see #getMonster(byte)
     */
    public Monster getMonster(int monsterId) {
        return monsters.get(monsterId);
    }

    /**
     * 通过怪物ID获取怪物的属性
     *
     * @param monsterId 怪物ID
     * @return 怪物属性
     * @see #getMonster(int)
     */
    public Monster getMonster(byte monsterId) {
        return monsters.get(monsterId & 0xFF);
    }

    /**
     * @return 所有怪物组合集
     */
    public MonsterGroup[] getMonsterGroups() {
        return monsterGroups;
    }

    /**
     * 获取指定位置的怪物组合
     *
     * @param index 索引
     * @return 怪物组合
     */
    public MonsterGroup getMonsterGroup(@Range(from = 0x00, to = MONSTER_GROUP_MAX_COUNT - 1) int index) {
        return monsterGroups[index];
    }

    /**
     * 获取所有特殊怪物组合集
     *
     * @return 所有特殊怪物组合集
     */
    public SpecialMonsterGroup[] getSpecialMonsterGroups() {
        return specialMonsterGroups;
    }

    /**
     * 获取指定位置的特殊怪物组合集
     *
     * @param index 索引
     * @return 特殊怪物组合集
     */
    public SpecialMonsterGroup getSpecialMonsterGroup(@Range(from = 0x00, to = SPECIAL_MONSTER_GROUP_MAX_COUNT - 1) int index) {
        return specialMonsterGroups[index];
    }

    /**
     * @return 所有赏金首怪物的属性
     */
    public List<WantedMonster> getWantedMonsters() {
        return monsters.values().parallelStream()
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
    public WantedMonster getWantedMonster(@Range(from = 0x01, to = WANTED_MONSTER_BOUNTY_MAX_COUNT) byte monsterId) {
        return ((WantedMonster) monsters.get(monsterId & 0xFF));
    }

    public List<Byte> getWorldMapRealms() {
        return worldRealms;
    }
}
