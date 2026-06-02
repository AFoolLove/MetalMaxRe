package me.afoolslove.metalmaxre.editors.elevator;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.list.IListEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ElevatorEditorImpl extends RomBufferWrapperAbstractEditor implements IElevatorEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElevatorEditorImpl.class);
    /**
     * 电梯顶楼地图地址
     */
    public static final String ELEVATOR_TOP_FLOORS_ADDRESS = "elevatorTopFloors";
    /**
     * 拥有电梯属性的地图地址
     */
    public static final String ELEVATOR_MAPS_ADDRESS = "elevatorMaps";

    /**
     * top floors
     */
    private final List<Byte> topFloors = new ArrayList<>(getElevatorMaxCount());
    private final List<SingleMapEntry<Integer, Integer>> floorRanges = new ArrayList<>(getElevatorMaxCount());

    public ElevatorEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x20150 - 0x10, 0x25154 - 0x10),
                DataAddress.fromPRG(0x29226 - 0x10, 0x2922F - 0x10)
        );
    }

    public ElevatorEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                              DataAddress elevatorTopFloorsAddress,
                              DataAddress elevatorMapsAddress
    ) {
        super(metalMaxRe);
        putDataAddress(ELEVATOR_TOP_FLOORS_ADDRESS, elevatorTopFloorsAddress);
        putDataAddress(ELEVATOR_MAPS_ADDRESS, elevatorMapsAddress);
    }

    @Editor.Load
    public void onLoad(IListEditor listEditor) {
        // 读取前清空数据
        getElevatorTopFloors().clear();
        getElevatorFloorRanges().clear();

        // 读取电梯顶层地图ID
        byte[] topFloors = new byte[getElevatorMaxCount()];
        byte[][] floorRanges = new byte[2][getElevatorMaxCount()];
        position(getDataAddress(ELEVATOR_TOP_FLOORS_ADDRESS));
        getBuffer().get(topFloors);
        for (byte topFloor : topFloors) {
            getElevatorTopFloors().add(topFloor);
        }

        // 读取电梯有效范围
        position(getDataAddress(ELEVATOR_MAPS_ADDRESS));
        getBuffer().getAABytes(0, getElevatorMaxCount(), floorRanges);
        for (int floor = 0; floor < getElevatorMaxCount(); floor++) {
            int minMap = floorRanges[0x00][floor] & 0xFF;
            int maxMap = floorRanges[0x01][floor] & 0xFF;
            getElevatorFloorRanges().add(SingleMapEntry.create(minMap, maxMap));
        }
    }

    @Editor.Apply
    public void onApply(IListEditor listEditor) {
        byte[] topFloors = new byte[getElevatorMaxCount()];
        byte[][] floorRanges = new byte[2][getElevatorMaxCount()];

        for (int floor = 0; floor < getElevatorMaxCount(); floor++) {
            topFloors[floor] = getElevatorTopFloors().get(floor);

            SingleMapEntry<Integer, Integer> entryRange = getElevatorFloorRanges().get(floor);
            floorRanges[0x00][floor] = ((byte) (entryRange.getKey() & 0xFF));
            floorRanges[0x01][floor] = ((byte) (entryRange.getValue() & 0xFF));
        }

        // 写入电梯顶层地图ID
        position(getDataAddress(ELEVATOR_TOP_FLOORS_ADDRESS));
        getBuffer().put(topFloors);
        // 写入电梯有效范围
        position(getDataAddress(ELEVATOR_MAPS_ADDRESS));
        getBuffer().putAABytes(0, getElevatorMaxCount(), floorRanges);
    }

    @Override
    public List<Byte> getElevatorTopFloors() {
        return this.topFloors;
    }

    @Override
    public List<SingleMapEntry<Integer, Integer>> getElevatorFloorRanges() {
        return this.floorRanges;
    }
}
