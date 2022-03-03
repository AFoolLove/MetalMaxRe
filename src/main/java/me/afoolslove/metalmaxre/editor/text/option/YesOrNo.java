package me.afoolslove.metalmaxre.editor.text.option;

import me.afoolslove.metalmaxre.editor.text.BaseTextImpl;
import org.jetbrains.annotations.Range;

/**
 * 对话时进行选择，选择Yes会继续对话，选择No会结束当前对话
 * <p>
 * *只能在一段对话的末尾生效（编译时会自动移动到末尾），否则当前对话段直接结束
 * <p>
 * * TODO: 未添加索引到其它对话段
 *
 * @see YesAndNo
 */
public class YesOrNo extends BaseTextImpl {

    public YesOrNo(byte yes) {
        super((byte) 0xE3, yes);
    }

    public YesOrNo(@Range(from = 0x00, to = 0xFF) int yes) {
        super((byte) 0xE3, (byte) yes);
    }
}
