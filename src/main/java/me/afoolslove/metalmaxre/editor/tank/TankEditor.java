package me.afoolslove.metalmaxre.editor.tank;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * 坦克编辑器
 * <p>
 * 包含出租坦克
 * <p>
 * 包括不限于坦克初始属性
 * 目前只能修改初始数据
 * <p>
 * 2021年6月3日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class TankEditor extends AbstractEditor {
    /**
     * NO.1 - NO.8 - TAX1 - TAXA 的初始属性
     */
    private final EnumMap<Tank, TankInitialAttributes> initialAttributes = new EnumMap<>(Tank.class);

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 初始化数据
        getInitialAttributes().clear();

        // 创建新的初始坦克属性
        TankInitialAttributes[] tankInitialAttributes = new TankInitialAttributes[Tank.values().length];
        for (Tank tank : Tank.values()) {
            // 添加
            getInitialAttributes().put(tank, tankInitialAttributes[tank.getId() & 0xFF] = new TankInitialAttributes());
        }

        // 读取坦克初始装备（6byte）
        buffer.position(0x22010);
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            byte[] equipment = new byte[0x06];
            buffer.get(equipment);
            tankInitialAttributes[tank].setEquipment(Arrays.copyOf(equipment, equipment.length));
        }
        // 读取初始装备的 装备/卸下 状态
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            tankInitialAttributes[tank].setEquipmentState(buffer.get());
        }
        // 读取坦克弹仓大小
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            tankInitialAttributes[tank].setMaxShells(buffer.get());
        }
        // 读取坦克底盘防御力（2byte）
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            tankInitialAttributes[tank].setDefense(buffer.get() + (buffer.get() << 8));
        }

        // 读取坦克的底盘重量
        buffer.position(0x28142);
        for (int noTank = 0; noTank < Tank.NO_COUNT; noTank++) {
            tankInitialAttributes[noTank].setWeight(buffer.get() + (buffer.get() << 8));
        }

        // 读取出租坦克的底盘重量（2byte）
        buffer.position(0x220D4);
        for (int taxTank = 0; taxTank < Tank.TAX_COUNT; taxTank++) {
            tankInitialAttributes[Tank.NO_COUNT + taxTank].setWeight(buffer.get() + (buffer.get() << 8));
        }
        // 读取坦克开洞状态
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            tankInitialAttributes[tank].setSlot(buffer.get());
        }

        // 读取坦克的初始SP
        buffer.position(0x28157);
        for (int noTank = 0; noTank < Tank.NO_COUNT; noTank++) {
            tankInitialAttributes[noTank].setSp(buffer.get() + (buffer.get() << 8));
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 写入初始属性


        TankInitialAttributes[] tankInitialAttributes = new TankInitialAttributes[Tank.values().length];
        for (Map.Entry<Tank, TankInitialAttributes> entry : getInitialAttributes().entrySet()) {
            tankInitialAttributes[entry.getKey().getId() & 0xFF] = entry.getValue();
        }

        // 写入坦克初始装备（6byte）
        buffer.position(0x22010);
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            buffer.put(tankInitialAttributes[tank].getEquipment());
        }
        // 写入初始装备的 装备/卸下 状态
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            buffer.put(tankInitialAttributes[tank].getEquipmentState());
        }
        // 写入坦克弹仓大小
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            buffer.put(tankInitialAttributes[tank].getMaxShells());
        }
        // 写入坦克底盘防御力（2byte）
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            buffer.put(tankInitialAttributes[tank].getBytesDefense());
        }

        // 写入坦克的底盘重量
        buffer.position(0x28142);
        for (int noTank = 0; noTank < Tank.NO_COUNT; noTank++) {
            buffer.put(tankInitialAttributes[noTank].getBytesWeight());
        }

        // 写入出租坦克的底盘重量（2byte）
        buffer.position(0x220D4);
        for (int taxTank = 0; taxTank < Tank.TAX_COUNT; taxTank++) {
            buffer.put(tankInitialAttributes[Tank.NO_COUNT + taxTank].getBytesWeight());
        }
        // 写入坦克开洞状态
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            buffer.put(tankInitialAttributes[tank].getSlot());
        }

        // 写入坦克的初始SP
        buffer.position(0x28158);
        for (int noTank = 0; noTank < Tank.NO_COUNT; noTank++) {
            buffer.put(tankInitialAttributes[noTank].getBytesSp());
        }
        return true;
    }

    /**
     * @return 所有坦克的初始属性
     */
    public EnumMap<Tank, TankInitialAttributes> getInitialAttributes() {
        return initialAttributes;
    }

    /**
     * @return 指定坦克的初始属性
     */
    public TankInitialAttributes getInitialAttributes(@NotNull Tank tank) {
        return initialAttributes.get(tank);
    }
}
