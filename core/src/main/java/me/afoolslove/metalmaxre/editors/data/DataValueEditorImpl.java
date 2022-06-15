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
 * 0xAC - 0xD0为3byte数据，但视作为2byte数据操作
 * <p>
 * 0xD1 - 0xE8为3byte数据，经过((x08*A)+(x09*0x100*A))计算后得出
 * <p>
 * *超过范围(0x00-0xE8)后不能保证数据准确性
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
            if (i < 0x24) {
                // 前0x24个数据值虽然属于3byte，但会被当作2byte使用，实际上也是2byte
                getValues().put(getValues().size(), NumberR.toInt(x3Bytes[i * 2], x3Bytes[(i * 2) + 1]));
            } else {
                final int A = 0x0A; // 程序中写死的值
                int x08 = x3Bytes[i * 2] & 0xFF;
                int x09 = x3Bytes[(i * 2) + 1] & 0xFF;
                getValues().put(getValues().size(), (x08 * A) + (x09 * 0x100 * A));
            }
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

        byte[] x3Bytes = new byte[get3ByteMaxCount() * 2];
        for (Map.Entry<Integer, Number> entry : get3ByteValues().entrySet()) {
            int index = entry.getKey() - (get1ByteMaxCount() + get2ByteMaxCount());
            if (index < 0x24) {
                // 前0x24个数据值虽然属于3byte，但会被当作2byte使用，实际上也是2byte
                var value = NumberR.toByteArray(entry.getValue().intValue(), 2, false);
                x3Bytes[index * 2] = value[0];
                x3Bytes[(index * 2) + 1] = value[1];
                continue;
            }

            final int A = 0x0A;  // 程序中写死的值
            var value = entry.getValue().intValue();
            int x09 = value / (0x100 * A); // 得到 x09
            int x08 = (value - (x09 * 0x100 * A)) / A; // 得到 x08

            x3Bytes[index * 2] = (byte) x08;
            x3Bytes[(index * 2) + 1] = (byte) x09;
        }

        getBuffer().put(get1ByteAddress(), x1Bytes);
        getBuffer().put(get2ByteAddress(), x2Bytes);
        getBuffer().put(get3ByteAddress(), x3Bytes);
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
