package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.text.mapping.CharMapCN;
import me.afoolslove.metalmaxre.editors.text.mapping.CharMapJP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 多MetalMaxRe实例管理
 */
public class MultipleMetalMaxRe {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultipleMetalMaxRe.class);
    private final List<MetalMaxRe> metalMaxRes = new ArrayList<>();

    /**
     * 当前实例索引
     */
    private int currentIndex = -1;

    public MultipleMetalMaxRe() {
    }

    /**
     * 读取文件并创建一个实例
     *
     * @param version       rom版本，默认{@link RomVersion#getChinese()}
     * @param path          rom路径
     * @param useDefEditors 使用默认提供的编辑器和管理器
     * @return 实例
     */
    public MetalMaxRe create(@Nullable RomVersion version, @Nullable Path path, boolean useDefEditors) throws IOException {
        if (version == null) {
            version = RomVersion.getChinese();
        }
        RomBuffer romBuffer = new RomBuffer(version, path);
        return create(romBuffer, useDefEditors);
    }

    /**
     * 通过RomBuffer创建一个实例
     *
     * @param romBuffer     RomBuffer
     * @param useDefEditors 使用默认提供的编辑器和管理器
     * @return 实例
     */
    public MetalMaxRe create(@NotNull RomBuffer romBuffer, boolean useDefEditors) {
        MetalMaxRe metalMaxRe = new MetalMaxRe(romBuffer);

        if (romBuffer.getVersion() == RomVersion.getJapanese()) {
            metalMaxRe.setCharMap(new CharMapJP(metalMaxRe.getProperties().getProperty("charMapJP", null)));
        } else {
            metalMaxRe.setCharMap(new CharMapCN(metalMaxRe.getProperties().getProperty("charMapCN", null)));
        }

        if (useDefEditors) {
            EditorManagerImpl editorManager = new EditorManagerImpl(metalMaxRe);
            metalMaxRe.setEditorManager(editorManager);
            editorManager.registerDefaultEditors();
        }
        add(metalMaxRe);
        return metalMaxRe;
    }

    /**
     * 添加一个已有实例
     */
    public void add(@NotNull MetalMaxRe metalMaxRe) {
        if (hasInstance() && metalMaxRes.contains(metalMaxRe)) {
            return;
        }
        if (!hasInstance()) {
            // 将第一个添加的实例作为 当前实例
            currentIndex = 0;
        }
        metalMaxRes.add(metalMaxRe);
    }

    /**
     * 移除一个实例
     *
     * @param metalMaxRe 被移除的实例
     */
    public void remove(@Nullable MetalMaxRe metalMaxRe) {
        metalMaxRes.remove(metalMaxRe);
    }

    /**
     * @return 是否存在一个或多个实例
     */
    public boolean hasInstance() {
        return !metalMaxRes.isEmpty();
    }

    /**
     * @return 获取当前的的实例，如果不存在实例，返回 {@code null}
     */
    public MetalMaxRe current() {
        if (hasInstance()) {
            return metalMaxRes.get(currentIndex);
        }
        return null;
    }

    /**
     * 选择一个实例索引作为 当前实例
     *
     * @param index 索引
     * @return 选择的实例
     */
    public synchronized MetalMaxRe select(int index) {
        if (index >= metalMaxRes.size()) {
            // 超出范围
            return null;
        }
        this.currentIndex = index;
        return metalMaxRes.get(index);
    }

    /**
     * 将一个实例添加并选中
     *
     * @param metalMaxRe 实例
     * @return 选择的实例
     */
    public synchronized MetalMaxRe select(@NotNull MetalMaxRe metalMaxRe) {
        int indexOf = metalMaxRes.indexOf(metalMaxRe);
        if (indexOf == -1) {
            // 不存在，添加并选中
            add(metalMaxRe);
            this.currentIndex = metalMaxRes.indexOf(metalMaxRe);
        } else {
            this.currentIndex = indexOf;
        }
        return metalMaxRe;
    }
}
