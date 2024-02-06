package me.afoolslove.metalmaxre.utils;

import java.util.prefs.Preferences;

public class PreferencesUtils {
    private PreferencesUtils() {
    }

    /**
     * 获取应用的配置
     *
     * @return 应用的配置
     */
    public static Preferences getPreferences() {
        return Preferences.userRoot().node("me/afoolslove/metalmaxre");
    }

    /**
     * 获取当前类的配置
     *
     * @return 当前类的配置
     */
    public static Preferences getPreferences(Class<?> c) {
        return Preferences.userNodeForPackage(c);
    }

    /**
     * 获取调色板数据
     *
     * @return 调色板数据
     */
    public static Preferences getPalettePreferences() {
        return getPreferences().node("palettes");
    }

    /**
     * 获取字库配置
     *
     * @return 字库配置
     */
    public static Preferences getCharMapPreferences() {
        return getPreferences().node("char_map");
    }

    /**
     * 获取编辑器管理器配置
     *
     * @return 编辑器管理器配置
     */
    public static Preferences getEditorManagerPreferences() {
        return getPreferences().node("editor_manager");
    }

    /**
     * 获取精灵脚本操作配置
     *
     * @return 精灵脚本操作配置
     */
    public static Preferences getSpriteScriptPreferences() {
        return getPreferences().node("sprite_script");
    }
}
