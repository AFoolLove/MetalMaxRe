package me.afoolslove.metalmaxre.editors.elevator;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElevatorEditorImpl extends RomBufferWrapperAbstractEditor implements IElevatorEditor {
    private final DataAddress elevatorTopFloorsAddress;
    private final DataAddress elevatorFloorsAddress;
    private final DataAddress elevatorMapsAddress;

    /**
     * K: top floors
     * V: floors
     */
    private final List<SingleMapEntry<Byte, List<Byte>>> floors = new ArrayList<>(getElevatorMaxCount());
    private final List<SingleMapEntry<Integer, Integer>> floorRanges = new ArrayList<>(getElevatorMaxCount());

    public ElevatorEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x20150 - 0x10, 0x25154 - 0x10),
                DataAddress.fromPRG(0x23FF0 - 0x10, 0x2400F - 0x10),
                DataAddress.fromPRG(0x29226 - 0x10, 0x2922F - 0x10)
        );
    }

    public ElevatorEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                              DataAddress elevatorTopFloorsAddress,
                              DataAddress elevatorFloorsAddress,
                              DataAddress elevatorMapsAddress
    ) {
        super(metalMaxRe);
        this.elevatorTopFloorsAddress = elevatorTopFloorsAddress;
        this.elevatorFloorsAddress = elevatorFloorsAddress;
        this.elevatorMapsAddress = elevatorMapsAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getElevatorFloors().clear();
        getElevatorFloorRanges().clear();

        // 读取电梯顶层地图ID
        byte[] topFloors = new byte[getElevatorMaxCount()];
        byte[][] floorRanges = new byte[2][getElevatorMaxCount()];
        position(getElevatorTopFloorsAddress());
        getBuffer().get(topFloors);
        // 读取电梯显示的楼层
        position(getElevatorFloorsAddress());
        for (byte topFloor : topFloors) {
            List<Byte> fs = new ArrayList<>();
            int count = getBuffer().getToInt();
            byte[] floorBytes = new byte[count];
            getBuffer().get(floorBytes);
            for (int floorByte = 0; floorByte < count; floorByte++) {
                fs.add(floorBytes[floorByte]);
            }

            getElevatorFloors().add(SingleMapEntry.create(topFloor, fs));
        }
        // 读取电梯有效范围
        position(getElevatorMapsAddress());
        getBuffer().getAABytes(0, getElevatorMaxCount(), floorRanges);
        for (int floor = 0; floor < getElevatorMaxCount(); floor++) {
            int minMap = floorRanges[0x00][floor] & 0xFF;
            int maxMap = floorRanges[0x01][floor] & 0xFF;
            getElevatorFloorRanges().add(SingleMapEntry.create(minMap, maxMap));
        }
    }

    @Editor.Apply
    public void onApply() {
        byte[] topFloors = new byte[getElevatorMaxCount()];
        byte[][] floors = new byte[getElevatorMaxCount()][];
        byte[][] floorRanges = new byte[2][getElevatorMaxCount()];

        for (int floor = 0; floor < getElevatorMaxCount(); floor++) {
            SingleMapEntry<Byte, List<Byte>> entry = getElevatorFloors().get(floor);
            topFloors[floor] = entry.getKey();
            floors[floor] = new byte[entry.getValue().size()];
            for (int f = 0; f < entry.getValue().size(); f++) {
                floors[floor][f] = entry.getValue().get(f);
            }

            SingleMapEntry<Integer, Integer> entryRange = getElevatorFloorRanges().get(floor);
            floorRanges[0x00][floor] = ((byte) (entryRange.getKey() & 0xFF));
            floorRanges[0x01][floor] = ((byte) (entryRange.getValue() & 0xFF));
        }

        // 写入电梯顶层地图ID
        position(getElevatorTopFloorsAddress());
        getBuffer().put(topFloors);
        // 写入电梯显示的楼层
        int floorCount = 5; // 最少5个
        int floorMaxLength = getElevatorFloorsAddress().length();
        position(getElevatorFloorsAddress());
        for (int i = 0; i < floors.length; i++) {
            byte[] floor = floors[i];
            int count = floor.length;

            if (floorCount >= floorMaxLength) {
                System.err.format("电梯编辑器：空间不足，无法写入第%d电梯的楼层\n", i);
                continue;
            } else if (floorMaxLength < (floorCount + count)) {
                // 空间不足，剪切楼层数量
                count = (floorCount + count) - getElevatorFloorsAddress().length();
                System.err.format("电梯编辑器：空间不足，剪切第%d电梯楼层数量为%d\n", i, count);
            }

            floorCount += count;

            getBuffer().put(count);
            getBuffer().put(floor);
        }

        int end = getElevatorFloorsAddress().getEndAddress(-position() + 0x10 + 1);
        if (end >= 0) {
            if (end > 0) {
                // 使用0xFF填充未使用的数据
                byte[] fillBytes = new byte[end];
                Arrays.fill(fillBytes, (byte) 0xFF);
                getBuffer().put(fillBytes);
            }
            System.out.printf("电梯编辑器：剩余%d个空闲字节\n", end);
        } else {
            System.err.printf("电梯编辑器：错误！超出了数据上限%d字节\n", -end);
        }

        // 写入电梯有效范围
        position(getElevatorMapsAddress());
        getBuffer().putAABytes(0, getElevatorMaxCount(), floorRanges);
    }

    @Override
    public List<SingleMapEntry<Byte, List<Byte>>> getElevatorFloors() {
        return floors;
    }

    @Override
    public List<SingleMapEntry<Integer, Integer>> getElevatorFloorRanges() {
        return floorRanges;
    }

    @Override
    public DataAddress getElevatorTopFloorsAddress() {
        return elevatorTopFloorsAddress;
    }

    @Override
    public DataAddress getElevatorFloorsAddress() {
        return elevatorFloorsAddress;
    }

    @Override
    public DataAddress getElevatorMapsAddress() {
        return elevatorMapsAddress;
    }
}
