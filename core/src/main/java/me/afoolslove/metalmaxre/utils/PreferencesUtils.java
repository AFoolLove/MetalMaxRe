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
}
