package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.editor.text.option.TextOffsetImpl;
import me.afoolslove.metalmaxre.editor.text.option.YesOrNo;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class TextRaw extends TreeSet<IBaseText> implements IBaseText {
    private static final Comparator<IBaseText> comparator = IBaseText::compareTo;

    public TextRaw() {
        super(comparator);
    }

    public TextRaw(Comparator<? super IBaseText> comparator) {
        super(comparator);
    }

    public TextRaw(@NotNull Collection<? extends IBaseText> c) {
        super(comparator);
        addAll(c);
    }

    public TextRaw(SortedSet<IBaseText> s) {
        super(s);
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (IBaseText iBaseText : this) {
            byteArrayOutputStream.writeBytes(iBaseText.toByteArray());
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static void main(String[] args) {
        var textRaw = new TextRaw();
        System.out.println(textRaw.add(new YesOrNo(0x05)));
        System.out.println(textRaw.add(new TextImpl("12355")));
        System.out.println(textRaw.add(new TextOffsetImpl(0x03)));
        var bytes = textRaw.toByteArray();
        System.out.println();
    }
}
