package me.afoolslove.metalmaxre.editors.text;

/**
 * 游戏中的一段文本
 * <p>
 * 只储存文本，请不要储存文本以外的数据
 *
 * @author AFoolLove
 */
public class Text extends AbstractBaseText {
    private final StringBuilder builder = new StringBuilder();

    public Text(Text text) {
        append(text);
    }

    public Text(String text) {
        append(text);
    }

    public Text append(Text text) {
        builder.append(text.toText());
        return this;
    }

    public Text append(String text) {
        builder.append(text);
        return this;
    }

    @Override
    public byte[] toByteArray() {
        var chars = builder.toString();
        return WordBank.toBytes(chars);
    }

    @Override
    public String toText() {
        return builder.toString();
    }

    @Override
    public String toString() {
        return toText();
    }
}
