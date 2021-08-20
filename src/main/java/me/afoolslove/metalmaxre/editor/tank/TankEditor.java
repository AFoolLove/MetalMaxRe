package me.afoolslove.metalmaxre.editor.tank;

import me.afoolslove.metalmaxre.NumberR;
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
public class TankEditor extends AbstractEditor<TankEditor> {
    public static final int TANK_INIT_EQUIPMENT_START_OFFSET = 0x22010 - 0x10;

    public static final int TANK_INIT_CHASSIS_WEIGHT_START_OFFSET = 0x28142 - 0x10;
    public static final int TAX_TANK_INIT_CHASSIS_WEIGHT_START_OFFSET = 0x220D4 - 0x10;

    public static final int TANK_INIT_SP_START_OFFSET = 0x28158 - 0x10;

    /**
     * NO.1 - NO.8 - TAX1 - TAXA 的初始属性
     */
    private final EnumMap<Tank, TankInitialAttributes> initialAttributes = new EnumMap<>(Tank.class);

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        initialAttributes.clear();

        // 创建新的初始坦克属性
        TankInitialAttributes[] tankInitialAttributes = new TankInitialAttributes[Tank.values().length];
        for (Tank tank : Tank.values()) {
            // 添加
            getInitialAttributes().put(tank, tankInitialAttributes[tank.getId() & 0xFF] = new TankInitialAttributes());
        }

        // 读取坦克初始装备（6byte）
        setPrgRomPosition(TANK_INIT_EQUIPMENT_START_OFFSET);
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            byte[] equipment = new byte[0x06];
            get(buffer, equipment);
            tankInitialAttributes[tank].setEquipment(Arrays.copyOf(equipment, equipment.length));
        }
        // 读取初始装备的 装备/卸下 状态
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            tankInitialAttributes[tank].setEquipmentState(get(buffer));
        }
        // 读取坦克弹仓大小
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            tankInitialAttributes[tank].setMaxShells(get(buffer));
        }
        // 读取坦克底盘防御力（2byte）
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            tankInitialAttributes[tank].setDefense(NumberR.toInt(get(buffer), get(buffer)));
        }

        // 读取坦克的底盘重量
        setPrgRomPosition(TANK_INIT_CHASSIS_WEIGHT_START_OFFSET);
        for (int noTank = 0; noTank < Tank.NO_COUNT; noTank++) {
            tankInitialAttributes[noTank].setWeight(NumberR.toInt(get(buffer), get(buffer)));
        }

        // 读取出租坦克的底盘重量（2byte）
        setPrgRomPosition(TAX_TANK_INIT_CHASSIS_WEIGHT_START_OFFSET);
        for (int taxTank = 0; taxTank < Tank.TAX_COUNT; taxTank++) {
            tankInitialAttributes[Tank.NO_COUNT + taxTank].setWeight(NumberR.toInt(get(buffer), get(buffer)));
        }
        // 读取坦克开洞状态
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            tankInitialAttributes[tank].setSlot(get(buffer));
        }

        // 读取坦克的初始SP
        setPrgRomPosition(TANK_INIT_SP_START_OFFSET);
        for (int noTank = 0; noTank < Tank.NO_COUNT; noTank++) {
            tankInitialAttributes[noTank].setSp(NumberR.toInt(get(buffer), get(buffer)));
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

        setPrgRomPosition(TANK_INIT_EQUIPMENT_START_OFFSET);
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            put(buffer, tankInitialAttributes[tank].getEquipment());
        }
        // 写入初始装备的 装备/卸下 状态
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            put(buffer, tankInitialAttributes[tank].getEquipmentState());
        }
        // 写入坦克弹仓大小
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            put(buffer, tankInitialAttributes[tank].getMaxShells());
        }
        // 写入坦克底盘防御力（2byte）
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            put(buffer, tankInitialAttributes[tank].getDefenseByteArray());
        }

        // 写入坦克的底盘重量
        setPrgRomPosition(TANK_INIT_CHASSIS_WEIGHT_START_OFFSET);
        for (int noTank = 0; noTank < Tank.NO_COUNT; noTank++) {
            put(buffer, tankInitialAttributes[noTank].getWeightByteArray());
        }

        // 写入出租坦克的底盘重量（2byte）
        setPrgRomPosition(TAX_TANK_INIT_CHASSIS_WEIGHT_START_OFFSET);
        for (int taxTank = 0; taxTank < Tank.TAX_COUNT; taxTank++) {
            put(buffer, tankInitialAttributes[Tank.NO_COUNT + taxTank].getWeightByteArray());
        }
        // 写入坦克开洞状态
        for (int tank = 0; tank < Tank.COUNT; tank++) {
            put(buffer, tankInitialAttributes[tank].getSlot());
        }

        // 写入坦克的初始SP
        setPrgRomPosition(TANK_INIT_SP_START_OFFSET);
        for (int noTank = 0; noTank < Tank.NO_COUNT; noTank++) {
            put(buffer, tankInitialAttributes[noTank].getSpByteArray());
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
