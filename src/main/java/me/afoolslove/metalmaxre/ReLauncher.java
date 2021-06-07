package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.gui.MainWindow;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ReLauncher {

    public static void main(String[] args) {
        String config = null;

        boolean showGui = true;
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
                case "GUI":
                    showGui = Boolean.parseBoolean(value);
                    break;
                default:
                    break;
            }
        }
        MetalMaxRe metalMaxRe = new MetalMaxRe(config);
        if (showGui) {
            new MainWindow();
        } else {
            metalMaxRe.loadGame("C:/Users/AFoolLove/IdeaProjects/MetalMaxRe/src/main/resources/MetalMax.nes");
        }
        metalMaxRe.saveAs("C:/Users/AFoolLove/IdeaProjects/MetalMaxRe/src/main/resources/MetalMax-Test.nes");
    }
}
