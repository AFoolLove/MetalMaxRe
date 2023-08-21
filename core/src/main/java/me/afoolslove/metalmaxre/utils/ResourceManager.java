package me.afoolslove.metalmaxre.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 资源管理器
 *
 * @author AFoolLove
 */
public class ResourceManager {
    /**
     * 导出资源到指定路径
     *
     * @param name 资源路径
     * @param path 目标资源路径
     * @return 导出是否成功
     */
    public static boolean export(@NotNull String name, @NotNull Path path) throws IOException {
        if (!Files.exists(path)) {
            // 文件不存在
            Files.createDirectories(path.getParent()); // 创建父级文件夹
            Files.createFile(path); // 创建空文件
        }

        // 写入文件
        try (OutputStream outputStream = Files.newOutputStream(path);
             InputStream inputStream = getAsStream(name)) {
            if (inputStream != null) {
                inputStream.transferTo(outputStream);
                return true;
            }
        }
        return false;
    }

    /**
     * 获取资源文件为URL
     *
     * @param name 资源路径
     * @return 资源文件的URL
     */
    @Nullable
    public static URL get(@NotNull String name) {
        return ResourceManager.class.getResource(name);
    }

    /**
     * 获取资源文件为输入流
     *
     * @param name 资源路径
     * @return 资源文件的输入流
     */
    @Nullable
    public static InputStream getAsStream(@NotNull String name) {
        return ResourceManager.class.getResourceAsStream(name);
    }

    /**
     * 获取资源文件并转换为字节数组
     *
     * @param name 资源路径
     * @return 资源文件的字节数组
     */
    @Nullable
    public static byte[] getAsBytes(@NotNull String name) {
        try (var asStream = getAsStream(name)) {
            if (asStream != null) {
                var byteArrayOutputStream = new ByteArrayOutputStream(asStream.available());
                asStream.transferTo(byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取程序运行时所在的路径
     *
     * @return 程序运行时所在的路径
     */
    public static Path getRunningPath() {
        URL location = ResourceManager.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            Path path = Paths.get(location.toURI());
            return isJar() ? path.getParent() : path;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 当前运行的方式是否为jar
     * @see #isNotJar()
     */
    public static boolean isJar() {
        URL url = get("");
        return url != null && "jar".equalsIgnoreCase(url.getProtocol());
    }

    public static boolean isNotJar() {
        return !isJar();
    }
}
