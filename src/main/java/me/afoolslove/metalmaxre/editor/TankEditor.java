package me.afoolslove.metalmaxre.editor;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * 坦克编辑器
 *
 * @author AFoolLove
 */
public class TankEditor extends AbstractEditor {
    public byte weight;
    public byte defense;
    public char magazine;

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        return false;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        return false;
    }


}
