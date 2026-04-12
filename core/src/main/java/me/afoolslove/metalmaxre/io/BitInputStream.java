package me.afoolslove.metalmaxre.io;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 二进制输入流
 *
 * @author AFoolLove
 */
public class BitInputStream extends ByteArrayInputStream {
    private int nextBitIndex = 0; // 下一个被读取的bit位

    /**
     * 构造函数
     */
    public BitInputStream(byte[] buf) {
        super(buf);
    }

    /**
     * 构造函数
     */
    public BitInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    /**
     * 读取一个bit
     */
    public synchronized boolean readBit() {
        if (pos >= count) {
            return false;
        }

        if (nextBitIndex == 0) {
            // 需要读取下一个字节
            int b = read();
            if (b == -1) {
                return false;
            }
            // 将读取的字节放回缓冲区，以便按位读取
            pos--;
            buf[pos] = (byte) b;
        }

        // 从当前字节中提取指定位置的bit
        boolean bit = (buf[pos] & (0B1000_0000 >> nextBitIndex)) != 0;

        // 移动到下一个bit位置
        if (++nextBitIndex > 0x07) {
            nextBitIndex = 0;
            pos++; // 移动到下一个字节
        }

        return bit;
    }

    /**
     * 读取一个字节的8个bit
     */
    public synchronized int readBitByte() {
        if (pos >= count) {
            return -1;
        }

        if (nextBitIndex == 0) {
            // 直接读取整个字节
            return read();
        }

        // 从当前位置读取8个bit组成一个字节
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 1;
            if (readBit()) {
                result |= 1;
            }
        }
        return result & 0xFF;
    }

    /**
     * 读取指定数量的bit并组合成整数
     */
    public synchronized int readBits(int length) {
        return readBits(length, false);
    }

    /**
     * 读取指定数量的bit并组合成整数
     *
     * @param reverse 是否倒序读取该长度的bit
     */
    public synchronized int readBits(int length, boolean reverse) {
        if (length <= 0) {
            return 0;
        }

        int result = 0;
        if (!reverse) {
            for (int i = 0; i < length; i++) {
                result <<= 1;
                if (readBit()) {
                    result |= 1;
                }
            }
        } else {
            for (int i = 0; i < length; i++) {
                if (readBit()) {
                    result |= (1 << i);
                }
            }
        }
        return result;
    }

    /**
     * 跳过指定数量的bit
     */
    public synchronized long skipBits(long n) {
        if (n <= 0) {
            return 0;
        }

        long skipped = 0;
        while (skipped < n && pos < count) {
            readBit();
            skipped++;
        }
        return skipped;
    }

    /**
     * 返回剩余可读取的bit数
     */
    public synchronized long availableBits() {
        return (count - pos - 1) * 8L + (8 - nextBitIndex);
    }

    @Override
    public synchronized int read() {
        // 直接读取字节会使下一个被读取的bit位变为初始位
        nextBitIndex = 0;
        return super.read();
    }

    @Override
    public synchronized int read(byte[] b, int off, int len) {
        // 直接读取字节会使下一个被读取的bit位变为初始位
        nextBitIndex = 0;
        return super.read(b, off, len);
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        // 直接读取字节会使下一个被读取的bit位变为初始位
        nextBitIndex = 0;
        return super.read(b);
    }

    @Override
    public synchronized long skip(long n) {
        // 直接跳过字节会使下一个被读取的bit位变为初始位
        nextBitIndex = 0;
        return super.skip(n);
    }

    @Override
    public synchronized void reset() {
        super.reset();
        nextBitIndex = 0;
    }

    /**
     * 判断是否所有bit都已经读取完毕
     *
     * @return 如果没有更多bit可读取则返回true，否则返回false
     */
    public synchronized boolean hasBits() {
        return availableBits() > 0;
    }

    public static void main(String[] args) {
        BitOutputStream outputStream = new BitOutputStream();
        outputStream.writeBit(false);
        outputStream.writeBit(true);
        outputStream.writeBit(true);
        outputStream.writeBit(true);
        outputStream.writeBit(true);
        outputStream.writeBit(false);
        outputStream.writeBit(false);
        outputStream.writeBit(false);

        byte[] testData = new byte[]{(byte) 0B1100_1010, (byte) 0B1011_0101};
        BitInputStream inputStream = new BitInputStream(outputStream.toByteArray());

        while (inputStream.hasBits()) {
//            boolean bit = inputStream.readBit();
//            System.out.print(bit ? "1" : "0");
            System.out.println(inputStream.readBits(4, true));
        }
    }
}