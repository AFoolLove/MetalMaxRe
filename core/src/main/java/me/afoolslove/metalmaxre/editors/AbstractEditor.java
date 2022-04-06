package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractEditor<L extends IEditorListener> implements IRomEditor {
    private final MetalMaxRe metalMaxRe;
    private int position = 0;
    private final List<L> listeners = new LinkedList<>();

    protected AbstractEditor(@NotNull MetalMaxRe metalMaxRe) {
        this.metalMaxRe = metalMaxRe;
    }

    @NotNull
    @Override
    public MetalMaxRe getMetalMaxRe() {
        return metalMaxRe;
    }

    @NotNull
    @Override
    public List<L> getListeners() {
        return listeners;
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public synchronized int position(int position) {
        this.position = position;
        return position;
    }
}
