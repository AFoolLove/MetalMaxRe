package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.IEditorManager;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.palette.SystemPalette;
import me.afoolslove.metalmaxre.editors.text.mapping.CharMapCN;
import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import me.afoolslove.metalmaxre.event.EventHandler;
import me.afoolslove.metalmaxre.event.editors.editor.EditorLoadEvent;
import me.afoolslove.metalmaxre.event.editors.editor.EditorManagerEvent;
import me.afoolslove.metalmaxre.patch.IPatchManager;
import me.afoolslove.metalmaxre.patch.PatchManagerImpl;
import me.afoolslove.metalmaxre.utils.PreferencesUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * @author AFoolLove
 */
public class MetalMaxRe {
    private final static Logger LOGGER = LoggerFactory.getLogger(MetalMaxRe.class);
    private RomBuffer romBuffer;
    private IEditorManager editorManager;
    private ICharMap charMap;
    private EventHandler eventHandler = new EventHandler();
    private SystemPalette systemPalette = SystemPalette.DEFAULT_SYSTEM_PALETTE;
    private IPatchManager patchManager;

    private MetalMaxRe() {
    }

    public MetalMaxRe(@NotNull RomBuffer romBuffer) {
        this.romBuffer = romBuffer;
    }

    /**
     * 使用默认提供的实现类
     */
    public void useDefault() {
        if (getEditorManager() == null) {
            // 配置默认编辑器管理器
            setEditorManager(new EditorManagerImpl(this));
        }

        if (getCharMap() == null) {
            // 字库配置
            Preferences preferences = PreferencesUtils.getPreferences().node("char_map");
            // - char_map
            // |- jp
            // |- cn
            setCharMap(new CharMapCN(preferences.get("cn", null)));
        }

        if (getPatchManager() == null) {
            // 配置默认补丁管理器
            setPatchManager(new PatchManagerImpl());
        }
    }

    public RomBuffer getBuffer() {
        return romBuffer;
    }

    public IEditorManager getEditorManager() {
        return editorManager;
    }

    public void setEditorManager(IEditorManager editorManager) {
        this.editorManager = editorManager;
    }

    public ICharMap getCharMap() {
        return charMap;
    }

    public void setCharMap(ICharMap charMap) {
        this.charMap = charMap;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void setSystemPalette(SystemPalette systemPalette) {
        this.systemPalette = systemPalette;
    }

    public SystemPalette getSystemPalette() {
        return systemPalette;
    }

    public void setPatchManager(IPatchManager patchManager) {
        this.patchManager = patchManager;
    }

    public IPatchManager getPatchManager() {
        return patchManager;
    }

    /**
     * 获取编辑器实例
     *
     * @param editor 编辑器类型
     * @return 编辑器
     */
    <E extends IRomEditor> E getEditor(Class<? extends IRomEditor> editor) {
        return getEditorManager().getEditor(editor);
    }

    /**
     * 获取所有编辑器实例
     *
     * @return 所有编辑器
     */
    public Map<Class<? extends IRomEditor>, IRomEditor> getEditors() {
        return getEditorManager().getEditors();
    }

    @TestOnly
    public void debugLoadTime() {
        getEventHandler().register(new EventListener() {
            private final Map<IRomEditor, Long> times = new HashMap<>();

            public void onPre(EditorLoadEvent.Pre pre) {
                times.put(pre.getEditor(), System.currentTimeMillis());
            }

            public void onPost(EditorLoadEvent.Post post) {
                times.put(post.getEditor(), System.currentTimeMillis() - times.get(post.getEditor()));
                LOGGER.debug(post.getEditor().getClass().getSimpleName() + times.get(post.getEditor()));
            }

            public void onEnd(EditorManagerEvent.LoadPost post) {
                LOGGER.debug("time:" + times.values().stream().mapToInt(Long::intValue).sum());
                times.clear();
            }
        });
    }
}
