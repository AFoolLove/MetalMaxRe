package me.afoolslove.metalmaxre.editors.data;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 0x00 - 0x46为1byte数据
 * <p>
 * 0x47 - 0xAB为2byte数据
 * <p>
 * 0xAC - ???为3byte数据，3byte数据经过多个计算后得出，暂时没有追溯
 * <p>
 * <p>
 * 目前只能修改1byte和2byte的数据值，3byte只能读取不能修改
 * <p>
 * *0x47和0x46有交集，0x46作为0x47的低位byte
 *
 * @author AFoolLove
 */
public class DataValueEditorImpl extends RomBufferWrapperAbstractEditor implements IDataValueEditor {
    private final DataAddress x1ByteAddress;
    private final DataAddress x2ByteAddress;
    private final DataAddress x3ByteAddress;

    private final Map<Integer, Number> VALUES = new HashMap<>();

    public DataValueEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x220FA - 0x10, 0x22130 - 0x10),
                DataAddress.fromPRG(0x22130 - 0x10, 0x22209 - 0x10),
                DataAddress.fromPRG(0x2220A - 0x10, 0x22283 - 0x10)
        );
    }

    public DataValueEditorImpl(@NotNull MetalMaxRe metalMaxRe, DataAddress x1ByteAddress, DataAddress x2ByteAddress, DataAddress x3ByteAddress) {
        super(metalMaxRe);
        this.x1ByteAddress = x1ByteAddress;
        this.x2ByteAddress = x2ByteAddress;
        this.x3ByteAddress = x3ByteAddress;
    }

    @Editor.Load
    public void onLoad() {
        getValues().clear();


        byte[] x1Bytes = new byte[get1ByteMaxCount()];
        getBuffer().get(get1ByteAddress(), x1Bytes);
        for (byte x1Byte : x1Bytes) {
            getValues().put(getValues().size(), x1Byte);
        }

        byte[] x2Bytes = new byte[get2ByteMaxCount() * 2];
        getBuffer().get(get2ByteAddress(), x2Bytes);
        for (int i = 0; i < get2ByteMaxCount(); i++) {
            getValues().put(getValues().size(), NumberR.toInt(x2Bytes[i * 2], x2Bytes[(i * 2) + 1]));
        }

        byte[] x3Bytes = new byte[get3ByteMaxCount() * 2];
        getBuffer().get(get3ByteAddress(), x3Bytes);

        for (int i = 0; i < get3ByteMaxCount(); i++) {
            byte[] data = EE41((byte) getValues().size(), x3Bytes);
            getValues().put(getValues().size(), NumberR.toInt(data[0], data[1], data[2]));
        }
    }

    @Editor.Apply
    public void onApply() {
        byte[] x1Bytes = new byte[get1ByteMaxCount()];
        for (Map.Entry<Integer, Number> entry : get1ByteValues().entrySet()) {
            x1Bytes[entry.getKey()] = entry.getValue().byteValue();
        }

        byte[] x2Bytes = new byte[get2ByteMaxCount() * 2];
        for (Map.Entry<Integer, Number> entry : get2ByteValues().entrySet()) {
            var data = NumberR.toByteArray(entry.getValue().intValue(), 2, false);
            x2Bytes[(entry.getKey() - get1ByteMaxCount()) * 2] = data[0];
            x2Bytes[((entry.getKey() - get1ByteMaxCount()) * 2) + 1] = data[1];
        }

//        byte[] x3Bytes = new byte[get3ByteMaxCount() * 2];

        getBuffer().put(get1ByteAddress(), x1Bytes);
        getBuffer().put(get2ByteAddress(), x2Bytes);
//        getBuffer().put(get3ByteAddress(), x3Bytes);
    }

    private static byte[] EE41(byte A, byte[] x81FA) {
        // A寄存器储存数据值的key
        // X寄存器储存物品ID（但是会被初始化为0）
        // Y寄存器未知
        byte X, Y;

        // $00-$02储存3byte的数据值，初始化为0
        int x00 = 0, x01 = 0, x02 = 0;

        int x80EA = 0x80EA; // 1byte数据值使用
        int x8130 = 0x8130, x8131 = 0x8131; // 2byte数据值使用

        // 3byte数据值使用
        int x08 = 0, x09 = 0;
//        int x81FA = 0x81FA, x81FB = 0x81FB;

        // 数据值key与0x47对比
        if ((A & 0xFF) < 0x47) {
            // 数据值key小于0x47
            X = A;
            A = (byte) (x80EA + X); // 这里是获取目标内存地址的值，不是内存地址
            x00 = A;
            return new byte[]{(byte) x00, (byte) x01, (byte) x02}; // 单字节的数据值结束
        } else {
            // 数据值key大于或等于0x47
            // 数据值key与0xAC对比
            if ((A & 0xFF) < 0xAC) {
                // 数据值key小于0xAC
                A -= 0x47;
                A <<= 1;
                X = A;
                x00 = x8130 + X;
                x01 = x8131 + X;
                return new byte[]{(byte) x00, (byte) x01, (byte) x02}; // 双字节的数据值结束
            } else {
                // 数据值key大于或等于0xAC
                A -= 0xAC;
                A <<= 1;
                X = A;
                A = x81FA[X];
//                A = (byte) 0xD6;
                x00 = A;
                x08 = A;
                A = x81FA[X + 1];
//                A = (byte) 0x1F;
                x01 = A;
                x09 = A;

                if ((X & 0xFF) < 0x48) {
                    // 直接结束
                    return new byte[]{(byte) x00, (byte) x01, (byte) x02};
                }
                A = 0x0A;
                x00 = A;
                byte[] bs = D654(x08, x09, x00);
                x00 = bs[0];
                x01 = bs[1];
                x02 = bs[2];
                return bs;
            }
        }
    }

    private static byte[] D654(int x08, int x09, int x00) {
        int A, X, Y; // 寄存器

        int x0E, x0F, x10; // 这个函数会使用的内存地址

        A = 0;
        x0E = A;
        x0F = A;
        x10 = A;

        X = 0x08;
        do { // D660 循环X次
            boolean C = (x00 & 0B0000_0001) == 1; // C标志位，x00右移一位后的状态
            x00 >>>= 1;
            if (C) {
                x0F += (x08 & 0xFF);

                x10 += (x09 & 0xFF) + (x0F / 0x100); // x0F超过0x100后x10会+1

                x0F %= 0x100;
                x10 %= 0x100;
            }
            // D671

            C = (x10 & 0B0000_0001) == 1;
            x10 >>>= 1;

            // TODO ROR右旋逻辑错误

            // ROR右旋 x0F
            boolean C1 = (x0F & 0B0000_0001) == 1; // 右旋结束后的C标志位状态
            x0F >>>= 1;
            if (C) {
                x0F |= 0B1000_0000;
            }
            C = C1;
            // ROR右旋结束 x0F

            // ROR右旋 x0E
            C1 = (x0E & 0B0000_0001) == 1; // 右旋结束后的C标志位状态
            x0E >>>= 1;
            if (C) {
                x0E |= 0B1000_0000;
            }
            C = C1;
            // ROR右旋结束 x0E
        } while (--X != 0);
        return new byte[]{(byte) x0E, (byte) x0F, (byte) x10};
    }

    @Override
    public DataAddress get1ByteAddress() {
        return x1ByteAddress;
    }

    @Override
    public DataAddress get2ByteAddress() {
        return x2ByteAddress;
    }

    @Override
    public DataAddress get3ByteAddress() {
        return x3ByteAddress;
    }

    @Override
    public @NotNull Map<Integer, Number> getValues() {
        return VALUES;
    }
}
