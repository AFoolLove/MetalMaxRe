package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.IEditorManager;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.palette.SystemPalette;
import me.afoolslove.metalmaxre.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author AFoolLove
 */
public class MetalMaxRe {
    private RomBuffer romBuffer;
    private IEditorManager editorManager;

    private EventHandler eventHandler = new EventHandler();

    private SystemPalette systemPalette = SystemPalette.DEFAULT_SYSTEM_PALETTE;

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
            setEditorManager(new EditorManagerImpl(this));
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

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void setSystemPalette(SystemPalette systemPalette) {
        this.systemPalette = systemPalette;
    }

    public SystemPalette getSystemPalette() {
        return systemPalette;
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
}
