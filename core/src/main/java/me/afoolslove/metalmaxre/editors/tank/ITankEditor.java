package me.afoolslove.metalmaxre.editors.tank;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public interface ITankEditor extends IRomEditor {
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
    default TankInitialAttribute getTankInitAttribute(@NotNull Tank tank){
        return getTankInitAttributes().get(tank);
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
