package me.afoolslove.metalmaxre;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ReLauncher {

    public static void main(String[] args) {
        String config = null;

        for (String[] strings : Arrays.stream(args).map(s -> s.split("=", 2)).collect(Collectors.toList())) {
            String key = strings[0].toUpperCase(Locale.ROOT);
            String value = strings.length == 1 ? null : strings[1];
            switch (key) {
                case "CONFIG":
                    // 无效的文件不会作为配置文件加载
                    if (value != null && Files.exists(Paths.get(value))) {
                        config = value;
                    } else {
                        System.out.println("无效的配置文件：" + value);
                    }
                    break;
                default:
                    break;
            }
        }
        new MetalMaxRe(config);
    }
}
