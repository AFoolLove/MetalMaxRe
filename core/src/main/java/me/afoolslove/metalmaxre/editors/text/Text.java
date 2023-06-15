package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 游戏中的一段文本
 * <p>
 * 只储存文本，请不要储存文本以外的数据
 *
 * @author AFoolLove
 */
public class Text extends AbstractBaseText {
    private final StringBuilder builder = new StringBuilder();

    public Text(Text text, @Nullable ICharMap charMap) {
        append(text, charMap);
    }

    public Text(Text text) {
        append(text, null);
    }

    public Text(CharSequence text) {
        append(text.toString());
    }

    public Text(String text) {
        append(text);
    }

    public Text append(Text text, @Nullable ICharMap charMap) {
        builder.append(text.toText(charMap));
        return this;
    }

    public Text append(String text) {
        builder.append(text);
        return this;
    }

    @Override
    public byte[] toByteArray(@NotNull ICharMap charMap) {
        String chars = builder.toString();
        return charMap.toBytes(chars, null);
    }

    @Override
    public String toText(@Nullable ICharMap charMap) {
        return builder.toString();
    }

    @Override
    public int length(@NotNull ICharMap charMap) {
        return toByteArray(charMap).length;
    }

    @Override
    public String toString() {
        return toText(null);
    }
}
