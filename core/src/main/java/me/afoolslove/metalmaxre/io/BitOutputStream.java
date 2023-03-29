package me.afoolslove.metalmaxre.io;

import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 二进制输出流
 *
 * @author AFoolLove
 */
public class BitOutputStream extends ByteArrayOutputStream {

    private int nextBitIndex = 0; // 下一个被写入的bit位

    /**
     * 写入一个bit
     */
    public synchronized void writeBit(boolean bit) {
        if (count == 0) {
            write(0B0000_0000);
        }

        if (bit) {
            // 将bit置1
            buf[count - 1] |= (0B0000_0001 << (7 - nextBitIndex));
        } // 默认为0，不需要进行操作

        // 进位和循环
        if (++nextBitIndex > 0x07) {
//            nextBitIndex = 0x00;
            write(0B0000_0000);
        }
    }

    /**
     * 写入一个bit，非0为1
     */
    public void writeBit(int bit) {
        writeBit(bit != 0);
    }

    /**
     * 写入一个字节的8个bit
     */
    public synchronized void writeBit(byte bits) {
        if (count == 0) {
            write(0B0000_0000);
        }

        if (nextBitIndex == 0x00) {
            // 新字节还未写入任何bit，直接将bits写入
            buf[count - 1] = bits;
            super.write(0B0000_0000);
            return;
        }

        // 将剩余可写入的bit数量填满，并将剩下的bit写入到下一个字节里

        // 0B101_00000 |= (0B1111_1111 >> 2 + 1);
        // 0B101_00000 |= (0B1111_1111 >> 3);
        // 0B101_00000 |= 0B0001_1111;
        // 0B101_111111; 0B1011_11111;
        buf[count - 1] |= (bits >> nextBitIndex);
        // 0B1111_1111 << (8 - 2);
        // 0B1111_1111 << 6;
        // 0B11_000000; 0B1100_0000;
        super.write(bits << (8 - nextBitIndex)); // 调用父类的写入，防止nextBitIndex被置零
        // 0B1011_11111 + 0B1100_0000
        // 0B[101](1_11111 + 0B11)00_0000

        // nextBitIndex不需要进行变更
    }

    /**
     * 写入一个字节的8个bit
     */
    public synchronized void writeBits(byte[] bits) {
        if (count == 0) {
            write(0B0000_0000);
        }

        if (nextBitIndex == 0x00) {
            // 新字节还未写入任何bit，直接将bits写入
            writeBytes(bits);
            return;
        }

        for (byte bit : bits) {
            writeBit(bit);
        }
    }

    /**
     * 写入指定数量的bit
     */
    public synchronized void writeBits(int bits, int index, int length) {
        int at = NumberR.at(bits, index, length, true);
        for (int i = 0; i < length; i++) {
            writeBit((at & 0B0000_0001) != 0x00);
            at >>>= 1;
        }
    }


    @Override
    public synchronized void write(int b) {
        // 直接写入字节会使下一个被写入的bit位变为初始位
        nextBitIndex = 0;
        super.write(b);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        // 直接写入字节会使下一个被写入的bit位变为初始位
        nextBitIndex = 0;
        super.write(b, off, len);
    }

    @Override
    public void writeBytes(byte[] b) {
        // 直接写入字节会使下一个被写入的bit位变为初始位
        nextBitIndex = 0;
        super.writeBytes(b);
    }

    @Override
    public void write(@NotNull byte[] b) throws IOException {
        // 直接写入字节会使下一个被写入的bit位变为初始位
        nextBitIndex = 0;
        super.write(b);
    }

    @Override
    public synchronized int size() {
        if (count != 0 && nextBitIndex == 0x00) {
            return count - 1;
        }
        return super.size();
    }

    @NotNull
    @Override
    public synchronized byte[] toByteArray() {
        if (count != 0 && nextBitIndex == 0x00) {
            // 多余的字节
            return Arrays.copyOf(buf, count - 1);
        }
        return super.toByteArray();
    }

    public static void main(String[] args) {
        BitOutputStream outputStream = new BitOutputStream();
        outputStream.writeBit(false);
        outputStream.writeBit(false);
        outputStream.writeBit(false);
        outputStream.writeBit(false);
        outputStream.writeBit(true);
        outputStream.writeBit(true);
        outputStream.writeBit(false);
        outputStream.writeBit(false);
        System.out.println(outputStream);
    }
}
