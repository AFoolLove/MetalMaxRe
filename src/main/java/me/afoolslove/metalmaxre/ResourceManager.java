package me.afoolslove.metalmaxre;

import java.io.InputStream;
import java.net.URL;

public class ResourceManager {
    public static URL get(String name) {
        return ResourceManager.class.getResource(name);
    }

    public static InputStream getAsStream(String name) {
        return ResourceManager.class.getResourceAsStream(name);
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
