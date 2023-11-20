package me.afoolslove.metalmaxre.editors.map;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
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
public class DogSystemEditorImpl extends RomBufferWrapperAbstractEditor implements IDogSystemEditor {
    @NotNull
    protected DataAddress townsAddress;
    @NotNull
    protected DataAddress townLocationsAddress;
    @NotNull
    protected DataAddress teleportLocationAddress;

    private final List<CameraMapPoint> townLocations = new ArrayList<>(getTownMaxCount());
    private final List<CameraMapPoint> teleportLocations = new ArrayList<>(getTownMaxCount());
    private final List<SingleMapEntry<Byte, Byte>> towns = new ArrayList<>();

    public DogSystemEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.from(0x34706 - 0x10, 0x34733 - 0x10),
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

        for (int i = 0; i < getTeleportMaxCount(); i++) {
            if (i < getTownMaxCount()) {
                getTownLocations().add(new CameraMapPoint());
            }
            getTeleportLocations().add(new CameraMapPoint());
        }
        // ----------------

        // 读取城镇和进城镇后开启的事件（包含开启犬系统城镇）
        byte[][] towns = new byte[2][0x17];
        getBuffer().getAABytes(getTownsAddress(), 0, 0x17, towns);
        for (int i = 0; i < 0x17; i++) {
            getTowns().add(SingleMapEntry.create(towns[0][i], towns[1][i]));
        }


        // 读取x和y并储存
        final byte[][] points = new byte[2][getTownMaxCount()];
        getBuffer().getAABytes(getTownLocationsAddress(), 0, getTownMaxCount(), points);
        for (int i = 0; i < getTownMaxCount(); i++) {
            getTownLocation(i).setCamera(points[0][i], points[1][i]);
        }

        // 读取时空隧道机器目的地
        // map，不包含多余的三个未知数据
        final byte[] teleportMaps = new byte[getTeleportMaxCount() - 0x03];
        // x、y，包含多余的三个未知数据
        final byte[][] teleportPoints = new byte[2][getTeleportMaxCount()];
        position(getTeleportLocationAddress());
        getBuffer().get(teleportMaps);
        getBuffer().getAABytes(0, getTeleportMaxCount(), teleportPoints);
        for (int i = 0; i < getTeleportMaxCount(); i++) {
            if (i < teleportMaps.length) {
                getTeleportLocation(i).setCamera(teleportMaps[i], teleportPoints[0][i], teleportPoints[1][i]);
            } else {
                getTeleportLocation(i).setCamera(teleportPoints[0][i], teleportPoints[1][i]);
            }
        }
    }

    @Editor.Apply
    public void onApply() {
        // 写入城镇和进城镇后开启的事件（包含开启犬系统城镇）
        byte[][] towns = new byte[2][0x17];
        for (int i = 0, count = Math.min(0x17, getTowns().size()); i < count; i++) {
            SingleMapEntry<Byte, Byte> entry = getTowns().get(i);
            towns[0][i] = entry.getKey();
            towns[1][i] = entry.getValue();
        }
        getBuffer().putAABytes(getTownsAddress(), 0, 0x17, towns);

        // x、y
        final byte[][] townPoints = new byte[2][getTownMaxCount()];
        for (int i = 0; i < getTownMaxCount(); i++) {
            CameraMapPoint townLocation = getTownLocation(i);
            townPoints[0][i] = townLocation.getCameraX();
            townPoints[1][i] = townLocation.getCameraY();
        }
        getBuffer().putAABytes(getTownLocationsAddress(), 0, getTownMaxCount(), townPoints);

        // map
        final byte[] teleportMaps = new byte[getTeleportMaxCount() - 0x03];
        // x、y
        final byte[][] teleportPoints = new byte[2][getTeleportMaxCount()];
        for (int i = 0; i < getTeleportMaxCount(); i++) {
            CameraMapPoint teleportLocation = getTeleportLocation(i);
            if (i < teleportMaps.length) {
                teleportMaps[i] = teleportLocation.getMap();
            }
            teleportPoints[0][i] = teleportLocation.getCameraX();
            teleportPoints[1][i] = teleportLocation.getCameraY();
            getTeleportLocation(i).setCamera(teleportPoints[0][i], teleportPoints[1][i]);
        }
        // 写入时空隧道机器的目的地
        position(getTeleportLocationAddress());
        getBuffer().put(teleportMaps);
        getBuffer().putAABytes(0, getTeleportMaxCount(), teleportPoints);
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
