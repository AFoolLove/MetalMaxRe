package me.afoolslove.metalmaxre.editor.tank;

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
     * 0B1000_0000  主炮开洞
     * 0B0100_0000  副炮开洞
     * 0B0010_0000  S-E开洞
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
    public byte weight;
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
     * 设置装备，数量小于8时使用0x00填充
     */
    public void setEquipment(int... equipment) {
        for (int i = 0; i < 0x06; i++) {
            // 不足使用0x00填充
            this.equipment[i] = equipment.length > i ? ((byte) (equipment[i] & 0xFF)) : 0x00;
        }
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
     * 设置底盘防御力
     */
    public void setDefense(@Range(from = 0x00, to = 0xFFFF) int defense) {
        this.defense = (char) (defense & 0xFFFF);
    }

    /**
     * 设置底盘重量
     */
    public void setWeight(@Range(from = 0x00, to = 0xFF) int weight) {
        this.weight = (byte) (weight & 0xFF);
    }

    /**
     * 设置装甲片
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
    public byte getEquipment(@Range(from = 0x00, to = 0x07) int index) {
        return equipment[index];
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
        return new byte[]{(byte) (defense & 0xFF), (byte) ((defense & 0xFF00) >>> 8)};
    }

    /**
     * @return 底盘重量
     */
    public byte getWeight() {
        return weight;
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
