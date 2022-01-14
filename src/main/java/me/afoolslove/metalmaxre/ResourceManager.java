package me.afoolslove.metalmaxre;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceManager {
    public static URL get(String name) {
        return ResourceManager.class.getResource(name);
    }

    public static InputStream getAsStream(String name) {
        return ResourceManager.class.getResourceAsStream(name);
    }

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
