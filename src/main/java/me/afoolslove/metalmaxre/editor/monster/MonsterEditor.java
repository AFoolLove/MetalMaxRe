package me.afoolslove.metalmaxre.editor.monster;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // 读取怪物出手攻击速度
        setPrgRomPosition(buffer, MONSTER_SPEEDS_OFFSET);
        buffer.get(speeds);

        // 读取怪物命中率
//        setPrgRomPosition(buffer, MONSTER_HIT_RATES_OFFSET);
        buffer.get(hitRates);

        // 读取怪物掉落物
        setPrgRomPosition(buffer, MONSTER_DROPS_ITEMS_OFFSET);
        buffer.get(dropsItems);

        for (int monsterId = 0; monsterId < MONSTER_COUNT; monsterId++) {
            Monster monster = new Monster();
            if (monsterId >= 0x18) {
                monster.setDropsItem(dropsItems[monsterId - 0x18]);
            }
            monster.setSpeed(speeds[monsterId]);
            monster.setHitRate(hitRates[monsterId]);
            monsters.put(monsterId, monster);
        }

        // 读取领域索引
        setPrgRomPosition(buffer, WORLD_MAP_MONSTERS_REALM_INDEX_START);
        for (int i = 0; i < WORLD_MAP_MONSTERS_REALM_INDEX_MAX_COUNT; i++) {
            worldRealms.add(buffer.get());
        }

        // 读取怪物组合
        setPrgRomPosition(buffer, MONSTER_GROUP_OFFSET);
        for (int i = 0; i < MONSTER_GROUP_MAX_COUNT; i++) {
            byte[] monsters = new byte[0x0E];
            buffer.get(monsters);
            monsterGroups[i] = new MonsterGroup(monsters);
        }

        // 读取特殊怪物组合数据
        // 覆盖式读取，不需要保留现有的数据
        setPrgRomPosition(buffer, SPECIAL_MONSTER_GROUP_OFFSET);
        for (int i = 0; i < SPECIAL_MONSTER_GROUP_MAX_COUNT; i++) {
            byte[] monsters = new byte[0x04];
            byte[] counts = new byte[0x04];
            for (int j = 0; j < 0x04; j++) {
                monsters[j] = buffer.get();
                counts[j] = buffer.get();
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

        for (Map.Entry<Integer, Monster> entry : monsters.entrySet()) {
            Monster monster = entry.getValue();
            int monsterId = entry.getKey();
            speeds[monsterId] = monster.speed;
            hitRates[monsterId] = monster.hitRate;
            if (monsterId >= 0x18) {
                dropsItems[monsterId - 0x18] = monster.dropsItem;
            }
        }

        // 写入怪物出手攻击的速度
        setPrgRomPosition(buffer, MONSTER_SPEEDS_OFFSET);
        buffer.put(speeds);
        // 写入怪物的命中率
//        setPrgRomPosition(buffer, MONSTER_HIT_RATES_OFFSET);
        buffer.put(hitRates);
        // 写入怪物的掉落物
        setPrgRomPosition(buffer, MONSTER_DROPS_ITEMS_OFFSET);
        buffer.put(dropsItems);

        // 写入领域索引
        setPrgRomPosition(buffer, WORLD_MAP_MONSTERS_REALM_INDEX_START);
        for (int i = 0; i < WORLD_MAP_MONSTERS_REALM_INDEX_MAX_COUNT; i++) {
            buffer.put(worldRealms.get(i));
        }

        // 写入怪物组合
        setPrgRomPosition(buffer, MONSTER_GROUP_OFFSET);
        for (int i = 0; i < MONSTER_GROUP_MAX_COUNT; i++) {
            MonsterGroup monsterGroup = monsterGroups[i];
            buffer.put(monsterGroup.monsters);
        }

        // 写入特殊怪物组合数据
        setPrgRomPosition(buffer, SPECIAL_MONSTER_GROUP_OFFSET);
        for (int i = 0; i < SPECIAL_MONSTER_GROUP_MAX_COUNT; i++) {
            SpecialMonsterGroup specialMonsterGroup = specialMonsterGroups[i];
            for (int j = 0; j < 0x04; j++) {
                buffer.put(specialMonsterGroup.monsters[j]);
                buffer.put(specialMonsterGroup.counts[j]);
            }
        }
        return true;
    }

    public List<Byte> getWorldMapRealms() {
        return worldRealms;
    }
}
