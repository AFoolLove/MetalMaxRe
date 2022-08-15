package me.afoolslove.metalmaxre.io;

import java.io.ByteArrayInputStream;

/**
 * 二进制输入流
 *
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
