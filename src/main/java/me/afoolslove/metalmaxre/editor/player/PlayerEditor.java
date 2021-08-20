package me.afoolslove.metalmaxre.editor.player;

import me.afoolslove.metalmaxre.NumberR;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * 玩家编辑器
 * 包括不限于玩家初始属性
 * 目前只能修改初始数据
 * <p>
 * 2021年6月1日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class PlayerEditor extends AbstractEditor<PlayerEditor> {

    public static final int PLAYER_START_OFFSET = 0x280CD - 0x10;
    /**
     * 初始属性
     */
    private final EnumMap<Player, PlayerInitialAttributes> initialAttributes = new EnumMap<>(Player.class);

    /**
     * 玩家的初始金钱（3byte）
     */
    public int money;

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        initialAttributes.clear();

        // 读取初始属性
        PlayerInitialAttributes[] playerInitialAttributes = new PlayerInitialAttributes[Player.values().length];
        for (Player player : Player.values()) {
            playerInitialAttributes[player.getId()] = new PlayerInitialAttributes();
            initialAttributes.put(player, playerInitialAttributes[player.getId()]);
        }

        // 从初始金钱开始读取
        setPrgRomPosition(PLAYER_START_OFFSET);
        money = NumberR.toInt(get(buffer), get(buffer), get(buffer));

        // 读取初始最大生命值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setMaxHealth(NumberR.toInt(get(buffer), get(buffer)));
        }
        // 读取初始当前生命值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setHealth(NumberR.toInt(get(buffer), get(buffer)));
        }
        // 读取初始攻击力（含已装备的武器
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setAttack(NumberR.toInt(get(buffer), get(buffer)));
        }
        // 读取初始防御力（含已装备的防具
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setDefense(NumberR.toInt(get(buffer), get(buffer)));
        }
        // 读取初始队伍状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setTeamStatus(get(buffer));
        }
        // 读取初始异常状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setDeBuff(get(buffer));
        }
        // 读取初始等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setLevel(get(buffer));
        }
        // 读取初始力量
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setStrength(get(buffer));
        }
        // 读取初始智力
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setWisdom(get(buffer));
        }
        // 读取初始速度
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setSpeed(get(buffer));
        }
        // 读取初始体力
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setVitality(get(buffer));
        }
        // 读取初始战斗等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setBattleSkill(get(buffer));
        }
        // 读取初始修理等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setRepairSkill(get(buffer));
        }
        // 读取初始驾驶等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setDrivingSkill(get(buffer));
        }

        byte[] temp = new byte[0x08];
        // 读取初始装备
        for (int i = 0; i < 0x03; i++) {
            get(buffer, temp);
            playerInitialAttributes[i].setEquipment(Arrays.copyOf(temp, temp.length));
        }
        // 读取初始道具
        for (int i = 0; i < 0x03; i++) {
            get(buffer, temp);
            playerInitialAttributes[i].setInventory(Arrays.copyOf(temp, temp.length));
        }
        // 读取初始装备的装备/卸下状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setEquipmentState(get(buffer));
        }
        // 读取初始经验值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setExperience(NumberR.toInt(get(buffer), get(buffer), get(buffer)));
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 写入初始属性

        // 转换为数组
        PlayerInitialAttributes[] playerInitialAttributes = new PlayerInitialAttributes[Player.values().length];
        for (Player player : Player.values()) {
            playerInitialAttributes[player.getId()] = getInitialAttributes(player);
        }

        // 从初始金钱开始写入
        setPrgRomPosition(PLAYER_START_OFFSET);
        put(buffer, getMoneyByteArray());
        // 写入初始最大生命值
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getBytesMaxHealth());
        }
        // 写入初始当前生命值
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getBytesHealth());
        }
        // 写入初始攻击力（含已装备的武器
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getBytesAttack());
        }
        // 写入初始防御力（含已装备的防具
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getBytesDefense());
        }
        // 写入初始队伍状态
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getTeamStatus());
        }
        // 写入初始异常状态
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getDeBuff());
        }
        //  写入初始等级
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getLevel());
        }
        // 写入初始力量
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getStrength());
        }
        // 写入初始智力
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getWisdom());
        }
        // 写入初始速度
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getSpeed());
        }
        // 写入初始体力
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getVitality());
        }
        // 写入初始战斗等级
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getBattleSkill());
        }
        // 写入初始修理等级
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getRepairSkill());
        }
        // 写入初始驾驶等级
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getDrivingSkill());
        }
        // 写入初始装备
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getEquipment());
        }
        // 写入初始道具
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getInventory());
        }
        // 写入初始装备的装备状态
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getEquipmentState());
        }
        // 写入初始经验值
        for (int i = 0; i < 0x03; i++) {
            put(buffer, playerInitialAttributes[i].getBytesExperience());
        }
        return true;
    }

    /**
     * @return 所有玩家的初始属性
     */
    public Map<Player, PlayerInitialAttributes> getInitialAttributes() {
        return initialAttributes;
    }

    /**
     * @return 指定玩家的初始属性
     */
    public PlayerInitialAttributes getInitialAttributes(Player player) {
        return initialAttributes.get(player);
    }

    /**
     * 设置初始金钱
     */
    public void setMoney(@Range(from = 0x00, to = 0xFFFFFF) int money) {
        this.money = money;
    }

    /**
     * @return 金钱
     */
    @Range(from = 0x00, to = 0xFFFFFF)
    public int getMoney() {
        return money;
    }

    /**
     * @return 数组形式的金钱
     */
    public byte[] getMoneyByteArray() {
        return NumberR.toByteArray(money, 3, false);
    }
}
