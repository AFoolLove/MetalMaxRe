package me.afoolslove.metalmaxre.editors.treasure;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.map.MapCheckPoints;
import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 宝藏编辑器
 *
 * @author AFoolLove
 */
public class TreasureEditorImpl extends RomBufferWrapperAbstractEditor implements ITreasureEditor {
    private final DataAddress randomTreasureAddress;
    private final DataAddress checkPointsAddress;
    private final DataAddress treasureAddress;

    private final LinkedHashSet<Treasure> treasures = new LinkedHashSet<>(getTreasureMaxCount());
    /**
     * 调查的随机宝藏
     * <p>
     * K：宝藏ID
     * <p>
     * V：宝藏概率
     */
    private final List<SingleMapEntry<Byte, Byte>> randomTreasures = new ArrayList<>(getRandomTreasureMaxCount());
    /**
     * 默认调查的随机宝藏和获得其它随机宝藏的概率
     * <p>
     * K：默认宝藏ID
     * <p>
     * V：其它宝藏概率
     */
    private final SingleMapEntry<Byte, Byte> defaultRandomTreasure = new SingleMapEntry<>((byte) 0x00, (byte) 0x0C);

    /**
     * 地图的调查点，算是"宝藏"吧
     */
    private final MapCheckPoints mapCheckPoints = new MapCheckPoints();

    public TreasureEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x35AE4 - 0x10, 0x35AF0 - 0x10),
                DataAddress.fromPRG(0x35CB5 - 0x10, 0x35CC6 - 0x10),
                DataAddress.fromPRG(0x39C50 - 0x10, 0x39DBB - 0x10));
    }


    public TreasureEditorImpl(@NotNull MetalMaxRe metalMaxRe, DataAddress randomTreasureAddress, DataAddress checkPointsAddress, DataAddress treasureAddress) {
        super(metalMaxRe);
        this.randomTreasureAddress = randomTreasureAddress;
        this.checkPointsAddress = checkPointsAddress;
        this.treasureAddress = treasureAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getTreasures().clear();
        getRandomTreasures().clear();


        // 宝藏的数据按顺序存放（地图、X、Y、宝藏
        // data[0] = map
        // data[1] = x
        // data[2] = y
        // data[3] = item
        byte[][] data = new byte[4][getTreasureMaxCount()];
        getBuffer().getAABytes(getTreasureAddress().getStartAddress(), 0, getTreasureMaxCount(), data);

        for (int i = 0; i < getTreasureMaxCount(); i++) {
            Treasure treasure = new Treasure(data[0][i], data[1][i], data[2][i], data[3][i]);
            boolean add = treasures.add(treasure);
            if (!add) {
                // 重复的宝藏
                System.out.println("读取到重复的宝藏 " + treasure);
                System.out.format("位置：Map:%05X, X:%05X, Y:%05X,Item:%05X\n", treasure.getMap(), treasure.getX(), treasure.getY(), treasure.getItem());
            }
        }

        // 读取地图的调查点
        // data[0] = map
        // data[1] = x
        // data[2] = y
        position(getCheckPointsAddress());
        getBuffer().getAABytes(getCheckPointsAddress().getStartAddress(), getCheckPointMaxCount(), data[0], data[1], data[2]);

        mapCheckPoints.entrance.getKey().set(data[0][0x00], data[1][0x00], data[2][0x00]);
        mapCheckPoints.text.getKey().set(data[0][0x01], data[1][0x01], data[2][0x01]);
        mapCheckPoints.reviveCapsule.getKey().set(data[0][0x02], data[1][0x02], data[2][0x02]);
        mapCheckPoints.urumi.getKey().set(data[0][0x03], data[1][0x03], data[2][0x03]);
        mapCheckPoints.drawers.getKey().set(data[0][0x04], data[1][0x04], data[2][0x04]);
        mapCheckPoints.text2.getKey().set(data[0][0x05], data[1][0x05], data[2][0x05]);

        mapCheckPoints.entrance.getValue().setCamera(
                getBuffer().getPrg(0x35CDD - 0x10),
                getBuffer().getPrg(0x35CE1 - 0x10),
                getBuffer().getPrg(0x35CE5 - 0x10)
        );
        mapCheckPoints.reviveCapsule.setValue(getBuffer().getPrg(0x35D01 - 0x10));
        mapCheckPoints.drawers.getValue().clear();
        mapCheckPoints.drawers.getValue().addAll(Arrays.asList(
                getBuffer().getPrg(0x35D48 - 0x10),
                getBuffer().getPrg(0x35D49 - 0x10)
        ));

        // 读取随机宝藏相关数据

        // 读取默认的随机宝藏和其它随机宝藏的概率
        position(getRandomTreasureAddress());
        defaultRandomTreasure.setKey(getBuffer().get()); // 0x35AE4 - 0x10
        defaultRandomTreasure.setValue(getBuffer().getPrg(0x35ACD - 0x10));
        // 读取其它随机宝藏和概率
        // 读取其它随机宝藏

        // data[0] 和 data[1] 作为物品和物品的概率使用
        getBuffer().get(data[0], 0, getRandomTreasureMaxCount());
        getBuffer().get(data[1], 0, getRandomTreasureMaxCount());
        for (int index = 0; index < getTreasureMaxCount(); index++) {
            getRandomTreasures().add(SingleMapEntry.create(data[0][index], data[1][index]));
        }
    }

    @Editor.Apply
    public void onApply() {
        // 宝藏的数据按顺序存放（地图、X、Y、宝藏
        // data[0] = map
        // data[1] = x
        // data[2] = y
        // data[3] = item
        byte[][] data = new byte[4][getTreasureMaxCount()];

        // 优先储存后加入的
        ArrayList<Treasure> treasures = new ArrayList<>(getTreasures());
        int fromIndex = Math.max(0, treasures.size() - getTreasureMaxCount());
        for (int index = fromIndex, size = treasures.size(); index < size; index++) {
            Treasure treasure = treasures.get(index);
            data[0][index] = treasure.getMap();
            data[1][index] = treasure.getX();
            data[2][index] = treasure.getY();
            data[3][index] = treasure.getItem();
        }
        // 写入宝藏
        getBuffer().put(getTreasureAddress(), data);


        // 写入地图的调查点
        position(getCheckPointsAddress());
        List<MapPoint> checkPoints = Arrays.asList(mapCheckPoints.entrance.getKey(),
                mapCheckPoints.text.getKey(),
                mapCheckPoints.reviveCapsule.getKey(),
                mapCheckPoints.urumi.getKey(),
                mapCheckPoints.drawers.getKey(),
                mapCheckPoints.text2.getKey());

        // data[0] = map
        // data[1] = x
        // data[2] = y
        for (int index = 0; index < getCheckPointMaxCount(); index++) {
            MapPoint checkPoint = checkPoints.get(index);
            data[0][index] = checkPoint.getMap();
            data[1][index] = checkPoint.getX();
            data[2][index] = checkPoint.getY();
        }
        getBuffer().putAABytes(getCheckPointsAddress().getStartAddress(), getCheckPointMaxCount(), data[0], data[1], data[2]);

        getBuffer().putPrg(0x35CDD - 0x10, mapCheckPoints.entrance.getValue().getMap());
        getBuffer().putPrg(0x35CE1 - 0x10, mapCheckPoints.entrance.getValue().getCameraX());
        getBuffer().putPrg(0x35CE5 - 0x10, mapCheckPoints.entrance.getValue().getCameraY());

        getBuffer().putPrg(0x35D01 - 0x10, mapCheckPoints.reviveCapsule.getValue());

        prgPosition(0x35D48 - 0x10);
        for (int randomItem : mapCheckPoints.drawers.getValue().stream()
                .mapToInt(value -> value & 0xFF)
                .limit(2)
                .toArray()) {
            getBuffer().put(randomItem);
        }

        // 写入随机宝藏相关数据

        // 写入默认的随机宝藏和其它随机宝藏的概率
        position(getRandomTreasureAddress());
        getBuffer().put(getDefaultRandomTreasure().getKey()); // 0x35AE4 - 0x10
        getBuffer().putPrg(0x35ACD - 0x10, getDefaultRandomTreasure().getValue());
        // 写入其它随机宝藏和概率
        // 写入其它随机宝藏
        // 优先储存后加入的
        // data[0] 和 data[1] 作为物品和物品的概率使用
        Arrays.fill(data[0], 0x00, getRandomTreasureMaxCount(), (byte) 0x00);
        Arrays.fill(data[1], 0x00, getRandomTreasureMaxCount(), (byte) 0x00);

        fromIndex = Math.max(0, getRandomTreasures().size() - getRandomTreasureMaxCount());
        for (int index = fromIndex, size = getRandomTreasures().size(); index < size; index++) {
            SingleMapEntry<Byte, Byte> randomTreasure = getRandomTreasures().get(index);

            data[0][index] = randomTreasure.getKey();
            data[1][index] = randomTreasure.getValue();
        }
        // 写入宝藏
        getBuffer().put(data[0], 0, getRandomTreasureMaxCount());
        // 写入概率
        getBuffer().put(data[1], 0, getRandomTreasureMaxCount());
    }

    @Override
    public Set<Treasure> getTreasures() {
        return treasures;
    }

    @Override
    public List<SingleMapEntry<Byte, Byte>> getRandomTreasures() {
        return randomTreasures;
    }

    @Override
    public SingleMapEntry<Byte, Byte> getDefaultRandomTreasure() {
        return defaultRandomTreasure;
    }

    @Override
    public DataAddress getRandomTreasureAddress() {
        return randomTreasureAddress;
    }

    @Override
    public DataAddress getCheckPointsAddress() {
        return checkPointsAddress;
    }

    @Override
    public DataAddress getTreasureAddress() {
        return treasureAddress;
    }
}
