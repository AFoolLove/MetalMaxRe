package me.afoolslove.metalmaxre.editor.player;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * 玩家编辑器
 * 包括不限于玩家初始属
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
            buffer.put((byte) (playerInitialAttributes[i].maxHealth & 0x00FF));
            buffer.put((byte) ((playerInitialAttributes[i].maxHealth & 0xFF00) >>> 8));
        }
        // 写入初始当前生命值
        for (int i = 0; i < 0x03; i++) {
            buffer.put((byte) (playerInitialAttributes[i].health & 0x00FF));
            buffer.put((byte) ((playerInitialAttributes[i].health & 0xFF00) >>> 8));
        }
        // 写入初始攻击力（含已装备的武器
        for (int i = 0; i < 0x03; i++) {
            buffer.put((byte) (playerInitialAttributes[i].attack & 0x00FF));
            buffer.put((byte) ((playerInitialAttributes[i].attack & 0xFF00) >>> 8));
        }
        // 写入初始防御力（含已装备的防具
        for (int i = 0; i < 0x03; i++) {
            buffer.put((byte) (playerInitialAttributes[i].defense & 0x00FF));
            buffer.put((byte) ((playerInitialAttributes[i].defense & 0xFF00) >>> 8));
        }
        // 写入初始队伍状态
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].teamStatus);
        }
        // 写入初始异常状态
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].deBuff);
        }
        //  写入初始等级
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].level);
        }
        // 写入初始力量
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].strength);
        }
        // 写入初始智力
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].wisdom);
        }
        // 写入初始速度
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].speed);
        }
        // 写入初始体力
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].vitality);
        }
        // 写入初始战斗等级
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].battleSkill);
        }
        // 写入初始修理等级
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].repairSkill);
        }
        // 写入初始驾驶等级
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].drivingSkill);
        }
        // 写入初始装备
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].equipment);
        }
        // 写入初始道具
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].inventory);
        }
        // 写入初始装备的装备状态
        for (int i = 0; i < 0x03; i++) {
            buffer.put(playerInitialAttributes[i].equipmentState);
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
     * 玩家初始属性
     */
    public static class PlayerInitialAttributes {
        /**
         * 装备，8byte
         */
        public byte[] equipment;
        /**
         * 装备状态
         */
        public byte equipmentState;

        /**
         * 道具，8byte
         */
        public byte[] inventory;

        /**
         * 队伍状态
         */
        public byte teamStatus;
        /**
         * 异常状态
         */
        public byte deBuff;


        /**
         * 最大生命值
         */
        public char maxHealth;
        /**
         * 生命值
         */
        public char health;

        /**
         * 攻击力（包含已装备的武器
         */
        public char attack;
        /**
         * 防御力（包含已装备的防具
         */
        public char defense;

        /**
         * 等级
         */
        public byte level;
        /**
         * 力量
         */
        public byte strength;
        /**
         * 智力
         */
        public byte wisdom;
        /**
         * 速度
         */
        public byte speed;
        /**
         * 体力
         */
        public byte vitality;

        /**
         * 战斗等级
         */
        public byte battleSkill;
        /**
         * 修理等级
         */
        public byte repairSkill;
        /**
         * 驾驶等级
         */
        public byte drivingSkill;
    }

}
