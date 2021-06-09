package me.afoolslove.metalmaxre.editor.items;

import me.afoolslove.metalmaxre.Item;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.player.PlayerArmor;
import me.afoolslove.metalmaxre.editor.player.PlayerWeapon;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品编辑器
 * 玩家的武器、防具等
 * 战车的主炮、副炮、S-E
 * 玩家的道具和战车道具
 *
 * @author AFoolLove
 */
public class ItemsEditor extends AbstractEditor {
    private final PlayerItems playerItems = new PlayerItems();
    private final List<Item> items = new ArrayList<>(PlayerItems.PLAYER_ITEMS_MAX_COUNT + 0x13);

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        playerItems.armors.clear();
        playerItems.weapons.clear();
        items.clear();

        // 初始化数据
        // 初始化道具，战车还没写所以直接写道具数量 0x13
        for (int i = 0; i < PlayerItems.PLAYER_ITEMS_MAX_COUNT + 0x13; i++) {
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

        buffer.position(0x22285);
        // 读取人类防具的可装备角色的数据
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            playerItems.armors.get(i).setCanEquipped(buffer.get());
        }
        // 读取人类武器的可装备角色和武器攻击方式的数据
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            playerItems.weapons.get(i).setCanEquipped(buffer.get());
        }

        buffer.position(0x2239D);
        // 读取人类防具防御力
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            playerItems.armors.get(i).setDefense(buffer.get());
        }
        // 读取人类武器的攻击力
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            playerItems.weapons.get(i).setAttack(buffer.get());
        }

        buffer.position(0x22462);
        // 读取人类防具的价格
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            playerItems.armors.get(i).setPrice(buffer.get());
        }
        // 读取人类武器的价格
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            playerItems.weapons.get(i).setPrice(buffer.get());
        }

        buffer.position(0x224FA);
        // 读取道具的价格，战车还没写所以直接写道具数量 0x13
        for (int i = 0; i < PlayerItems.PLAYER_ITEMS_MAX_COUNT + 0x13; i++) {
            items.get(i).setPrice(buffer.get());
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        buffer.position(0x22285);
        // 写入人类防具的可装备角色的数据
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            buffer.put(playerItems.armors.get(i).canEquipped);
        }
        // 写入人类武器的可装备角色和武器攻击方式的数据
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            buffer.put(playerItems.weapons.get(i).canEquipped);
        }

        buffer.position(0x2239D);
        // 写入人类防具防御力
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            buffer.put(playerItems.armors.get(i).getDefense());
        }
        // 写入人类武器的攻击力
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            buffer.put(playerItems.weapons.get(i).getAttack());
        }

        buffer.position(0x22462);
        // 写入人类防具的价格
        for (int i = 0; i < PlayerItems.PLAYER_ARMOR_MAX_COUNT; i++) {
            buffer.put(playerItems.armors.get(i).getPrice());
        }
        // 写入人类武器的价格
        for (int i = 0; i < PlayerItems.PLAYER_WEAPON_MAX_COUNT; i++) {
            buffer.put(playerItems.weapons.get(i).getPrice());
        }

        buffer.position(0x224FA);
        // 写入道具的价格，战车还没写所以直接写道具数量 0x13
        for (int i = 0; i < PlayerItems.PLAYER_ITEMS_MAX_COUNT + 0x13; i++) {
            buffer.put(items.get(i).getPrice());
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
     * @return 道具
     */
    public List<Item> getItems() {
        return items;
    }
}
