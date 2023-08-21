package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.event.editors.editor.EditorDisabledEvent;
import me.afoolslove.metalmaxre.event.editors.editor.EditorEnabledEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 实现了部分基本功能的编辑器
 *
 * @author AFoolLove
 */
public abstract class AbstractEditor implements IRomEditor {
    private final MetalMaxRe metalMaxRe;
    private boolean enabled;
    private int position = 0;

    protected AbstractEditor(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe, true);
    }

    protected AbstractEditor(@NotNull MetalMaxRe metalMaxRe, boolean enabled) {
        this.metalMaxRe = metalMaxRe;
        this.enabled = enabled;
    }

    @NotNull
    @Override
    public MetalMaxRe getMetalMaxRe() {
        return metalMaxRe;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            metalMaxRe.getEventHandler().callEvent(new EditorEnabledEvent(metalMaxRe, this));
        } else {
            metalMaxRe.getEventHandler().callEvent(new EditorDisabledEvent(metalMaxRe, this));
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 获取当前编辑器的RomBuffer的位置
     * <p>
     * *位置为绝对位置，包括头文件0x10等
     *
     * @return 当前编辑器的RomBuffer的位置
     */
    @Override
    public int position() {
        return position;
    }

    @Override
    public synchronized int position(int position) {
        if (this.position == position) {
            return position;
        }
        this.position = position;
        return position;
    }

    @Override
    public synchronized int chrPosition(int chrPosition) {
        this.position = getBuffer().getHeader().getChrRomStart(chrPosition);
        return position;
    }

    @Override
    public synchronized int prgPosition(int prgPosition) {
        this.position = getBuffer().getHeader().getPrgRomStart(prgPosition);
        return position;
    }

    @Override
    public synchronized int offsetPosition(int offset) {
        return IRomEditor.super.offsetPosition(offset);
    }
}
