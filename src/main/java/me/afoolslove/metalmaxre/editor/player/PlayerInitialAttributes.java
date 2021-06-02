package me.afoolslove.metalmaxre.editor.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * 玩家初始属性
 *
 * @author AFoolLove
 */
public class PlayerInitialAttributes {
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

    /**
     * 经验值
     */
    public int experience;

    /**
     * 设置最大生命值
     */
    public void setMaxHealth(@Range(from = 0x00, to = 0xFFFF) int maxHealth) {
        this.maxHealth = (char) (maxHealth & 0xFFFF);
    }

    /**
     * 设置当前生命值
     */
    public void setHealth(@Range(from = 0x00, to = 0xFFFF) int health) {
        this.health = (char) (health & 0xFFFF);
    }

    /**
     * 设置攻击力
     * 攻击力包含已装备的武器
     * ？！不能去观测，否则会恢复！？
     */
    public void setAttack(@Range(from = 0x00, to = 0xFFFF) int attack) {
        this.attack = (char) (attack & 0xFFFF);
    }

    /**
     * 设置防御力
     * 防御力包含已装备的防具
     * ？！不能去观测，否则会恢复！？
     */
    public void setDefense(@Range(from = 0x00, to = 0xFFFF) int defense) {
        this.defense = (char) (defense & 0xFFFF);
    }

    /**
     * 设置该玩家的队伍状态
     * 需要详细测试
     * 基本为：01 02 03，分别会加入队伍
     */
    public void setTeamStatus(@Range(from = 0x00, to = 0xFF) int teamStatus) {
        this.teamStatus = (byte) (teamStatus & 0xFF);
    }

    /**
     * 设置 Debuff
     */
    public void setDeBuff(@Range(from = 0x00, to = 0xFF) int deBuff) {
        // ！弃用 0B0000_0001！
        this.deBuff = (byte) (deBuff & 0B1111_1110);
    }

    /**
     * 设置 Debuff
     */
    public void setDeBuff(@Range(from = 0x00, to = 0xFF) @NotNull PlayerDeBuff deBuff) {
        this.deBuff = deBuff.getDeBuff();
    }

    /**
     * 设置等级
     */
    public void setLevel(@Range(from = 0x00, to = 0xFF) int level) {
        this.level = (byte) (level & 0xFF);
    }

    /**
     * 设置力量
     */
    public void setStrength(@Range(from = 0x00, to = 0xFF) int strength) {
        this.strength = (byte) (strength & 0xFF);
    }

    /**
     * 设置智力
     */
    public void setWisdom(@Range(from = 0x00, to = 0xFF) int wisdom) {
        this.wisdom = (byte) (wisdom & 0xFF);
    }

    /**
     * 设置速度
     */
    public void setSpeed(@Range(from = 0x00, to = 0xFF) int speed) {
        this.speed = (byte) (speed & 0xFF);
    }

    /**
     * 设置体力
     */
    public void setVitality(@Range(from = 0x00, to = 0xFF) int vitality) {
        this.vitality = (byte) (vitality & 0xFF);
    }

    /**
     * 设置战斗等级
     */
    public void setBattleSkill(@Range(from = 0x00, to = 0xFF) int battleSkill) {
        this.battleSkill = (byte) (battleSkill & 0xFF);
    }

    /**
     * 设置修理等级
     */
    public void setRepairSkill(@Range(from = 0x00, to = 0xFF) int repairSkill) {
        this.repairSkill = (byte) (repairSkill & 0xFF);
    }

    /**
     * 设置驾驶等级
     */
    public void setDrivingSkill(@Range(from = 0x00, to = 0xFF) int drivingSkill) {
        this.drivingSkill = (byte) (drivingSkill & 0xFF);
    }

    /**
     * 设置装备
     */
    public void setEquipment(byte[] equipment) {
        this.equipment = equipment;
    }

    /**
     * 设置装备，数量小于8时使用0x00填充
     */
    public void setEquipment(int... equipment) {
        for (int i = 0; i < 0x08; i++) {
            // 不足使用0x00填充
            this.equipment[i] = equipment.length > i ? ((byte) (equipment[i] & 0xFF)) : 0x00;
        }
    }

    /**
     * 设置某个位置的装备
     */
    public void setEquipment(@Range(from = 0x00, to = 0x07) int index, @Range(from = 0x00, to = 0xFF) int equipment) {
        this.equipment[index] = (byte) (equipment & 0xFF);
    }

    /**
     * 添加一个装备
     */
    public boolean addEquipment(int equipment) {
        for (int i = 0; i < 0x08; i++) {
            // 替换空闲的装备栏
            if (this.equipment[i] == 0x00) {
                this.equipment[i] = (byte) (equipment & 0xFF);
                return true;
            }
        }
        return false;
    }

    /**
     * 移除一个装备
     */
    public void removeEquipment(@Range(from = 0x00, to = 0x07) int index) {
        this.equipment[index] = 0x00;
    }

    /**
     * 设置道具
     */
    public void setInventory(byte[] inventory) {
        this.inventory = inventory;
    }

    /**
     * 设置道具，数量小于8时使用0x00填充
     */
    public void setInventory(int... inventory) {
        for (int i = 0; i < 0x08; i++) {
            // 不足使用0x00填充
            this.inventory[i] = inventory.length > i ? ((byte) (inventory[i] & 0xFF)) : 0x00;
        }
    }

    /**
     * 设置某个位置的道具
     */
    public void setInventory(@Range(from = 0x00, to = 0x07) int index, @Range(from = 0x00, to = 0xFF) int inventory) {
        this.inventory[index] = (byte) (inventory & 0xFF);
    }

    /**
     * 添加一个道具
     */
    public boolean addInventory(int inventory) {
        for (int i = 0; i < 0x08; i++) {
            // 替换空闲的道具栏
            if (this.inventory[i] == 0x00) {
                this.inventory[i] = (byte) (inventory & 0xFF);
                return true;
            }
        }
        return false;
    }

    /**
     * 移除一个道具
     */
    public void removeInventory(@Range(from = 0x00, to = 0x07) int index) {
        this.inventory[index] = 0x00;
    }

    /**
     * 设置装备的 装备/卸下 状态
     * <p>
     * 0x80 0x40
     * 0x20 0x10
     * 0x08 0x04
     * 0x02 0x01
     * 相加即可装备
     */
    public void setEquipmentState(@Range(from = 0x00, to = 0xFF) int equipmentState) {
        this.equipmentState = (byte) (equipmentState & 0xFF);
    }

    /**
     * 设置经验值
     */
    public void setExperience(@Range(from = 0x00, to = 0xFFFFFF) int experience) {
        this.experience = experience & 0xFFFFFF;
    }

    //---------------------

    /**
     * @return 最大生命值
     */
    @Range(from = 0x00, to = 0xFFFF)
    public char getMaxHealth() {
        return maxHealth;
    }

    /**
     * @return 数组形式的最大生命值
     */
    public byte[] getBytesMaxHealth() {
        return new byte[]{(byte) (maxHealth & 0xFF), (byte) ((maxHealth & 0xFF00) >>> 8)};
    }

    /**
     * @return 当前生命值
     */
    @Range(from = 0x00, to = 0xFFFF)
    public char getHealth() {
        return health;
    }

    /**
     * @return 数组形式的当前生命值
     */
    public byte[] getBytesHealth() {
        return new byte[]{(byte) (health & 0xFF), (byte) ((health & 0xFF00) >>> 8)};
    }

    /**
     * @return 攻击力
     */
    @Range(from = 0x00, to = 0xFFFF)
    public char getAttack() {
        return attack;
    }

    /**
     * @return 数组形式的攻击力
     */
    public byte[] getBytesAttack() {
        return new byte[]{(byte) (attack & 0xFF), (byte) ((attack & 0xFF00) >>> 8)};
    }

    /**
     * @return 防御力
     */
    @Range(from = 0x00, to = 0xFFFF)
    public char getDefense() {
        return defense;
    }

    /**
     * @return 数组形式的防御力
     */
    public byte[] getBytesDefense() {
        return new byte[]{(byte) (defense & 0xFF), (byte) ((defense & 0xFF00) >>> 8)};
    }

    /**
     * @return 队伍状态
     */
    public byte getTeamStatus() {
        return teamStatus;
    }

    /**
     * @return DeBuff
     */
    public byte getDeBuff() {
        return deBuff;
    }

    /**
     * @return 枚举形式的DeBuff
     */
    public PlayerDeBuff getPlayerDeBuff() {
        return PlayerDeBuff.getDeBuff(deBuff);
    }

    /**
     * @return 等级
     */
    public byte getLevel() {
        return level;
    }

    /**
     * @return 力量
     */
    public byte getStrength() {
        return strength;
    }

    /**
     * @return 智力
     */
    public byte getWisdom() {
        return wisdom;
    }

    /**
     * @return 速度
     */
    public byte getSpeed() {
        return speed;
    }

    /**
     * @return 体力
     */
    public byte getVitality() {
        return vitality;
    }

    /**
     * @return 战斗等级
     */
    public byte getBattleSkill() {
        return battleSkill;
    }

    /**
     * @return 修理等级
     */
    public byte getRepairSkill() {
        return repairSkill;
    }

    /**
     * @return 驾驶等级
     */
    public byte getDrivingSkill() {
        return drivingSkill;
    }

    /**
     * @return 装备
     */
    public byte[] getEquipment() {
        return equipment;
    }

    /**
     * @return 指定位置的装备
     */
    public byte getEquipment(@Range(from = 0x00, to = 0x07) int index) {
        return equipment[index];
    }

    /**
     * @return 道具
     */
    public byte[] getInventory() {
        return inventory;
    }

    /**
     * @return 指定位置的道具
     */
    public byte getInventory(@Range(from = 0x00, to = 0x07) int index) {
        return inventory[index];
    }

    /**
     * @return 装备的 装备/卸下 状态
     */
    public byte getEquipmentState() {
        return equipmentState;
    }

    /**
     * @return 经验值
     */
    @Range(from = 0x00, to = 0xFFFFFF)
    public int getExperience() {
        return experience;
    }

    /**
     * @return 数组形式的经验值
     */
    public byte[] getBytesExperience() {
        return new byte[]{(byte) (experience & 0x0000FF), (byte) ((experience & 0x00FF00) >>> 8), (byte) ((experience & 0xFF0000) >>> 16)};
    }
}
