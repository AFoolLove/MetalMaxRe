package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.AbstractEditor;
import org.jetbrains.annotations.NotNull;

public abstract class RomBufferWrapperAbstractEditor extends AbstractEditor {
    private final EditorRomBufferWrapper romBufferWrapper;

    public RomBufferWrapperAbstractEditor(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
        this.romBufferWrapper = new EditorRomBufferWrapper(metalMaxRe.getBuffer(), this);
    }

    public RomBufferWrapperAbstractEditor(@NotNull MetalMaxRe metalMaxRe, boolean enabled) {
        super(metalMaxRe, enabled);
        this.romBufferWrapper = new EditorRomBufferWrapper(metalMaxRe.getBuffer(), this);
    }

    @Override
    public @NotNull EditorRomBufferWrapper getBuffer() {
        return romBufferWrapper;
    }
}
