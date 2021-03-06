package me.afoolslove.metalmaxre.editor.items;

import me.afoolslove.metalmaxre.Item;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.player.PlayerArmor;
import me.afoolslove.metalmaxre.editor.player.PlayerWeapon;
import me.afoolslove.metalmaxre.editor.tank.TankEngine;
import me.afoolslove.metalmaxre.editor.tank.TankEquipmentItem;
import me.afoolslove.metalmaxre.editor.tank.TankWeapon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品编辑器
 * 玩家的武器、防具等
 * 战车的主炮、副炮、S-E
 * 玩家的道具和战车道具
 * <p>
 * 起始：0x21804
 * 结束：0x22285
 * <p>
 * 2021年6月9日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class ItemsEditor extends AbstractEditor<ItemsEditor> {
    /**
     * 坦克引擎的最大载重
     */
    public static final int TANK_ENGINE_MAX_CAPACITY_START_OFFSET = 0x21804 - 0x10;
    public static final int TANK_ENGINE_MAX_CAPACITY_END_OFFSET = 0x2181B - 0x10;

    /**
     * 各玩家可以穿戴的装备状态
     */
    public static final int PLAYER_EQUIPMENT_CAN_EQUIPPED_START_OFFSET = 0x22285 - 0x10;
    public static final int PLAYER_EQUIPMENT_CAN_EQUIPPED_END_OFFSET = 0x222A6 - 0x10;

    /**
     * 道具类型
     */
    public static final List<Integer> ITEM_TYPES = List.of(
            PlayerItems.PLAYER_ARMOR_MAX_COUNT,
            PlayerItems.PLAYER_WEAPON_MAX_COUNT,
            TankItems.TANK_WEAPON_MAX_COUNT,
            TankItems.TANK_C_UNIT_MAX_COUNT,
            TankItems.TANK_ENGINE_MAX_COUNT,
            TankItems.TANK_CHASSIS_MAX_COUNT,
            PlayerItems.PLAYER_ITEMS_MAX_COUNT,
            TankItems.TANK_ITEMS_MAX_COUNT
    );

    /**
     * 玩家的物品（武器、装备）
     */
    private final PlayerItems playerItems = new PlayerItems();
    /**
     * 坦克的物品（装备、C装置、引擎、底盘）
     */
    private final TankItems tankItems = new TankItems();
    /**
     * 玩家和坦克的道具
     */
    private final List<Item> items = new ArrayList<>(PlayerItems.PLAYER_ITEMS_MAX_COUNT + TankItems.TANK_ITEMS_MAX_COUNT);

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        playerItems.armors.clear();
        playerItems.weapons.clear();
        tankItems.weapons.clear();
        tankItems.cUnits.clear();
        tankItems.engines.clear();
        tankItems.chassis.clear();
        items.clear();

        // 初始化数据
        // 初始化道具
        for (int i = 0; i < PlayerItems.PLAYER_ITEMS_MAX_COUNT + TankItems.TANK_ITEMS_MAX_COUNT; i++) {
            items.add(i, new Item());
        }
        // 初始化玩家的防具
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            playerItems.armors.add(i, new PlayerArmor());
        }
        // 初始化玩家的武器
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            playerItems.weapons.add(i, new PlayerWeapon());
        }
        // 初始化坦克装备
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            tankItems.weapons.add(i, new TankWeapon());
        }
        // 初始化坦克C装置
        for (int i = 0; i < TankItems.TANK_C_UNIT_MAX_COUNT; i++) {
            tankItems.cUnits.add(i, new TankEquipmentItem());
        }
        // 初始化坦克引擎
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            tankItems.engines.add(i, new TankEngine());
        }
        // 初始化坦克底盘
        for (int i = 0; i < TankItems.TANK_CHASSIS_MAX_COUNT; i++) {
            tankItems.chassis.add(i, new Item());
        }

        // 读取战车引擎的最大载重
        setPrgRomPosition(TANK_ENGINE_MAX_CAPACITY_START_OFFSET);
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            tankItems.engines.get(i).setCapacity(get(buffer));
        }

        // 读取人类防具的可装备角色的数据
        setPrgRomPosition(PLAYER_EQUIPMENT_CAN_EQUIPPED_START_OFFSET);
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            playerItems.armors.get(i).setCanEquipped(get(buffer));
        }
        // 读取人类武器的可装备角色和武器攻击方式的数据
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            playerItems.weapons.get(i).setCanEquipped(get(buffer));
        }
        // 读取战车可装备穴、攻击范围和弹药量
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            tankItems.weapons.get(i).setCanEquipped(get(buffer));
        }
        get(buffer); // 0x222F9 不知道干嘛的
        // 读取人类武器攻击动画
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            playerItems.weapons.get(i).setAttackAnim(get(buffer));
        }
        // 读取战车武器攻击动画
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            tankItems.weapons.get(i).setAttackAnim(get(buffer));
        }
        // 读取战车的武器重量
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            tankItems.weapons.get(i).setWeight(get(buffer));
        }
        // 读取战车的C装置重量
        for (int i = 0; i < TankItems.TANK_C_UNIT_MAX_COUNT; i++) {
            tankItems.cUnits.get(i).setWeight(get(buffer));
        }
        // 读取战车的引擎自重量
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            tankItems.engines.get(i).setWeight(get(buffer));
        }

        get(buffer); // 0x2239C 未知
        // 读取人类防具防御力
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            playerItems.armors.get(i).setDefense(get(buffer));
        }
        // 读取人类武器的攻击力
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            playerItems.weapons.get(i).setAttack(get(buffer));
        }
        // 读取战车武器攻击力
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            tankItems.weapons.get(i).setAttack(get(buffer));
        }
        // 读取战车武器防御力
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            tankItems.weapons.get(i).setDefense(get(buffer));
        }
        // 读取战车C装置防御力
        for (int i = 0; i < TankItems.TANK_C_UNIT_MAX_COUNT; i++) {
            tankItems.cUnits.get(i).setDefense(get(buffer));
        }
        // 读取战车引擎防御力
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            tankItems.engines.get(i).setDefense(get(buffer));
        }
        get(buffer); // 0x22461 未知
        // 读取人类防具的价格
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            playerItems.armors.get(i).setPrice(get(buffer));
        }
        // 读取人类武器的价格
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            playerItems.weapons.get(i).setPrice(get(buffer));
        }
        // 读取战车装备的价格
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            tankItems.weapons.get(i).setPrice(get(buffer));
        }
        // 读取战车C装置的价格
        for (int i = 0; i < TankItems.TANK_C_UNIT_MAX_COUNT; i++) {
            tankItems.cUnits.get(i).setPrice(get(buffer));
        }
        // 读取战车引擎的价格
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            tankItems.engines.get(i).setPrice(get(buffer));
        }
        // 读取战车底盘的价格
        for (int i = 0; i < TankItems.TANK_CHASSIS_MAX_COUNT; i++) {
            tankItems.chassis.get(i).setPrice(get(buffer));
        }
        // 读取道具的价格，战车还没写所以直接写道具数量 0x13
        for (int i = 0; i < PlayerItems.PLAYER_ITEMS_MAX_COUNT + TankItems.TANK_ITEMS_MAX_COUNT; i++) {
            items.get(i).setPrice(get(buffer));
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        setPrgRomPosition(TANK_ENGINE_MAX_CAPACITY_START_OFFSET);
        // 写入战车引擎的最大载重
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            put(buffer, tankItems.engines.get(i).getCapacity());
        }

        setPrgRomPosition(PLAYER_EQUIPMENT_CAN_EQUIPPED_START_OFFSET);
        // 写入人类防具的可装备角色的数据
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            put(buffer, playerItems.armors.get(i).canEquipped);
        }
        // 写入人类武器的可装备角色和武器攻击方式的数据
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            put(buffer, playerItems.weapons.get(i).canEquipped);
        }
        // 写入战车可装备穴、攻击范围和弹药量
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            put(buffer, tankItems.weapons.get(i).getCanEquipped());
        }
        skip(); // 0x222F9 不知道干嘛的
        // 写入人类武器攻击动画
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            put(buffer, playerItems.weapons.get(i).getAttackAnim());
        }
        // 写入战车武器攻击动画
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            put(buffer, tankItems.weapons.get(i).getAttackAnim());
        }
        // 写入战车的武器重量
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            put(buffer, tankItems.weapons.get(i).getWeight());
        }
        // 写入战车的C装置重量
        for (int i = 0; i < TankItems.TANK_C_UNIT_MAX_COUNT; i++) {
            put(buffer, tankItems.cUnits.get(i).getWeight());
        }
        // 写入战车的引擎自重量
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            put(buffer, tankItems.engines.get(i).getWeight());
        }
        skip(); // 0x2239C 未知
        // 写入人类防具防御力
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            put(buffer, playerItems.armors.get(i).getDefense());
        }
        // 写入人类武器的攻击力
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            put(buffer, playerItems.weapons.get(i).getAttack());
        }
        // 写入战车武器攻击力
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            put(buffer, tankItems.weapons.get(i).getAttack());
        }
        // 写入战车武器防御力
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            put(buffer, tankItems.weapons.get(i).getDefense());
        }
        // 写入战车C装置防御力
        for (int i = 0; i < TankItems.TANK_C_UNIT_MAX_COUNT; i++) {
            put(buffer, tankItems.cUnits.get(i).getDefense());
        }
        // 写入战车引擎防御力
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            put(buffer, tankItems.engines.get(i).getDefense());
        }
        skip(); // 0x22461 未知
        // 写入人类防具的价格
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            put(buffer, playerItems.armors.get(i).getPrice());
        }
        // 写入人类武器的价格
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            put(buffer, playerItems.weapons.get(i).getPrice());
        }
        // 写入战车装备的价格
        for (int i = 0; i < TankItems.TANK_WEAPON_MAX_COUNT; i++) {
            put(buffer, tankItems.weapons.get(i).getPrice());
        }
        // 写入战车C装置的价格
        for (int i = 0; i < TankItems.TANK_C_UNIT_MAX_COUNT; i++) {
            put(buffer, tankItems.cUnits.get(i).getPrice());
        }
        // 写入战车引擎的价格
        for (int i = 0; i < TankItems.TANK_ENGINE_MAX_COUNT; i++) {
            put(buffer, tankItems.engines.get(i).getPrice());
        }
        // 写入战车底盘的价格
        for (int i = 0; i < TankItems.TANK_CHASSIS_MAX_COUNT; i++) {
            put(buffer, tankItems.chassis.get(i).getPrice());
        }
        // 写入道具的价格
        for (int i = 0; i < PlayerItems.PLAYER_ITEMS_MAX_COUNT + TankItems.TANK_ITEMS_MAX_COUNT; i++) {
            put(buffer, items.get(i).getPrice());
        }
        return true;
    }


    /**
     * @return 玩家的武器和装备
     */
    public PlayerItems getPlayerItems() {
        return playerItems;
    }

    /**
     * @return 坦克的武器、C装置、引擎和底盘
     */
    public TankItems getTankItems() {
        return tankItems;
    }

    /**
     * @return 坦克和人类道具
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @return 所有物品
     */
    public List<Item> getAllItems() {
        List<Item> list = new ArrayList<>(getAllItemsCount());
        list.addAll(playerItems.armors);
        list.addAll(playerItems.weapons);
        list.addAll(tankItems.weapons);
        list.addAll(tankItems.cUnits);
        list.addAll(tankItems.engines);
        list.addAll(tankItems.chassis);
        list.addAll(items); // player items + tank items
        return list;
    }

    /**
     * 获取某个物品
     *
     * @param item 物品id
     * @return 物品
     */
    public Item getItem(byte item) {
        return getItem(item & 0xFF);
    }

    /**
     * 获取某个物品
     *
     * @param item 物品id
     * @return 物品
     */
    public Item getItem(@Range(from = 0x01, to = 0xFF) int item) {
        int temp = 0; // 计数
        item--;
        for (Integer itemType : ITEM_TYPES) {
            temp += itemType;
            if (temp > item) {
                switch (itemType) {
                    case PlayerItems.PLAYER_ARMOR_MAX_COUNT:
                        // 玩家防具
                        return getPlayerItems().getArmors().get(item - (temp - itemType));
                    case PlayerItems.PLAYER_WEAPON_MAX_COUNT:
                        // 玩家武器
                        return getPlayerItems().getWeapons().get(item - (temp - itemType));
                    case TankItems.TANK_WEAPON_MAX_COUNT:
                        // 武器
                        return getTankItems().getWeapons().get(item - (temp - itemType));
                    case TankItems.TANK_C_UNIT_MAX_COUNT:
                        // C装置
                        return getTankItems().getCUnits().get(item - (temp - itemType));
                    case TankItems.TANK_ENGINE_MAX_COUNT:
                        // 引擎
                        return getTankItems().getEngines().get(item - (temp - itemType));
                    case TankItems.TANK_CHASSIS_MAX_COUNT:
                        // 底盘
                        return getTankItems().getChassis().get(item - (temp - itemType));

                    case PlayerItems.PLAYER_ITEMS_MAX_COUNT:
                    case TankItems.TANK_ITEMS_MAX_COUNT:
                        // 玩家和坦克的道具
                        return getItems().get(item - (temp - itemType));
                    default:
                        break;
                }
            }
        }
        return null;
    }

    /**
     * @return 所有物品的总数量
     */
    public int getAllItemsCount() {
        return PlayerItems.PLAYER_ARMOR_MAX_COUNT +
                PlayerItems.PLAYER_WEAPON_MAX_COUNT +
                TankItems.TANK_WEAPON_MAX_COUNT +
                TankItems.TANK_C_UNIT_MAX_COUNT +
                TankItems.TANK_ENGINE_MAX_COUNT +
                TankItems.TANK_CHASSIS_MAX_COUNT +
                PlayerItems.PLAYER_ITEMS_MAX_COUNT +
                TankItems.TANK_ITEMS_MAX_COUNT;
    }
}
