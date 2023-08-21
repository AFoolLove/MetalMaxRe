package me.afoolslove.metalmaxre;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;

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
        var ver = VERSIONS.get(version);
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
        var infoBytes = ResourceManager.getAsBytes("/roms/info.json");
        var gson = new Gson();
        Map<String, RomVersion> map = gson.fromJson(new String(infoBytes), new TypeToken<Map<String, RomVersion>>() {
        }.getType());
        VERSIONS.putAll(map);
    }


    private final String name;
    private final String path;
    private final int size;
    private final String description;
    private final List<String> modifyRecords;

    public RomVersion(String name, String path, int size, String description, List<String> modifyRecords) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.description = description;
        this.modifyRecords = modifyRecords;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getModifyRecords() {
        return modifyRecords;
    }

    public String getId() {
        return idFromInstance(this);
    }

    public static String idFromInstance(@NotNull RomVersion instance) {
        for (Map.Entry<String, RomVersion> entry : VERSIONS.entrySet()) {
            if (entry.getValue() == instance) {
                return entry.getKey();
            }
        }
        return null;
    }
}
