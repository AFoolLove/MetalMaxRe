package me.afoolslove.metalmaxre.editor.player;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
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
public class PlayerEditor extends AbstractEditor {
    /**
     * 初始属性
     */
    private final Map<Player, PlayerInitialAttributes> initialAttributes = new HashMap<>(Player.values().length);

    public int money;

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 初始化数据
        getInitialAttributes().clear();

        // 读取初始属性

        PlayerInitialAttributes[] playerInitialAttributes = new PlayerInitialAttributes[0x03];
        playerInitialAttributes[0x00] = new PlayerInitialAttributes();
        initialAttributes.put(Player.HANTA, playerInitialAttributes[0x00]);
        playerInitialAttributes[0x01] = new PlayerInitialAttributes();
        initialAttributes.put(Player.TAMPER, playerInitialAttributes[0x01]);
        playerInitialAttributes[0x02] = new PlayerInitialAttributes();
        initialAttributes.put(Player.ANNE, playerInitialAttributes[0x02]);

        // 从初始金钱开始读取
        buffer.position(0x280CD);
        money = buffer.get();
        money |= buffer.get() << 8;
        money |= buffer.get() << 16;

        // 读取初始最大生命值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].maxHealth = (char) (buffer.get() + (buffer.get() << 8));
        }
        // 读取初始当前生命值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].health = (char) (buffer.get() + (buffer.get() << 8));
        }
        // 读取初始攻击力（含已装备的武器
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].attack = (char) (buffer.get() + (buffer.get() << 8));
        }
        // 读取初始防御力（含已装备的防具
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].defense = (char) (buffer.get() + (buffer.get() << 8));
        }
        // 读取初始队伍状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].teamStatus = buffer.get();
        }
        // 读取初始异常状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].deBuff = buffer.get();
        }
        // 读取初始等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].level = buffer.get();
        }
        // 读取初始力量
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].strength = buffer.get();
        }
        // 读取初始智力
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].wisdom = buffer.get();
        }
        // 读取初始速度
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].speed = buffer.get();
        }
        // 读取初始体力
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].vitality = buffer.get();
        }
        // 读取初始战斗等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].battleSkill = buffer.get();
        }
        // 读取初始修理等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].repairSkill = buffer.get();
        }
        // 读取初始驾驶等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].drivingSkill = buffer.get();
        }

        byte[] temp = new byte[0x08];
        // 读取初始装备
        for (int i = 0; i < 0x03; i++) {
            buffer.get(temp);
            playerInitialAttributes[i].equipment = Arrays.copyOf(temp, temp.length);
        }
        // 读取初始道具
        for (int i = 0; i < 0x03; i++) {
            buffer.get(temp);
            playerInitialAttributes[i].inventory = Arrays.copyOf(temp, temp.length);
        }
        // 读取初始装备的装备/卸下状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].equipmentState = buffer.get();
        }
        // 读取初始经验值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].experience = buffer.get() + (buffer.get() << 8) + (buffer.get() << 16);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 写入初始属性

        PlayerInitialAttributes[] playerInitialAttributes = new PlayerInitialAttributes[0x03];
        playerInitialAttributes[0x00] = getInitialAttributes(Player.HANTA);
        playerInitialAttributes[0x01] = getInitialAttributes(Player.TAMPER);
        playerInitialAttributes[0x02] = getInitialAttributes(Player.ANNE);

        // 从初始金钱开始写入
        buffer.position(0x280CD);
        buffer.put((byte) (money & 0x0000FF));
        buffer.put((byte) ((money & 0x00FF00) >>> 8));
        buffer.put((byte) ((money & 0xFF0000) >>> 16));
        // 写入初始最大生命值
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getBytesMaxHealth());
        }
        // 写入初始当前生命值
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getBytesHealth());
        }
        // 写入初始攻击力（含已装备的武器
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getBytesAttack());
        }
        // 写入初始防御力（含已装备的防具
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getBytesDefense());
        }
        // 写入初始队伍状态
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getTeamStatus());
        }
        // 写入初始异常状态
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getDeBuff());
        }
        //  写入初始等级
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getLevel());
        }
        // 写入初始力量
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getStrength());
        }
        // 写入初始智力
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getWisdom());
        }
        // 写入初始速度
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getSpeed());
        }
        // 写入初始体力
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getVitality());
        }
        // 写入初始战斗等级
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getBattleSkill());
        }
        // 写入初始修理等级
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getRepairSkill());
        }
        // 写入初始驾驶等级
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getDrivingSkill());
        }
        // 写入初始装备
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getEquipment());
        }
        // 写入初始道具
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getInventory());
        }
        // 写入初始装备的装备状态
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getEquipmentState());
        }
        // 写入初始经验值
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].getBytesExperience());
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

}
