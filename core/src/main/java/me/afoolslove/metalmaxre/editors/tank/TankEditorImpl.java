package me.afoolslove.metalmaxre.editors.tank;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
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
public class TankEditorImpl extends RomBufferWrapperAbstractEditor implements ITankEditor {
    private final DataAddress tankInitEquipmentsAddress;
    private final DataAddress taxTankInitChassisWeightsAddress;
    private final DataAddress tankInitChassisWeightsAddress;
    private final DataAddress tankInitSPAddress;
    private final DataAddress tankInitLocationsAddress;

    /**
     * NO.1 - NO.8 - TAX1 - TAXA 的初始属性
     */
    private final EnumMap<Tank, TankInitialAttribute> tankInitAttributes = new EnumMap<>(Tank.class);


    public TankEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x22010 - 0x10, 0x2207B - 0x10),
                DataAddress.fromPRG(0x220D4 - 0x10, 0x220E7 - 0x10),
                DataAddress.fromPRG(0x28142 - 0x10, 0x28151 - 0x10),
                DataAddress.fromPRG(0x28158 - 0x10, 0x28167 - 0x10),
                DataAddress.fromPRG(0x38E5C - 0x10, 0x38E76 - 0x10));
    }

    public TankEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                          @NotNull DataAddress tankInitEquipmentsAddress,
                          @NotNull DataAddress taxTankInitChassisWeightsAddress,
                          @NotNull DataAddress tankInitChassisWeightsAddress,
                          @NotNull DataAddress tankInitSPAddress,
                          @NotNull DataAddress tankInitLocationsAddress) {
        super(metalMaxRe);
        this.tankInitEquipmentsAddress = tankInitEquipmentsAddress;
        this.taxTankInitChassisWeightsAddress = taxTankInitChassisWeightsAddress;
        this.tankInitChassisWeightsAddress = tankInitChassisWeightsAddress;
        this.tankInitSPAddress = tankInitSPAddress;
        this.tankInitLocationsAddress = tankInitLocationsAddress;
    }

    @Editor.Load
    public void onLoad(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        getTankInitAttributes().clear();

        // 创建新的初始坦克属性
        TankInitialAttribute[] tankInitialAttributes = new TankInitialAttribute[Tank.values().length];
        for (Tank tank : Tank.values()) {
            // 添加
            getTankInitAttributes().put(tank, tankInitialAttributes[tank.getId() & 0xFF] = new TankInitialAttribute());
        }

        // 读取坦克初始装备（6byte）
        position(getTankInitEquipmentsAddress());
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            byte[] equipment = new byte[0x06];
            getBuffer().get(equipment);
            tankInitialAttributes[tank].setEquipment(equipment);
        }
        // 读取初始装备的 装备/卸下 状态
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            tankInitialAttributes[tank].setEquipmentState(getBuffer().get());
        }
        // 读取坦克弹仓大小
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            tankInitialAttributes[tank].setMaxShells(getBuffer().get());
        }
        // 读取坦克底盘防御力（2byte）
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            tankInitialAttributes[tank].setDefense(NumberR.toInt(getBuffer().get(), getBuffer().get()));
        }

        // 读取坦克的底盘重量
        position(getTankInitChassisWeightsAddress());
        for (int playerTank = 0; playerTank < Tank.PLAYER_TANK_COUNT; playerTank++) {
            tankInitialAttributes[playerTank].setWeight(NumberR.toInt(getBuffer().get(), getBuffer().get()));
        }

        // 读取出租坦克的底盘重量（2byte）
        position(getTaxTankInitChassisWeightsAddress());
        for (int taxTank = 0; taxTank < Tank.TAX_TANK_COUNT; taxTank++) {
            tankInitialAttributes[Tank.PLAYER_TANK_COUNT + taxTank].setWeight(NumberR.toInt(getBuffer().get(), getBuffer().get()));
        }
        // 读取坦克开洞状态
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            tankInitialAttributes[tank].setSlot(getBuffer().get());
        }

        // 读取坦克的初始SP
        position(getTankInitSPAddress());
        for (int playerTank = 0; playerTank < Tank.PLAYER_TANK_COUNT; playerTank++) {
            tankInitialAttributes[playerTank].setSp(NumberR.toInt(getBuffer().get(), getBuffer().get()));
        }

        // 读取坦克的初始坐标
        position(getTankInitLocationsAddress());
        byte[] maps = new byte[Tank.AVAILABLE_COUNT];
        byte[] xs = new byte[Tank.PLAYER_TANK_COUNT];
        byte[] ys = new byte[Tank.PLAYER_TANK_COUNT];
        getBuffer().get(maps);
        getBuffer().get(xs);
        getBuffer().get(ys);
        for (int tank = 0; tank < Tank.AVAILABLE_COUNT; tank++) {
            // 读取所在地图，包括出租坦克
            tankInitialAttributes[tank].setMap(maps[tank]);
            if (tank < Tank.PLAYER_TANK_COUNT) {
                // 读取坐标，出租坦克不能设置坐标
                tankInitialAttributes[tank].set(xs[tank], ys[tank]);
            }
        }
    }

    @Editor.Apply
    public void onApply(@NotNull ByteBuffer buffer) {
        // 写入初始属性

        TankInitialAttribute[] tankInitialAttributes = new TankInitialAttribute[Tank.ALL_COUNT];
        for (Map.Entry<Tank, TankInitialAttribute> entry : getTankInitAttributes().entrySet()) {
            tankInitialAttributes[entry.getKey().getId() & 0xFF] = entry.getValue();
        }

        // 写入坦克初始装备（6byte）
        position(getTankInitEquipmentsAddress());
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            getBuffer().put(tankInitialAttributes[tank].getEquipment());
        }
        // 写入初始装备的 装备/卸下 状态
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            getBuffer().put(tankInitialAttributes[tank].getEquipmentState());
        }
        // 写入坦克弹仓大小
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            getBuffer().put(tankInitialAttributes[tank].getMaxShells());
        }
        // 写入坦克底盘防御力（2byte）
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            getBuffer().put(tankInitialAttributes[tank].getDefenseByteArray());
        }

        // 写入坦克的底盘重量
        position(getTankInitChassisWeightsAddress());
        for (int playerTank = 0; playerTank < Tank.PLAYER_TANK_COUNT; playerTank++) {
            getBuffer().put(tankInitialAttributes[playerTank].getWeightByteArray());
        }

        // 写入出租坦克的底盘重量（2byte）
        position(getTaxTankInitChassisWeightsAddress());
        for (int taxTank = 0; taxTank < Tank.TAX_TANK_COUNT; taxTank++) {
            getBuffer().put(tankInitialAttributes[Tank.PLAYER_TANK_COUNT + taxTank].getWeightByteArray());
        }
        // 写入坦克开洞状态
        for (int tank = 0; tank < Tank.ALL_COUNT; tank++) {
            getBuffer().put(tankInitialAttributes[tank].getSlot());
        }

        // 写入坦克的初始SP
        position(getTankInitSPAddress());
        for (int playerTank = 0; playerTank < Tank.PLAYER_TANK_COUNT; playerTank++) {
            getBuffer().put(tankInitialAttributes[playerTank].getSpByteArray());
        }

        // 写入坦克的初始坐标
        position(getTankInitLocationsAddress());
        byte[] maps = new byte[Tank.AVAILABLE_COUNT];
        byte[] xs = new byte[Tank.PLAYER_TANK_COUNT];
        byte[] ys = new byte[Tank.PLAYER_TANK_COUNT];
        for (int tank = 0; tank < Tank.AVAILABLE_COUNT; tank++) {
            // 写入所在地图，包括出租坦克
            maps[tank] = tankInitialAttributes[tank].getMap();
            if (tank < Tank.PLAYER_TANK_COUNT) {
                // 写入坐标，出租坦克不能设置坐标
                xs[tank] = tankInitialAttributes[tank].getX();
                ys[tank] = tankInitialAttributes[tank].getY();
            }
        }
        getBuffer().put(maps);
        getBuffer().put(xs);
        getBuffer().put(ys);
    }

    @Override
    public DataAddress getTankInitEquipmentsAddress() {
        return tankInitEquipmentsAddress;
    }

    @Override
    public DataAddress getTaxTankInitChassisWeightsAddress() {
        return taxTankInitChassisWeightsAddress;
    }

    @Override
    public DataAddress getTankInitChassisWeightsAddress() {
        return tankInitChassisWeightsAddress;
    }

    @Override
    public DataAddress getTankInitSPAddress() {
        return tankInitSPAddress;
    }

    @Override
    public DataAddress getTankInitLocationsAddress() {
        return tankInitLocationsAddress;
    }

    @Override
    public EnumMap<Tank, TankInitialAttribute> getTankInitAttributes() {
        return tankInitAttributes;
    }
}
