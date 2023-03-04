package me.afoolslove.metalmaxre.editors.elevator;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;

import java.util.List;

/**
 * 电梯编辑器
 *
 * @author AFoolLove
 */
public interface IElevatorEditor extends IRomEditor {
    /**
     * @return 电梯的数量
     */
    default int getElevatorMaxCount() {
        return 0x05;
    }

    /**
     * @return 所有电梯和对应的楼层
     */
    List<SingleMapEntry<Byte, List<Byte>>> getElevatorFloors();

    /**
     * @return 所有电梯对应的有效范围地图
     */
    List<SingleMapEntry<Integer, Integer>> getElevatorFloorRanges();

    /**
     * @return 所有电梯顶楼地图地址
     */
    DataAddress getElevatorTopFloorsAddress();

    /**
     * @return 所有电梯显示的楼层地址
     */
    DataAddress getElevatorFloorsAddress();

    /**
     * @return 拥有电梯属性的地图地址
     */
    DataAddress getElevatorMapsAddress();

}
