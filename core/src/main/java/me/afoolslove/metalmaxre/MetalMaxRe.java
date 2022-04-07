package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.IEditorManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author AFoolLove
 */
public class MetalMaxRe {
    private RomBuffer romBuffer;
    private IEditorManager editorManager;

    private MetalMaxRe() {
    }

    public MetalMaxRe(@NotNull RomBuffer romBuffer) {
        this.romBuffer = romBuffer;
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
}
