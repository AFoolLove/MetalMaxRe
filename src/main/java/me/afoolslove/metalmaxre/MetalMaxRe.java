package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
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
import java.lang.reflect.Method;
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

        WorldMapEditor worldMapEditor = new WorldMapEditor();
        EditorManager.register(worldMapEditor);

        EventTilesEditor eventTilesEditor = new EventTilesEditor();
        EditorManager.register(eventTilesEditor);

        SpriteEditor spriteEditor = new SpriteEditor();
        EditorManager.register(spriteEditor);

        MapEntranceEditor mapEntranceEditor = new MapEntranceEditor();
        EditorManager.register(mapEntranceEditor);

        PlayerEditor playerEditor = new PlayerEditor();
        EditorManager.register(playerEditor);

        TankEditor tankEditor = new TankEditor();
        EditorManager.register(tankEditor);

        PlayerExperienceEditor playerExperienceEditor = new PlayerExperienceEditor();
        EditorManager.register(playerExperienceEditor);

        PaletteEditor paletteEditor = new PaletteEditor();
        EditorManager.register(paletteEditor);

        ItemsEditor itemsEditor = new ItemsEditor();
        EditorManager.register(itemsEditor);

        TextEditor textEditor = new TextEditor();
        EditorManager.register(textEditor);

        TileSetEditor tileSetEditor = new TileSetEditor();
        EditorManager.register(tileSetEditor);

        MonsterEditor monsterEditor = new MonsterEditor();
        EditorManager.register(monsterEditor);
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
    public void loadGame(boolean isInitTarget, @NotNull File game, @NotNull EditorWorker editorWorker) {
        setTarget(game);
        this.isInitTarget = isInitTarget;
        editorWorker.execute();
    }

    public boolean saveAs(@NotNull String path) {
        try {
            System.out.printf("保存修改到：%s\n", path);
            // 写入头属性
            buffer.position(0x00);
            buffer.put(header.getHeader());

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
            var tankInitialAttributes = EditorManager.getEditor(TankEditor.class);
            var playerExperienceEditor = EditorManager.getEditor(PlayerExperienceEditor.class);
            var paletteEditor = EditorManager.getEditor(PaletteEditor.class);
            var itemsEditor = EditorManager.getEditor(ItemsEditor.class);
            var textEditor = EditorManager.getEditor(TextEditor.class);
            var tileSetEditor = EditorManager.getEditor(TileSetEditor.class);
            var worldMapEditor = EditorManager.getEditor(WorldMapEditor.class);
            var monsterEditor = EditorManager.getEditor(MonsterEditor.class);

            List<AbstractEditor<?>> unorderedEditors = new ArrayList<>();
            List<AbstractEditor<?>> orderedEditors = new LinkedList<>();

            // 无序
            unorderedEditors.add(treasureEditor);
            unorderedEditors.add(computerEditor);
            unorderedEditors.add(dogSystemEditor);
            unorderedEditors.add(vendorEditor);
            unorderedEditors.add(spriteEditor);
            unorderedEditors.add(playerEditor);
            unorderedEditors.add(tankInitialAttributes);
            unorderedEditors.add(playerExperienceEditor);
            unorderedEditors.add(itemsEditor);
            unorderedEditors.add(textEditor);
            unorderedEditors.add(tileSetEditor);
            unorderedEditors.add(monsterEditor);

            // 顺序写入
            orderedEditors.add(mapEditor); // 影响 mapPropertiesEditor
            orderedEditors.add(worldMapEditor); // 影响 eventTilesEditor
            orderedEditors.add(eventTilesEditor); // 影响 mapPropertiesEditor
            orderedEditors.add(mapEntranceEditor); // 影响 mapPropertiesEditor
            orderedEditors.add(paletteEditor); // 影响 mapPropertiesEditor // 暂时没有
            orderedEditors.add(mapPropertiesEditor);

            try {
                Method onWriteBeforeMethod = AbstractEditor.Listener.class.getMethod("onWriteBefore", AbstractEditor.class);
                Method onWriteAfterMethod = AbstractEditor.Listener.class.getMethod("onWriteAfter", AbstractEditor.class);
                // 无序
                for (AbstractEditor<?> unorderedEditor : unorderedEditors) {
                    for (AbstractEditor.Listener<?> listener : unorderedEditor.getListeners()) {
                        onWriteBeforeMethod.invoke(listener, unorderedEditor);
                    }
                    unorderedEditor.onWrite(buffer);
                    for (AbstractEditor.Listener<?> listener : unorderedEditor.getListeners()) {
                        onWriteAfterMethod.invoke(listener, unorderedEditor);
                    }
                }

                // 有序
                for (AbstractEditor<?> orderedEditor : orderedEditors) {
                    for (AbstractEditor.Listener<?> listener : orderedEditor.getListeners()) {
                        onWriteBeforeMethod.invoke(listener, orderedEditor);
                    }
                    orderedEditor.onWrite(buffer);
                    for (AbstractEditor.Listener<?> listener : orderedEditor.getListeners()) {
                        onWriteAfterMethod.invoke(listener, orderedEditor);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
