package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;

/**
 * 实现了部分基本功能的编辑器
 *
 * @author AFoolLove
 */
public abstract class AbstractEditor implements IRomEditor {
    private final MetalMaxRe metalMaxRe;
    private int position = 0;

    protected AbstractEditor(@NotNull MetalMaxRe metalMaxRe) {
        this.metalMaxRe = metalMaxRe;
    }

    @NotNull
    @Override
    public MetalMaxRe getMetalMaxRe() {
        return metalMaxRe;
    }

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
