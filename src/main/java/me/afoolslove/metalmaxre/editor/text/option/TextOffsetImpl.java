package me.afoolslove.metalmaxre.editor.text.option;

import me.afoolslove.metalmaxre.editor.text.BaseTextImpl;
import org.jetbrains.annotations.Range;

/**
 * 偏移文本，不影响中间的文本
 */
public class TextOffsetImpl extends BaseTextImpl {
    private final byte[] value = {(byte) 0xED, 0x00};

    /**
     * 可用于字节占位
     */
    public TextOffsetImpl() {
    }

    public TextOffsetImpl(byte offset) {
        this.value[0x01] = offset;
    }

    public TextOffsetImpl(@Range(from = 0x00, to = 0xFF) int offset) {
        this.value[0x01] = (byte) (offset & 0xFF);
    }

    @Override
    public byte[] toByteArray() {
        return value;
    }
}
