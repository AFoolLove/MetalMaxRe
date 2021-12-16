package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.gui.MainWindow;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

public class ReLauncher {

    public static void main(String[] args) {
        String config = null;

        boolean showGui = false;
        for (String[] strings : Arrays.stream(args).map(s -> s.split("=", 2)).toList()) {
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
                    showGui = value == null || Boolean.parseBoolean(value);
                    break;
                default:
                    break;
            }
        }
        MetalMaxRe metalMaxRe = new MetalMaxRe(config);
        if (showGui) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            new MainWindow();
        } else {
            metalMaxRe.loadInitGame();
        }
    }
}
