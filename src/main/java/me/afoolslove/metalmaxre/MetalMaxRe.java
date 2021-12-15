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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private URI target;
    /**
     * 是否为初始文件
     */
    private boolean isInitTarget = true;

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

        EditorManager.register(TreasureEditor.class);
        EditorManager.register(ComputerEditor.class);
        EditorManager.register(DogSystemEditor.class);
        EditorManager.register(VendorEditor.class);
        EditorManager.register(SpriteEditor.class);
        EditorManager.register(PlayerEditor.class);
        EditorManager.register(TankEditor.class);
        EditorManager.register(PaletteEditor.class);
        EditorManager.register(PlayerExperienceEditor.class);
        EditorManager.register(ItemsEditor.class);
        EditorManager.register(TextEditor.class);
        EditorManager.register(TileSetEditor.class);
        EditorManager.register(MonsterEditor.class);
        EditorManager.register(PatchEditor.class);

        EditorManager.register(MapEditor.class);
        EditorManager.register(MapEntranceEditor.class);
        EditorManager.register(MapPropertiesEditor.class);

        EditorManager.register(EventTilesEditor.class);
        EditorManager.register(WorldMapEditor.class);
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
    public URI getTarget() {
        return target;
    }

    /**
     * 设置头属性
     */
    public void setHeader(GameHeader header) {
        this.header = header;
    }

    public void setTarget(URI target) {
        this.target = target;
    }

    /**
     * 设置是否使用默认ROM
     */
    public void setInitTarget(boolean initTarget) {
        isInitTarget = initTarget;
    }

    /**
     * @return 是否使用默认ROM
     */
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
     *
     * @param game ROM
     */
    public void loadGame(@NotNull URI game) {
        loadGame(game, null);
    }

    /**
     * 加载游戏文件
     *
     * @param game         ROM
     * @param loadListener 加载编辑器
     */
    public void loadGame(@NotNull URI game, @Nullable EditorManager.LoadListener loadListener) {
        this.isInitTarget = false;
        setTarget(game);

        MetalMaxRe instance = MetalMaxRe.getInstance();
        byte[] bytes;
        // 外部路径
        if (instance.getTarget() == null) {
            System.out.println("未指定ROM文件");
            return;
        }
        Path path = Paths.get(instance.getTarget());
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取目标ROM失败");
            return;
        }

        loadInit0(bytes, loadListener);
    }

    /**
     * 加载默认游戏文件
     */
    public void loadInitGame() {
        loadInitGame(null);
    }

    /**
     * 加载默认游戏文件
     *
     * @param loadListener 加载监听器
     */
    public void loadInitGame(@Nullable EditorManager.LoadListener loadListener) {
        this.isInitTarget = true;
        setTarget(null);

        byte[] bytes;

        // 直接获取流
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             InputStream resourceAsStream = ResourceManager.getAsStream("/MetalMax.nes")) {
            if (resourceAsStream == null) {
                // 读取失败可还行
                System.out.println("初始ROM不存在");
                return;
            }
            resourceAsStream.transferTo(byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取初始ROM失败");
            return;
        }

        loadInit0(bytes, loadListener);
    }

    private void loadInit0(byte[] bytes, @Nullable EditorManager.LoadListener loadListener) {
        MetalMaxRe instance = MetalMaxRe.getInstance();

        if (instance.getBuffer() != null) {
            instance.getBuffer().clear(); // 怎么释放呢？
        }

        // 读取头属性
        instance.setHeader(new GameHeader(bytes));
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        instance.setBuffer(buffer);
        // 写入到 ByteBuffer
        buffer.put(bytes);

        EditorManager.loadEditors(true, loadListener);
    }

    /**
     * 重新加载ROM文件
     *
     * @return 是否成功重新加载
     */
    public boolean reloadGame() {
        if (isIsInitTarget()) {
            loadInitGame();
        } else {
            if (getTarget() == null) {
                // 不存在的目标文件，但也不是初始文件
                return false;
            }
            Path path = Paths.get(getTarget());
            if (Files.notExists(path)) {
                // 目标文件不存在
                return false;
            }
            loadGame(getTarget());
        }
        return true;
    }

    /**
     * 重新加载ROM文件
     *
     * @return 是否成功重新加载
     */
    public boolean reloadGame(@Nullable EditorManager.LoadListener loadListener) {
        if (isIsInitTarget()) {
            loadInitGame(loadListener);
        } else {
            if (getTarget() == null) {
                // 不存在的目标文件，但也不是初始文件
                return false;
            }
            Path path = Paths.get(getTarget());
            if (Files.notExists(path)) {
                // 目标文件不存在
                return false;
            }
            loadGame(getTarget(), loadListener);
        }
        return true;
    }

    public boolean saveAs(@NotNull String path) {
        try {
            System.out.printf("保存修改到：%s\n", path);
            // 写入头属性
            buffer.put(0x00000, header.getHeader());
            EditorManager.applyEditors();
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
