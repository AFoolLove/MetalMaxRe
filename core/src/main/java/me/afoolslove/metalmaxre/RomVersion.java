package me.afoolslove.metalmaxre;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 游戏ROM的版本
 *
 * @author AFoolLove
 */
public class RomVersion {
    public static final Map<String, RomVersion> VERSIONS = new ConcurrentHashMap<>();

    /**
     * 日文版
     * <p>
     */
    public static RomVersion getJapanese() {
        return getVersion("japanese");
    }

    /**
     * 中文版
     */
    public static RomVersion getChinese() {
        return getVersion("chinese");
    }

    /**
     * SuperHack版
     */
    public static RomVersion getSuperHack() {
        return getVersion("super_hack");
    }

    /**
     * SuperHack通用版
     */
    public static RomVersion getSuperHackGeneral() {
        return getVersion("super_hack_general");
    }

    public static RomVersion getVersion(@NotNull String version) {
        return getVersion(version, "chinese");
    }

    public static RomVersion getVersion(@NotNull String version, String def) {
        RomVersion ver = VERSIONS.get(version);
        if (ver == null && def != null) {
            return VERSIONS.get(def);
        }
        return ver;
    }

    @NotNull
    public static Map<String, RomVersion> getVersions() {
        return VERSIONS;
    }

    static {
        byte[] infoBytes = ResourceManager.getAsBytes("/roms/info.json");
        Gson gson = new Gson();
        Map<String, RomVersion> map = gson.fromJson(new String(infoBytes), new TypeToken<Map<String, RomVersion>>() {
        }.getType());
        VERSIONS.putAll(map);
    }


    @SerializedName("name")
    private final String name;
    @SerializedName("path")
    private final String path;
    @SerializedName("size")
    private final int size;
    @SerializedName("description")
    private final String description;
    @SerializedName("modifyRecords")
    private final List<String> modifyRecords;
    @SerializedName("builtIn")
    private final boolean builtIn;

    public RomVersion(String name, String path, int size, String description, List<String> modifyRecords) {
        this(false, name, path, size, description, modifyRecords);
    }

    private RomVersion(boolean builtIn, String name, String path, int size, String description, List<String> modifyRecords) {
        this.builtIn = builtIn;
        this.name = name;
        this.path = path;
        this.size = size;
        this.description = description;
        this.modifyRecords = modifyRecords;
    }

    /**
     * 是否为内置的ROM
     * @return 内置的ROM
     */
    public boolean isBuiltIn() {
        return builtIn;
    }

    /**
     * ROM版本名称
     * @return ROM版本名称
     */
    public String getName() {
        return name;
    }

    /**
     * ROM文件路径
     * @return ROM文件路径
     */
    public String getPath() {
        return path;
    }

    /**
     * ROM文件大小
     * @return ROM文件大小
     */
    public int getSize() {
        return size;
    }

    /**
     * ROM版本描述
     * @return ROM版本描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * ROM修改记录
     * @return ROM修改记录
     */
    public List<String> getModifyRecords() {
        return modifyRecords;
    }

    /**
     * ROM版本ID
     * @return ROM版本ID
     */
    public String getId() {
        return idFromInstance(this);
    }

    /**
     * 判断是否为相同ROM版本
     */
    public boolean isSameVersion(@Nullable RomVersion version) {
        if (version == null) {
            return false;
        }
        if (this == version) {
            return true;
        }
        return this.getId().equals(version.getId());
    }

    /**
     * 通过RomVersion的实例获取ID
     * @param instance RomVersion的实例
     * @return 通过RomVersion的实例获取ID，可能为{@code null}
     */
    public static String idFromInstance(@NotNull RomVersion instance) {
        for (Map.Entry<String, RomVersion> entry : VERSIONS.entrySet()) {
            if (entry.getValue() == instance) {
                return entry.getKey();
            }
        }
        return null;
    }
}
