package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.AbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 犬系统编辑器
 * <p>
 * 修改时空隧道机器目的地和犬系统传送的目的地
 * <p>
 * 时空隧道机器一共有基础的12个目的地+1个故障目的地
 * <p>
 * 犬系统目的地一共有基础的12个目的地
 *
 * @author AFoolLove
 */
public class DogSystemEditorImpl extends AbstractEditor implements IDogSystemEditor {
    @NotNull
    protected DataAddress townsAddress;
    @NotNull
    protected DataAddress townLocationsAddress;
    @NotNull
    protected DataAddress teleportLocationAddress;

    private final List<CameraMapPoint> townLocations = new ArrayList<>(getTownMaxCount());
    private final List<CameraMapPoint> teleportLocations = new ArrayList<>(getTownMaxCount());
    private final List<SingleMapEntry<Byte, Byte>> towns = new ArrayList<>(getTownMaxCount());
    private final List<SingleMapEntry<Byte, Byte>> townSeries = new ArrayList<>(0x02);

    public DogSystemEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.from(0x34707 - 0x10, 0x34712 - 0x10),
                DataAddress.from(0x3272D - 0x10, 0x32738 - 0x10),
                DataAddress.from(0x3538C - 0x10, 0x353B8 - 0x10)
        );
    }

    public DogSystemEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                               @NotNull DataAddress townsAddress,
                               @NotNull DataAddress townLocationsAddress,
                               @NotNull DataAddress teleportLocationAddress) {
        super(metalMaxRe);
        this.townsAddress = townsAddress;
        this.townLocationsAddress = townLocationsAddress;
        this.teleportLocationAddress = teleportLocationAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 初始化数据
        getTowns().clear();
        getTownLocations().clear();
        getTeleportLocations().clear();
        getTownSeries().clear();

        for (int i = 0; i < getTeleportMaxCount(); i++) {
            if (i < getTownMaxCount()) {
                getTowns().add(SingleMapEntry.createEmpty());
                getTownLocations().add(new CameraMapPoint());
            }
            getTeleportLocations().add(new CameraMapPoint());
        }
        for (int i = 0; i < 0x02; i++) {
            getTownSeries().add(SingleMapEntry.createEmpty());
        }
        // ----------------

        // 读取x和y并储存
        final byte[][] points = new byte[2][getTownMaxCount()];
        getBuffer().getAABytes(getTownLocationsAddress().getStartAddress(), 0, getTownMaxCount(), points);
        for (int i = 0; i < getTownMaxCount(); i++) {
            getTownLocation(i).setCamera(points[0][i], points[1][i]);
        }

        // 读取城镇对应的地图
        final byte[] towns = new byte[getTownMaxCount()];
        getBuffer().get(getTownsAddress(), towns);
        // 读取城镇数据
        // 这个数据就厉害了
        // 可以修改0x0441-0x460的数据，但是只能 或(|)运算，无法移除
        final byte[] townValues = new byte[getTownMaxCount()];
        getBuffer().getPrg(0x3471E - 0x10, townValues);
        for (int i = 0; i < getTownMaxCount(); i++) {
            getTown(i).set(towns[i], townValues[i]);
        }

        // 城镇附属，最多2个，格式和城镇一样，就不重复注释了
        final byte[] townSeries = new byte[0x02];
        final byte[] townSeriesValues = new byte[0x02];
        getBuffer().getPrg(0x3471B - 0x10, townSeries);
        getBuffer().getPrg(0x34732 - 0x10, townSeriesValues);
        for (int i = 0; i < 0x02; i++) {
            getTownSeries().get(i).set(townSeries[i], townSeriesValues[i]);
        }


        // 读取时空隧道机器目的地
        // map，不包含多余的三个未知数据
        final byte[] teleportMaps = new byte[getTeleportMaxCount() - 0x03];
        getBuffer().get(getTeleportLocationAddress(), teleportMaps);
        // x、y，包含多余的三个未知数据
        final byte[][] teleportPoints = new byte[2][getTeleportMaxCount()];
        getBuffer().getAABytes(getTeleportLocationAddress().getEndAddress() + 1, 0, getTeleportMaxCount(), teleportPoints);
        for (int i = 0; i < getTeleportMaxCount(); i++) {
            if (i < teleportMaps.length) {
                getTeleportLocation(i).set(teleportMaps[i], teleportPoints[0][i], teleportPoints[1][i]);
            } else {
                getTeleportLocation(i).set(teleportPoints[0][i], teleportPoints[1][i]);
            }
        }
    }

    @Editor.Apply
    public void onApply() {
        // x、y
        final byte[][] townPoints = new byte[2][getTownMaxCount()];
        for (int i = 0; i < getTownMaxCount(); i++) {
            var townLocation = getTownLocation(i);
            townPoints[0][i] = townLocation.getCameraX();
            townPoints[1][i] = townLocation.getCameraY();
        }
        getBuffer().putAABytes(getTownLocationsAddress().getStartAddress(), 0, getTownMaxCount(), townPoints);

        // map
        final byte[] teleportMaps = new byte[getTeleportMaxCount() - 0x03];
        // x、y
        final byte[][] teleportPoints = new byte[2][getTeleportMaxCount()];
        for (int i = 0; i < getTeleportMaxCount(); i++) {
            var teleportLocation = getTeleportLocation(i);
            if (i < teleportMaps.length) {
                teleportMaps[i] = teleportLocation.getMap();
            }
            teleportPoints[0][i] = teleportLocation.getCameraX();
            teleportPoints[1][i] = teleportLocation.getCameraY();
            getTeleportLocation(i).set(teleportPoints[0][i], teleportPoints[1][i]);
        }
        // 写入时空隧道机器的目的地
        getBuffer().put(getTeleportLocationAddress(), teleportMaps);
        getBuffer().putAABytes(getTeleportLocationAddress().getEndAddress() + 1, 0, getTeleportMaxCount(), teleportPoints);

        // 写入城镇对应的地图
        final byte[] towns = new byte[getTownMaxCount()];
        final byte[] townValues = new byte[getTownMaxCount()];
        for (int i = 0; i < getTownMaxCount(); i++) {
            var town = getTown(i);
            towns[i] = town.getKey();
            townValues[i] = town.getValue();
        }
        getBuffer().put(getTownsAddress(), towns);
        getBuffer().putPrg(0x3471E - 0x10, townValues);

        // 写入城镇附属
        byte[] townSeries = new byte[0x02];
        byte[] townSeriesValues = new byte[0x02];
        townSeries[0x00] = (byte) (getTownSeries().get(0x00).getKey() & 0xFF);
        townSeries[0x01] = (byte) (getTownSeries().get(0x01).getKey() & 0xFF);
        townSeriesValues[0x00] = (byte) (getTownSeries().get(0x00).getValue() & 0xFF);
        townSeriesValues[0x01] = (byte) (getTownSeries().get(0x01).getValue() & 0xFF);

        // 写入附属地图
        getBuffer().putPrg(0x3471B - 0x10, townSeries);
        // 写入附属地图的所属地图（也可以是其它数据
        getBuffer().putPrg(0x34732 - 0x10, townSeriesValues);
    }

    @Override
    public @NotNull List<CameraMapPoint> getTownLocations() {
        return townLocations;
    }

    @Override
    public List<CameraMapPoint> getTeleportLocations() {
        return teleportLocations;
    }

    @Override
    public @NotNull List<SingleMapEntry<Byte, Byte>> getTowns() {
        return towns;
    }

    @Override
    public @NotNull List<SingleMapEntry<Byte, Byte>> getTownSeries() {
        return townSeries;
    }

    @Override
    public @NotNull DataAddress getTownsAddress() {
        return townsAddress;
    }

    @Override
    public @NotNull DataAddress getTownLocationsAddress() {
        return townLocationsAddress;
    }

    @Override
    public @NotNull DataAddress getTeleportLocationAddress() {
        return teleportLocationAddress;
    }
}
