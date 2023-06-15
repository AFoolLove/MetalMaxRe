package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import org.jetbrains.annotations.NotNull;

/**
 * 基本文本
 *
 * @author AFoolLove
 */
public interface IBaseText extends Comparable<IBaseText> {
    /**
     * 在多个文本中的优先级
     * <p>
     * 越低越靠前，越高越靠后
     *
     * @return 优先级
     */
    default byte priority() {
        return 0x00;
    }

    /**
     * @return 转换为游戏中使用的文本字节
     */
    byte[] toByteArray(@NotNull ICharMap charMap);

    /**
     * @return 转换为文本
     */
    String toText(@NotNull ICharMap charMap);

    /**
     * @return 占用长度
     */
    int length(@NotNull ICharMap charMap);

    @Override
    default int compareTo(@NotNull IBaseText o) {
        return Byte.compare(priority(), o.priority());
    }
}
