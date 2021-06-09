package me.afoolslove.metalmaxre.editor.tank;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * 坦克编辑器
 * <p>
 * 包含出租坦克
 *
 * @author AFoolLove
 */
public class TankInitialAttributes {
    /**
     * 开洞状态
     */
    public byte slot;
    /**
     * 装备，6byte
     */
    public byte[] equipment;
    /**
     * 装备状态
     */
    public byte equipmentState;
    /**
     * 防御力
     */
    public char defense;
    /**
     * 底盘重量
     */
    public char weight;
    /**
     * 弹仓，每扩充一次增加5
     */
    public byte maxShells;

    /**
     * 装甲片
     * SoukouPointo
     */
    public char sp;

    /**
     * 设置开洞状态
     * 0B1000_0000  主炮开洞
     * 0B0100_0000  副炮开洞
     * 0B0010_0000  S-E开洞
     */
    public void setSlot(@Range(from = 0x00, to = 0xFF) int slot) {
        this.slot = (byte) (slot & 0xFF);
    }

    /**
     * 设置装备
     */
    public void setEquipment(byte[] equipment) {
        this.equipment = equipment;
    }

    /**
     * 设置装备
     */
    public void setEquipment(int mainGun, int secondaryGun, int specialEquipment, int cUnit, int engine, int chassis, boolean allEquipped) {
        setEquipment(TankEquipmentSlot.MAIN_GUN, mainGun, allEquipped);
        setEquipment(TankEquipmentSlot.SECONDARY_GUN, secondaryGun, allEquipped);
        setEquipment(TankEquipmentSlot.SPECIAL_EQUIPMENT, specialEquipment, allEquipped);
        setEquipment(TankEquipmentSlot.C_UNIT, cUnit, allEquipped);
        setEquipment(TankEquipmentSlot.ENGINE, engine, allEquipped);
        setEquipment(TankEquipmentSlot.CHASSIS, chassis, allEquipped);
    }

    /**
     * 设置某个位置的装备
     */
    public void setEquipment(@Range(from = 0x00, to = 0x05) int index, @Range(from = 0x00, to = 0xFF) int equipment) {
        this.equipment[index] = (byte) (equipment & 0xFF);
    }

    /**
     * 添加一个装备
     */
    public boolean addEquipment(@Range(from = 0x00, to = 0xFF) int equipment) {
        for (int i = 0; i < 0x06; i++) {
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
    public void removeEquipment(@Range(from = 0x00, to = 0x05) int index) {
        this.equipment[index] = 0x00;
    }

    /**
     * 移除一个装备
     */
    public void removeEquipment(@NotNull TankEquipmentSlot equipment) {
        this.equipment[equipment.getSlot()] = 0x00;
    }

    /**
     * 设置某个部位的装备
     */
    public void setEquipment(@NotNull TankEquipmentSlot slot, @Range(from = 0x00, to = 0xFF) int equipment, boolean equipped) {
        // 设置装备
        this.equipment[slot.getSlot()] = (byte) (equipment & 0xFF);
        // 装备或卸下
        if (equipped) {
            // 装备
            this.equipmentState |= slot.getStates();
        } else {
            // 卸下
            this.equipmentState &= (0B1111_0000 ^ slot.getStates());
        }
    }

    public void setEquipment(@NotNull TankEquipmentSlot slot, @Range(from = 0x00, to = 0xFF) int equipment) {
        setEquipment(slot, equipment, true);
    }


    /**
     * 设置装备的 装备/卸下 状态
     * <p>
     * 0B1000_0000  装备主炮
     * 0B0100_0000  装备副炮
     * 0B0010_0000  装备S-E
     * 0B0001_0000  装备C装置
     * 0B0000_1000  装备发动机
     * 0B0000_0100  装备底盘
     * 其它为无效属性
     * 相加即可装备
     */
    public void setEquipmentState(@Range(from = 0x00, to = 0xFF) int equipmentState) {
        this.equipmentState = (byte) (equipmentState & 0B1111_1100);
    }

    /**
     * 设置底盘防御力
     */
    public void setDefense(@Range(from = 0x00, to = 0xFFFF) int defense) {
        this.defense = (char) (defense & 0xFFFF);
    }

    /**
     * 设置底盘重量
     */
    public void setWeight(@Range(from = 0x00, to = 0xFFFF) int weight) {
        this.weight = (char) (weight & 0xFFFF);
    }

    /**
     * 设置装甲片
     * 注：当装甲片大于承载时，会显示当前的sp，而不是承载值（e.g：0/50 to 55/55
     */
    public void setSp(@Range(from = 0x00, to = 0xFFFF) int sp) {
        this.sp = (char) (sp & 0xFFFF);
    }

    /**
     * 设置最大弹仓数量，出租坦克固定无法提升
     */
    public void setMaxShells(@Range(from = 0x00, to = 0xFF) int maxShells) {
        this.maxShells = (byte) (maxShells & 0xFF);
    }

    // ----------------

    /**
     * @return 开洞状态
     */
    public byte getSlot() {
        return slot;
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
    public byte getEquipment(@Range(from = 0x00, to = 0x05) int index) {
        return equipment[index];
    }

    /**
     * @return 某个部位的装备
     */
    public byte getEquipment(@NotNull TankEquipmentSlot equipment) {
        return this.equipment[equipment.getSlot()];
    }

    /**
     * @return 装备的 装备/卸下 状态
     */
    public byte getEquipmentState() {
        return equipmentState;
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
        return new byte[]{(byte) (defense & 0x00FF), (byte) ((defense & 0xFF00) >>> 8)};
    }

    /**
     * @return 底盘重量
     */
    public char getWeight() {
        return weight;
    }

    /**
     * @return 数组形式的底盘重量
     */
    public byte[] getBytesWeight() {
        return new byte[]{(byte) (weight & 0x00FF), (byte) ((weight & 0xFF00) >>> 8)};
    }

    /**
     * @return 装甲片
     */
    public char getSp() {
        return sp;
    }

    /**
     * @return 数组形式的装甲片
     */
    public byte[] getBytesSp() {
        return new byte[]{(byte) (sp & 0x0000FF), (byte) ((sp & 0x00FF00) >>> 8)};
    }

    /**
     * @return 初始弹仓大小，出租坦克固定无法提升
     */
    public byte getMaxShells() {
        return maxShells;
    }
}
