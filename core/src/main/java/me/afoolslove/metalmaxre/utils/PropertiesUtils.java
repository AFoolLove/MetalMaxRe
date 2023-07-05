package me.afoolslove.metalmaxre.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * 常用配置功能
 */
public class PropertiesUtils {
    private PropertiesUtils() {
    }

    public static void exportDefault() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = ResourceManager.getAsStream("/config/config.properties");
        properties.load(inputStream);
        saveToDefault(properties);
    }

    public static Properties loadDefault() {
        return load(System.getProperty("user.dir") + File.separator + "MetalMaxRe" + File.separator + "config.properties");
    }

    public static boolean saveToDefault(@NotNull Properties properties) {
        return save(properties, System.getProperty("user.dir") + File.separator + "MetalMaxRe" + File.separator + "config.properties");
    }

    public static Properties load(@NotNull String path) {
        Properties properties = new Properties();
        Path of = Path.of(path);
        if (Files.exists(of)) {
            try {
                properties.load(Files.newInputStream(of));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static boolean save(@NotNull Properties properties, @NotNull String path) {
        try {
            Path of = Path.of(path);
            if (!Files.exists(of)) {
                // 文件不存在
                Files.createDirectories(of.getParent()); // 创建父级文件夹
                Files.createFile(of); // 创建空文件
            }
            properties.store(Files.newOutputStream(of), null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
