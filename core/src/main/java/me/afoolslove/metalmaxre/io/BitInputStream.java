package me.afoolslove.metalmaxre.io;

import org.jetbrains.annotations.Range;

import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * @author AFoolLove
 */
public class BitInputStream extends ByteArrayInputStream {
    private int nextBitIndex = 0;  // 下一个被读取的bit位

    public BitInputStream(byte[] buf) {
        super(buf);
    }

    public BitInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }
}
