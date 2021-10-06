package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.PatchEditor;
import me.afoolslove.metalmaxre.editor.computer.ComputerEditor;
import me.afoolslove.metalmaxre.editor.computer.vendor.VendorEditor;
import me.afoolslove.metalmaxre.editor.items.ItemsEditor;
import me.afoolslove.metalmaxre.editor.map.DogSystemEditor;
import me.afoolslove.metalmaxre.editor.map.MapEditor;
import me.afoolslove.metalmaxre.editor.map.MapEntranceEditor;
import me.afoolslove.metalmaxre.editor.map.MapPropertiesEditor;
import me.afoolslove.metalmaxre.editor.map.events.EventTilesEditor;
import me.afoolslove.metalmaxre.editor.map.tileset.TileSetEditor;
import me.afoolslove.metalmaxre.editor.map.world.WorldMapEditor;
import me.afoolslove.metalmaxre.editor.monster.MonsterEditor;
import me.afoolslove.metalmaxre.editor.palette.PaletteEditor;
import me.afoolslove.metalmaxre.editor.player.PlayerEditor;
import me.afoolslove.metalmaxre.editor.player.PlayerExperienceEditor;
import me.afoolslove.metalmaxre.editor.sprite.SpriteEditor;
import me.afoolslove.metalmaxre.editor.tank.TankEditor;
import me.afoolslove.metalmaxre.editor.text.TextEditor;
import me.afoolslove.metalmaxre.editor.treasure.TreasureEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Properties;

// 你好陌生人，我顶你个肺

/**
 * 程序主体
 *
 * @author AFoolLove
 */
public class MetalMaxRe {
    private static MetalMaxRe INSTANCE;

    private GameHeader header;

    /**
     * 当前加载的游戏文件
     */
    private File target;
    /**
     * 是否为初始文件
     */
    private boolean isInitTarget;

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
        ComputerEditor computerEditor = new ComputerEditor();
        MapPropertiesEditor mapPropertiesEditor = new MapPropertiesEditor();
        MapEditor mapEditor = new MapEditor();
        DogSystemEditor dogSystemEditor = new DogSystemEditor();
        VendorEditor vendorEditor = new VendorEditor();
        WorldMapEditor worldMapEditor = new WorldMapEditor();
        EventTilesEditor eventTilesEditor = new EventTilesEditor();
        SpriteEditor spriteEditor = new SpriteEditor();
        MapEntranceEditor mapEntranceEditor = new MapEntranceEditor();
        PlayerEditor playerEditor = new PlayerEditor();
        TankEditor tankEditor = new TankEditor();
        PlayerExperienceEditor playerExperienceEditor = new PlayerExperienceEditor();
        PaletteEditor paletteEditor = new PaletteEditor();
        ItemsEditor itemsEditor = new ItemsEditor();
        TextEditor textEditor = new TextEditor();
        TileSetEditor tileSetEditor = new TileSetEditor();
        MonsterEditor monsterEditor = new MonsterEditor();
        PatchEditor patchEditor = new PatchEditor();


        EditorManager.register(treasureEditor);
        EditorManager.register(computerEditor);
        EditorManager.register(mapPropertiesEditor);
        EditorManager.register(mapEditor);
        EditorManager.register(dogSystemEditor);
        EditorManager.register(vendorEditor);
        EditorManager.register(worldMapEditor);
        EditorManager.register(eventTilesEditor);
        EditorManager.register(spriteEditor);
        EditorManager.register(mapEntranceEditor);
        EditorManager.register(playerEditor);
        EditorManager.register(tankEditor);
        EditorManager.register(playerExperienceEditor);
        EditorManager.register(paletteEditor);
        EditorManager.register(itemsEditor);
        EditorManager.register(textEditor);
        EditorManager.register(tileSetEditor);
        EditorManager.register(monsterEditor);
        EditorManager.register(patchEditor);
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * @return 主程序实例
     */
    @NotNull
    public static synchronized MetalMaxRe getInstance() {
        if (INSTANCE == null) {
            // 在？为什么会是 null
            INSTANCE = new MetalMaxRe(null);
        }
        return INSTANCE;
    }

    /**
     * @return 头属性
     */
    public GameHeader getHeader() {
        return header;
    }

    /**
     * @return 当前打开的游戏文件，可能为null
     */
    public File getTarget() {
        return target;
    }

    /**
     * 设置头属性
     */
    public void setHeader(GameHeader header) {
        this.header = header;
    }

    public void setTarget(File target) {
        this.target = target;
    }

    public boolean isIsInitTarget() {
        return isInitTarget;
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
        loadConfig(config);
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
            System.out.println("没有可用的配置文件加载！");
            return;
        }

        getProperties().clear();
        try {
            getProperties().load(Files.newBufferedReader(Paths.get(config)));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("加载配置文件失败：" + config);
        }
    }

    /**
     * 加载游戏文件
     */
    public void loadGame(@NotNull File game, @NotNull EditorWorker editorWorker) {
        setTarget(game);
        this.isInitTarget = false;
        editorWorker.execute();
    }

    public void loadInitGame(@NotNull EditorWorker editorWorker) {
        setTarget(null);
        this.isInitTarget = true;
        editorWorker.execute();
    }

    public boolean saveAs(@NotNull String path) {
        try {
            System.out.printf("保存修改到：%s\n", path);
            // 写入头属性
            buffer.put(0x00000, header.getHeader());
            new WriteEditorWorker() {
                @Override
                protected void process(List<Map.Entry<EditorProcess, Object>> chunks) {
                    for (Map.Entry<EditorProcess, Object> chunk : chunks) {
                        if (chunk.getKey() == EditorProcess.MESSAGE) {
                            System.out.println(chunk.getValue());
                        }
                    }
                }

                @Override
                protected void done() {
                    try {
                        Files.write(Paths.get(path), buffer.array(), StandardOpenOption.CREATE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
            return true;
        } catch (Exception e) {
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
     * 请不要长时间持有该对象！！！
     *
     * @return 数据
     */
    public ByteBuffer getBuffer() {
        return buffer;
    }
}
