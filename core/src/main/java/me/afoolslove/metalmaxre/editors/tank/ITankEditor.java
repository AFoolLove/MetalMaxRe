package me.afoolslove.metalmaxre.editors.tank;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public interface ITankEditor extends IRomEditor {
    @Override
    default String getId() {
        return "tankEditor";
    }

    /**
     * 设置改造防御力梯级
     */
    void setDefenseUpStep(int defenseUpStep);

    /**
     * 设置改造弹仓容量梯级
     */
    void setShellsUpStep(int shellsUpStep);

    /**
     * 设置改造弹仓最大容量
     */
    void setMaxShells(int maxShells);

    /**
     * @return 获取防御力改造梯级，每次改造增加的防御力
     */
    byte getDefenseUpStep();

    /**
     * @return 获取弹仓容量改造梯级，每次改造增加的弹仓容量
     */
    byte getShellsUpStep();

    /**
     * @return 获取弹仓容量改造上限
     */
    byte getMaxShells();

    /**
     * @return 获取武器的炮弹容量
     */
    byte[] getWeaponShellCapacities();

    /**
     * 获取所有坦克的初始属性
     *
     * @return 所有坦克的初始属性
     */
    EnumMap<Tank, TankInitialAttribute> getTankInitAttributes();

    /**
     * 获取指定坦克的初始属性
     *
     * @return 指定坦克的初始属性
     */
    default TankInitialAttribute getTankInitAttribute(@NotNull Tank tank) {
        return getTankInitAttributes().get(tank);
    }

    /**
     * 获取指定坦克的初始属性
     *
     * @return 指定坦克的初始属性
     */
    default TankInitialAttribute getTankInitAttribute(int tankId) {
        return getTankInitAttributes().get(Tank.fromId(tankId));
    }

    /**
     * 坦克的初始装备
     * <p>
     * *包含出租坦克
     *
     * @return 坦克的初始装备
     */
    DataAddress getTankInitEquipmentsAddress();

    /**
     * @return 出租坦克的底盘重量地址（TAX1 - TAXA
     */
    DataAddress getTaxTankInitChassisWeightsAddress();

    /**
     * @return 坦克的底盘重量地址（NO.1 - NO.8
     */
    DataAddress getTankInitChassisWeightsAddress();

    /**
     * @return 坦克的初始SP地址（NO.1 - NO.8
     */
    DataAddress getTankInitSPAddress();

    /**
     * @return 坦克的初始坐标地址（NO.1 - NO.8 +（TAX1 map - TAXA map）
     */
    DataAddress getTankInitLocationsAddress();
}
