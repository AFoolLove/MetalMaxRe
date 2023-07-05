package me.afoolslove.metalmaxre.editors.items;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.player.PlayerArmor;
import me.afoolslove.metalmaxre.editors.player.PlayerItem;
import me.afoolslove.metalmaxre.editors.player.PlayerWeapon;
import me.afoolslove.metalmaxre.editors.tank.*;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * 物品编辑器
 *
 * @author AFoolLove
 */
public class ItemEditorImpl extends RomBufferWrapperAbstractEditor implements IItemEditor {
    private final DataAddress tankEnginesMaxCapacityAddress;
    private final DataAddress tankEnginesImprovableAddress;
    private final DataAddress playerEquipmentCanEquippedStartAddress;

    private final List<PlayerArmor> playerArmors = new ArrayList<>(getPlayerArmorMaxCount());
    private final List<PlayerWeapon> playerWeapons = new ArrayList<>(getPlayerWeaponMaxCount());
    private final List<PlayerItem> playerItems = new ArrayList<>(getPlayerItemsMaxCount());

    private final List<TankWeapon> tankWeapons = new ArrayList<>(getTankWeaponMaxCount());
    private final List<TankCUnit> tankCUnits = new ArrayList<>(getTankCUnitMaxCount());
    private final List<TankEngine> tankEngines = new ArrayList<>(getTankEngineMaxCount());
    private final List<TankChassis> tankChassis = new ArrayList<>(getTankChassisMaxCount());
    private final List<TankItem> tankItems = new ArrayList<>(getTankItemsMaxCount());

    /**
     * 道具类型
     */
    public final List<Integer> ITEM_TYPES = List.of(
            getPlayerArmorMaxCount(),
            getPlayerWeaponMaxCount(),
            getTankWeaponMaxCount(),
            getTankCUnitMaxCount(),
            getTankEngineMaxCount(),
            getTankChassisMaxCount(),
            getPlayerItemsMaxCount(),
            getTankItemsMaxCount()
    );

    public ItemEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x21804 - 0x10, 0x2181B - 0x10),
                DataAddress.fromPRG(0x30A4A - 0x10, 0x30A51 - 0x10),
                DataAddress.fromPRG(0x22285 - 0x10, 0x222A6 - 0x10));
    }

    public ItemEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                          @NotNull DataAddress tankEnginesMaxCapacityAddress,
                          @NotNull DataAddress tankEnginesImprovableAddress,
                          @NotNull DataAddress playerEquipmentCanEquippedStartAddress
    ) {
        super(metalMaxRe);
        this.tankEnginesMaxCapacityAddress = tankEnginesMaxCapacityAddress;
        this.tankEnginesImprovableAddress = tankEnginesImprovableAddress;
        this.playerEquipmentCanEquippedStartAddress = playerEquipmentCanEquippedStartAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getPlayerArmors().clear();
        getPlayerWeapons().clear();
        getTankWeapons().clear();
        getTankCUnits().clear();
        getTankEngines().clear();
        getTankChassis().clear();
        getPlayerItems().clear();
        getTankItems().clear();

        // 初始化数据
        // 初始化道具

        // 初始化玩家的防具
        for (int i = 0; i < getPlayerArmorMaxCount(); i++) {
            getPlayerArmors().add(new PlayerArmor());
        }
        // 初始化玩家的武器
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getPlayerWeapons().add(new PlayerWeapon());
        }
        // 初始化坦克武器
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getTankWeapons().add(new TankWeapon());
        }
        // 初始化坦克C装置
        for (int i = 0; i < getTankCUnitMaxCount(); i++) {
            getTankCUnits().add(new TankCUnit());
        }
        // 初始化坦克引擎
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getTankEngines().add(new TankEngine());
        }
        // 初始化坦克底盘
        for (int i = 0; i < getTankChassisMaxCount(); i++) {
            getTankChassis().add(new TankChassis());
        }
        // 初始化玩家道具
        for (int i = 0; i < getPlayerItemsMaxCount(); i++) {
            getPlayerItems().add(new PlayerItem());
        }
        // 初始化坦克道具
        for (int i = 0; i < getTankItemsMaxCount(); i++) {
            getTankItems().add(new TankItem());
        }

        // 读取坦克引擎的最大载重
        position(getTankEnginesMaxCapacityAddress());
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getTankEngines().get(i).setCapacity(getBuffer().get());
        }

        // 读取人类防具的可装备角色的数据
        position(getPlayerEquipmentCanEquippedStartAddress());
        for (int i = 0; i < getPlayerArmorMaxCount(); i++) {
            getPlayerArmors().get(i).setCanEquipped(getBuffer().get());
        }
        // 读取人类武器的可装备角色和武器攻击方式的数据
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getPlayerWeapons().get(i).setCanEquipped(getBuffer().get());
        }
        // 读取坦克可装备穴、攻击范围和弹药量
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getTankWeapons().get(i).setCanEquipped(getBuffer().get());
        }
        getBuffer().get(); // 0x222F9 不知道干嘛的
        // 读取人类武器攻击动画
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getPlayerWeapons().get(i).setAttackAnim(getBuffer().get());
        }
        // 读取坦克武器攻击动画
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getTankWeapons().get(i).setAttackAnim(getBuffer().get());
        }
        // 读取坦克的武器重量
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getTankWeapons().get(i).setWeight(getBuffer().get());
        }
        // 读取坦克的C装置重量
        for (int i = 0; i < getTankCUnitMaxCount(); i++) {
            getTankCUnits().get(i).setWeight(getBuffer().get());
        }
        // 读取坦克的引擎自重量
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getTankEngines().get(i).setWeight(getBuffer().get());
        }

        getBuffer().get(); // 0x2239C 未知
        // 读取人类防具防御力
        for (int i = 0; i < getPlayerArmorMaxCount(); i++) {
            getPlayerArmors().get(i).setDefense(getBuffer().get());
        }
        // 读取人类武器的攻击力
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getPlayerWeapons().get(i).setAttack(getBuffer().get());
        }
        // 读取坦克武器攻击力
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getTankWeapons().get(i).setAttack(getBuffer().get());
        }
        // 读取坦克武器防御力
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getTankWeapons().get(i).setDefense(getBuffer().get());
        }
        // 读取坦克C装置防御力
        for (int i = 0; i < getTankCUnitMaxCount(); i++) {
            getTankCUnits().get(i).setDefense(getBuffer().get());
        }
        // 读取坦克引擎防御力
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getTankEngines().get(i).setDefense(getBuffer().get());
        }
        getBuffer().get(); // 0x22461 未知
        // 读取人类防具的价格
        for (int i = 0; i < getPlayerArmorMaxCount(); i++) {
            getPlayerArmors().get(i).setPrice(getBuffer().get());
        }
        // 读取人类武器的价格
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getPlayerWeapons().get(i).setPrice(getBuffer().get());
        }
        // 读取坦克装备的价格
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getTankWeapons().get(i).setPrice(getBuffer().get());
        }
        // 读取坦克C装置的价格
        for (int i = 0; i < getTankCUnitMaxCount(); i++) {
            getTankCUnits().get(i).setPrice(getBuffer().get());
        }
        // 读取坦克引擎的价格
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getTankEngines().get(i).setPrice(getBuffer().get());
        }
        // 读取坦克底盘的价格
        for (int i = 0; i < getTankChassisMaxCount(); i++) {
            getTankChassis().get(i).setPrice(getBuffer().get());
        }
        // 读取玩家道具的价格
        for (int i = 0; i < getPlayerItemsMaxCount(); i++) {
            getPlayerItems().get(i).setPrice(getBuffer().get());
        }
        // 读取坦克道具的价格
        for (int i = 0; i < getTankItemsMaxCount(); i++) {
            getTankItems().get(i).setPrice(getBuffer().get());
        }

        // 读取不可改造的引擎
        position(getTankEnginesImprovableAddress());
        List<Item> items = getItems();
        for (int i = 0; i < 0x08; i++) {
            Item item = items.get(getBuffer().getToInt() - 1);
            if (item instanceof TankEngine tankEngine) {
                tankEngine.setImprovable(false);
            }
        }
    }

    @Editor.Apply
    public void onApply() {
        position(getTankEnginesMaxCapacityAddress());
        // 写入战车引擎的最大载重
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getBuffer().put(getTankEngines().get(i).getCapacity());
        }

        position(getPlayerEquipmentCanEquippedStartAddress());
        // 写入人类防具的可装备角色的数据
        for (int i = 0; i < getPlayerArmorMaxCount(); i++) {
            getBuffer().put(getPlayerArmors().get(i).getCanEquipped());
        }
        // 写入人类武器的可装备角色和武器攻击方式的数据
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getBuffer().put(getPlayerWeapons().get(i).getCanEquipped());
        }
        // 写入战车可装备穴、攻击范围和弹药量
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getBuffer().put(getTankWeapons().get(i).getCanEquipped());
        }
        getBuffer().get(); // 0x222F9 不知道干嘛的
        // 写入人类武器攻击动画
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getBuffer().put(getPlayerWeapons().get(i).getAttackAnim());
        }
        // 写入战车武器攻击动画
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getBuffer().put(getTankWeapons().get(i).getAttackAnim());
        }
        // 写入战车的武器重量
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getBuffer().put(getTankWeapons().get(i).getWeight());
        }
        // 写入战车的C装置重量
        for (int i = 0; i < getTankCUnitMaxCount(); i++) {
            getBuffer().put(getTankCUnits().get(i).getWeight());
        }
        // 写入战车的引擎自重量
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getBuffer().put(getTankEngines().get(i).getWeight());
        }
        getBuffer().get(); // 0x2239C 未知
        // 写入人类防具防御力
        for (int i = 0; i < getPlayerArmorMaxCount(); i++) {
            getBuffer().put(getPlayerArmors().get(i).getDefense());
        }
        // 写入人类武器的攻击力
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getBuffer().put(getPlayerWeapons().get(i).getAttack());
        }
        // 写入战车武器攻击力
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getBuffer().put(getTankWeapons().get(i).getAttack());
        }
        // 写入战车武器防御力
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getBuffer().put(getTankWeapons().get(i).getDefense());
        }
        // 写入战车C装置防御力
        for (int i = 0; i < getTankCUnitMaxCount(); i++) {
            getBuffer().put(getTankCUnits().get(i).getDefense());
        }
        // 写入战车引擎防御力
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getBuffer().put(getTankEngines().get(i).getDefense());
        }
        getBuffer().get(); // 0x22461 未知
        // 写入人类防具的价格
        for (int i = 0; i < getPlayerArmorMaxCount(); i++) {
            getBuffer().put(getPlayerArmors().get(i).getPrice());
        }
        // 写入人类武器的价格
        for (int i = 0; i < getPlayerWeaponMaxCount(); i++) {
            getBuffer().put(getPlayerWeapons().get(i).getPrice());
        }
        // 写入战车武器的价格
        for (int i = 0; i < getTankWeaponMaxCount(); i++) {
            getBuffer().put(getTankWeapons().get(i).getPrice());
        }
        // 写入战车C装置的价格
        for (int i = 0; i < getTankCUnitMaxCount(); i++) {
            getBuffer().put(getTankCUnits().get(i).getPrice());
        }
        // 写入战车引擎的价格
        for (int i = 0; i < getTankEngineMaxCount(); i++) {
            getBuffer().put(getTankEngines().get(i).getPrice());
        }
        // 写入战车底盘的价格
        for (int i = 0; i < getTankChassisMaxCount(); i++) {
            getBuffer().put(getTankChassis().get(i).getPrice());
        }
        // 写入玩家道具的价格
        for (int i = 0; i < getPlayerItemsMaxCount(); i++) {
            getBuffer().put(getPlayerItems().get(i).getPrice());
        }
        // 写入坦克道具的价格
        for (int i = 0; i < getTankItemsMaxCount(); i++) {
            getBuffer().put(getTankItems().get(i).getPrice());
        }

        // 写入不可改造的引擎
        position(getTankEnginesImprovableAddress());
        byte[] improvable = new byte[0x08];
        List<Item> items = getItems();
        for (int i = 0, j = 0; j < 0x08 && i < items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof TankEngine tankEngine) {
                if (!tankEngine.isImprovable()) {
                    improvable[j] = (byte) (i + 1);
                    j++;
                }
            }
        }
        getBuffer().put(improvable);
    }

    @Override
    public Item getItem(@Range(from = 0x00, to = 0xFF) int id) {
        int temp = 0; // 计数
        id--;
        for (Integer itemType : ITEM_TYPES) {
            temp += itemType;
            if (temp > id) {
                if (itemType == getPlayerArmorMaxCount()) {// 玩家防具
                    return getPlayerArmors().get(id - (temp - itemType));
                } else if (itemType == getPlayerWeaponMaxCount()) {// 玩家武器
                    return getPlayerWeapons().get(id - (temp - itemType));
                } else if (itemType == getTankWeaponMaxCount()) {// 坦克武器
                    return getTankWeapons().get(id - (temp - itemType));
                } else if (itemType == getTankCUnitMaxCount()) {// 坦克C装置
                    return getTankCUnits().get(id - (temp - itemType));
                } else if (itemType == getTankEngineMaxCount()) {// 坦克引擎
                    return getTankEngines().get(id - (temp - itemType));
                } else if (itemType == getTankChassisMaxCount()) {// 坦克底盘
                    return getTankChassis().get(id - (temp - itemType));
                } else if (itemType == getPlayerItemsMaxCount()) {// 玩家道具
                    return getPlayerItems().get(id - (temp - itemType));
                } else if (itemType == getTankItemsMaxCount()) {// 坦克道具
                    return getTankItems().get(id - (temp - itemType));
                }
            }
        }
        return null;
    }

    @Override
    public Item getItem(byte id) {
        return getItem(id & 0xFF);
    }

    @Override
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items.addAll(getPlayerArmors());
        items.addAll(getPlayerWeapons());
        items.addAll(getTankWeapons());
        items.addAll(getTankCUnits());
        items.addAll(getTankEngines());
        items.addAll(getTankChassis());
        items.addAll(getPlayerItems());
        items.addAll(getTankItems());
        return items;
    }

    @Override
    public List<PlayerArmor> getPlayerArmors() {
        return playerArmors;
    }

    @Override
    public List<PlayerWeapon> getPlayerWeapons() {
        return playerWeapons;
    }

    @Override
    public List<PlayerItem> getPlayerItems() {
        return playerItems;
    }

    @Override
    public List<TankWeapon> getTankWeapons() {
        return tankWeapons;
    }

    @Override
    public List<TankCUnit> getTankCUnits() {
        return tankCUnits;
    }

    @Override
    public List<TankEngine> getTankEngines() {
        return tankEngines;
    }

    @Override
    public List<TankChassis> getTankChassis() {
        return tankChassis;
    }

    @Override
    public List<TankItem> getTankItems() {
        return tankItems;
    }

    @Override
    public DataAddress getTankEnginesMaxCapacityAddress() {
        return tankEnginesMaxCapacityAddress;
    }

    @Override
    public DataAddress getTankEnginesImprovableAddress() {
        return tankEnginesImprovableAddress;
    }

    @Override
    public DataAddress getPlayerEquipmentCanEquippedStartAddress() {
        return playerEquipmentCanEquippedStartAddress;
    }
}
