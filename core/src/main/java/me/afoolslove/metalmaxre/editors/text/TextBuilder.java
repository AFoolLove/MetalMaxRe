package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.editors.text.action.SelectAction;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TextBuilder implements IBaseText {

    private final List<IBaseText> texts = new ArrayList<>();

    public TextBuilder() {
    }

    public TextBuilder(IBaseText text) {
        texts.add(text);
    }

    public TextBuilder add(@NotNull IBaseText baseText) {
        // 如果上一个和当前都是Text，直接添加到上一个里，而不是添加到当前list里
        if (baseText instanceof Text text && !texts.isEmpty()) {
            IBaseText lastBaseText = texts.get(texts.size() - 1);
            if (lastBaseText instanceof Text lastText) {
                // 添加到上一个Text里
                lastText.append(text);
                return this;
            }
        }
        texts.add(baseText);
        // 重新排序
        texts.sort(null);
        return this;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (IBaseText text : texts) {
            outputStream.writeBytes(text.toByteArray());
        }
        if (texts.isEmpty() || !(texts.get(texts.size() - 1) instanceof SelectAction)) {
            // 如果最后一个不是SelectAction，就需要0x9F结尾，否则不需要
            outputStream.write(0x9F);
        }
        return outputStream.toByteArray();
    }

    public boolean isEmpty() {
        return texts.isEmpty();
    }

    public int size() {
        return texts.size();
    }

    @Override
    public String toText() {
        StringBuilder builder = new StringBuilder();
        for (IBaseText text : texts) {
            builder.append(text.toText());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("size=%d,text=%s", texts.size(), toText());
    }
}
