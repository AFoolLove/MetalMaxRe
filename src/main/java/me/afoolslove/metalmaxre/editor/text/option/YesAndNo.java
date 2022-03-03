package me.afoolslove.metalmaxre.editor.text.option;

import me.afoolslove.metalmaxre.editor.text.IBaseText;
import org.jetbrains.annotations.Range;

/**
 * 对话时进行选择，选择Yes和No都会进行不同的对话
 * <p>
 * *只能在一段对话的末尾生效（编译时会自动移动到末尾），否则当前对话段直接结束
 * <p>
 * TODO: 未添加索引到其它对话段
 *
 * @see YesOrNo
 */
public class YesAndNo implements IBaseText {
    private final byte[] value = {(byte) 0xE8, 0x00, 0x00};

    public YesAndNo(byte yes, byte no) {
        this.value[0x01] = yes;
        this.value[0x02] = no;
    }

    public YesAndNo(@Range(from = 0x00, to = 0xFF) int yes,
                    @Range(from = 0x00, to = 0xFF) int no) {
        this.value[0x01] = (byte) (yes & 0xFF);
        this.value[0x02] = (byte) (no & 0xFF);
    }

    @Override
    public byte[] toByteArray() {
        return value;
    }
}
