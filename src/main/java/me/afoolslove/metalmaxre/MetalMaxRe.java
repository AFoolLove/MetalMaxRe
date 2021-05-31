package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editor.*;
import me.afoolslove.metalmaxre.editor.computer.ComputerEditor;
import me.afoolslove.metalmaxre.editor.computer.vendor.VendorEditor;
import me.afoolslove.metalmaxre.editor.computer.vendor.VendorGood;
import me.afoolslove.metalmaxre.editor.computer.vendor.VendorGoods;
import me.afoolslove.metalmaxre.editor.map.*;
import me.afoolslove.metalmaxre.editor.map.events.EventTile;
import me.afoolslove.metalmaxre.editor.map.events.EventTilesEditor;
import me.afoolslove.metalmaxre.editor.player.Player;
import me.afoolslove.metalmaxre.editor.player.PlayerEditor;
import me.afoolslove.metalmaxre.editor.sprite.Sprite;
import me.afoolslove.metalmaxre.editor.sprite.SpriteEditor;
import me.afoolslove.metalmaxre.editor.treasure.TreasureEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * 程序主体
 *
 * @author AFoolLove
 */
public class MetalMaxRe {
    private static MetalMaxRe INSTANCE;

    private final String defaultConfig;
    private String config;
    private final Properties properties = new Properties();

    private ByteBuffer buffer;

    protected MetalMaxRe(@Nullable String config) {
        this.config = config;
        this.defaultConfig = this.config;

        INSTANCE = this;

        if (config != null) {
            loadConfig(config);
        }

        TreasureEditor treasureEditor = new TreasureEditor();
        EditorManager.register(treasureEditor);

        ComputerEditor computerEditor = new ComputerEditor();
        EditorManager.register(computerEditor);

        MapPropertiesEditor mapPropertiesEditor = new MapPropertiesEditor();
        EditorManager.register(mapPropertiesEditor);

        MapEditor mapEditor = new MapEditor();
        EditorManager.register(mapEditor);

        DogSystemEditor dogSystemEditor = new DogSystemEditor();
        EditorManager.register(dogSystemEditor);

        VendorEditor vendorEditor = new VendorEditor();
        EditorManager.register(vendorEditor);

        EventTilesEditor eventTilesEditor = new EventTilesEditor();
        EditorManager.register(eventTilesEditor);

        SpriteEditor spriteEditor = new SpriteEditor();
        EditorManager.register(spriteEditor);

        MapEntranceEditor mapEntranceEditor = new MapEntranceEditor();
        EditorManager.register(mapEntranceEditor);

        PlayerEditor playerEditor = new PlayerEditor();
        EditorManager.register(playerEditor);

        loadGame("C:/Users/AFoolLove/IdeaProjects/MetalMaxRe/src/main/resources/MetalMax.nes");

//        playerEditor.getInitialAttributes().get(Player.HANTA).attack = 5;

        saveAs("C:/Users/AFoolLove/IdeaProjects/MetalMaxRe/src/main/resources/MetalMax-Test.nes");
        System.out.println();
    }

    public static synchronized MetalMaxRe getInstance() {
        if (INSTANCE == null) {
            // 在？为什么会是 null
            INSTANCE = new MetalMaxRe(null);
        }
        return INSTANCE;
    }

    /**
     * @return 当前应用的配置
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * 设置配置文件路径
     *
     * @param config 配置文件路径
     */
    public void setConfig(@NotNull String config) {
        this.config = config;
        //TODO 更新配置
    }

    public void loadConfig(@Nullable String config) {
        if (config == null) {
            config = this.config;
        }
        if (config == null) {
            config = this.defaultConfig;
        }
        getProperties().clear();
        if (config == null) {
            System.err.println("没有可用的配置文件加载！");
            return;
        }

        getProperties().clear();
        try {
            getProperties().load(Files.newBufferedReader(Paths.get(config)));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("加载配置文件失败：" + config);
        }
    }

    /**
     * 加载游戏文件
     */
    public boolean loadGame(@NotNull String path) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            if (buffer != null) {
                buffer.clear(); // 怎么释放呢？
            }

            buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);

            // 编辑器重新读取数据

            long start = 0, count = 0;
            int successful = 0, failed = 0;
            if (!EditorManager.getEditors().isEmpty()) {
                System.out.println("开始加载编辑器");
                for (AbstractEditor editor : EditorManager.getEditors().values()) {
                    start = System.currentTimeMillis();
                    System.out.print("加载编辑器：" + editor.getClass().getSimpleName());
                    if (editor.onRead(buffer)) {
                        successful++;
                        long time = System.currentTimeMillis() - start;
                        count += time;
                        System.out.printf(" 加载成功！耗时：%dms\n", time);
                    } else {
                        failed++;
                        long time = System.currentTimeMillis() - start;
                        count += time;
                        System.out.printf(" 加载失败！耗时：%dms\n", time);
                    }
                }
                System.out.format("加载编辑器结束，共%d个编辑器，成功%d个，失败%d个\n", successful + failed, successful, failed);
                System.out.format("加载编辑器共计耗时：%dms\n", count);
            } else {
                System.out.println("没有可用的编辑器！");
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveAs(@NotNull String path) {
        try {
            System.out.printf("保存修改到：%s\n", path);
            // 保存所有更改
//            for (AbstractEditor editor : EditorManager.getEditors().values()) {
//                editor.onWrite(buffer);
//            }
            // 暂时还没想好写什么样的读取跟写入的优先度
            // 手动添加
            var treasureEditor = EditorManager.getEditor(TreasureEditor.class);
            var computerEditor = EditorManager.getEditor(ComputerEditor.class);
            var mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);
            var mapEditor = EditorManager.getEditor(MapEditor.class);
            var dogSystemEditor = EditorManager.getEditor(DogSystemEditor.class);
            var vendorEditor = EditorManager.getEditor(VendorEditor.class);
            var eventTilesEditor = EditorManager.getEditor(EventTilesEditor.class);
            var spriteEditor = EditorManager.getEditor(SpriteEditor.class);
            var mapEntranceEditor = EditorManager.getEditor(MapEntranceEditor.class);
            var playerEditor = EditorManager.getEditor(PlayerEditor.class);

            // 无序
            treasureEditor.onWrite(buffer);
            computerEditor.onWrite(buffer);
            dogSystemEditor.onWrite(buffer);
            vendorEditor.onWrite(buffer);
            spriteEditor.onWrite(buffer);
            playerEditor.onWrite(buffer);


            // 顺序写入
            mapEditor.onWrite(buffer); // 影响 mapPropertiesEditor
            eventTilesEditor.onWrite(buffer); // 影响 mapPropertiesEditor
            mapEntranceEditor.onWrite(buffer); // 影响 mapPropertiesEditor
            mapPropertiesEditor.onWrite(buffer);

            Files.write(Paths.get(path), buffer.array(), StandardOpenOption.CREATE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return 当前配置文件的路径
     */
    public String getConfig() {
        return config;
    }

    /**
     * @return 默认配置文件路径，未配置默认配置文件时为 {@code null}
     */
    public String getDefaultConfig() {
        return defaultConfig;
    }


    /**
     * @return 数据
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }


    /**
     * 获取 RPG ROM 的起始位置
     *
     * @return RPG ROM 的起始位置，占时固定为 0x10
     */
    public int getPROMOffset() {
        return 0x10;
    }


    /**
     * 获取 PRG ROM 的长度
     *
     * @return PRG ROM 的长度
     */
    public int getPROMLength() {
        int position = getBuffer().position();
        getBuffer().position(0x04);
        byte b = getBuffer().get();
        getBuffer().position(position);
        return (b & 0xFF) * 0x4000;
    }

    /**
     * 得到 CHR ROM的起始位置
     *
     * @return CHR ROM的起始位置
     */
    public int getVROMOffset() {
        return getPROMOffset() + getPROMLength();
    }

    /**
     * 得到 CHR ROM的长度
     *
     * @return CHR ROM的长度
     */
    public int getVROMLength() {
        int position = getBuffer().position();
        getBuffer().position(0x05);
        byte b = getBuffer().get();
        getBuffer().position(position);
        return (b & 0xFF) * 0x2000;
    }

}
