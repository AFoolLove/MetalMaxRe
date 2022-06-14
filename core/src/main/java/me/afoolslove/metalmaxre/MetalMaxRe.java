package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.IEditorManager;
import me.afoolslove.metalmaxre.event.EventHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author AFoolLove
 */
public class MetalMaxRe {
    private RomBuffer romBuffer;
    private IEditorManager editorManager;

    private EventHandler eventHandler = new EventHandler();

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
}
