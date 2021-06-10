package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * 文本编辑器
 *
 * @author AFoolLove
 */
public class TextEditor extends AbstractEditor {

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        return false;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        return false;
    }
}
