package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.editors.text.action.ByteAction;
import me.afoolslove.metalmaxre.editors.text.action.ByteArrayAction;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TextBuilder implements IBaseText {

    private final List<IBaseText> texts = new ArrayList<>();
    private boolean has9F = false;

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

    public boolean has9F() {
        return has9F;
    }

    public boolean has9F(boolean has9F) {
        this.has9F = has9F;
        return has9F;
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (IBaseText text : texts) {
            outputStream.writeBytes(text.toByteArray());
        }
        if (has9F()) {
            outputStream.write(0x9F);
        }
//        if (texts.isEmpty() || !(texts.get(texts.size() - 1) instanceof SelectAction)) {
//            // 如果最后一个不是SelectAction，就需要0x9F结尾，否则不需要
//            outputStream.write(0x9F);
//        }
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
        return toText(false);
    }

    public String toText(boolean include9F) {
        StringBuilder builder = new StringBuilder();
        for (IBaseText text : texts) {
            builder.append(text.toText());
        }
        if (include9F && has9F()) {
            builder.append("[9F]");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return String.format("size=%d,text=%s", texts.size(), toText());
    }

    /**
     * 将文本段转换为 List<TextBuilder>
     *
     * @param text 文本
     * @return List<TextBuilder>
     */
    public static List<TextBuilder> fromTexts(char[] text) {
        if (text == null || text.length == 0) {
            // null或空文本返回空的list
            return Collections.emptyList();
        }

        final LinkedList<TextBuilder> textBuilders = new LinkedList<>();

        TextBuilder textBuilder = new TextBuilder();

        // 用于文本拼接
        final StringBuilder builder = new StringBuilder();
        charsLoop:
        for (int i = 0; i < text.length; ) {
            char ch = text[i];

            if (ch == '[') {
                // 判断是否为十六进制 [00FF] [00 FF] [0 F] 等，
                // []里面只能是十六进制
                // 持续读取到末尾或']'
                while (++i < text.length) {
                    if (text[i] == ']') {
                        break;
                    }
                    builder.append(text[i]);
                }
                if (!builder.isEmpty()) {
                    // 将字符十六进制转换为byte数组
                    byte[] bytes = readBracketHex(builder.toString().toCharArray());
                    builder.setLength(0);

                    if (bytes.length == 1) {
                        textBuilder.add(new ByteAction(bytes[0]));
                    } else if (bytes.length > 1) {
                        textBuilder.add(new ByteArrayAction(bytes));
                    }
                    i++;
                    continue;
                }
            }

            // 从重复单byte中获取
            Object temp = WordBank.FONTS_SINGLE_REPEATED.get(ch);
            if (temp == null) {
                // 从单byte中获取
                temp = WordBank.FONTS_SINGLE.get(ch);
            }
            if (temp == null) {
                // 从多byte中获取
                temp = WordBank.FONTS.get(ch);
            }
            if (temp == null) {
                // 从重复多byte中获取
                temp = WordBank.FONTS_REPEATED.get(ch);
            }
            if (temp != null) {
                if (temp instanceof byte[] bytes) {
                    // 写入多字节
                    textBuilder.add(new Text(Character.toString(ch)));
                } else {
                    // 写入单字节
                    textBuilder.add(new ByteAction((byte) temp));
                }
            } else {
                // 没有这个字符
                System.out.println("未知字符：" + ch);
            }
            i++;
        }
        textBuilders.add(textBuilder);
        return textBuilders;
    }


    /**
     * 不包含 []
     */
    public static byte[] readBracketHex(char[] chars) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (int i = 0; i < chars.length; ) {
            char ch = chars[i++];

            if (ch == ' ') {
                continue;
            }

            // 转换为大写
            if (ch >= 'a' && ch <= 'f') {
                ch -= 32;
            }

            if (i >= chars.length || chars[i] == ' ') {
                // 单十六进制 0-F
                outputStream.write(WordBank.HEX_DIGITS.indexOf(ch));
                continue;
            }

            // 转换为大写
            if (chars[i] >= 'a' && chars[i] <= 'f') {
                chars[i] -= 32;
            }
            // 高F(4bit)+低F(4bit)
            outputStream.write((WordBank.HEX_DIGITS.indexOf(ch) << 4) + WordBank.HEX_DIGITS.indexOf(chars[i++]));
        }
        return outputStream.toByteArray();
    }

    public static void main(String[] args) {
        var textBuilders = fromTexts("你的名字是[E8]，想知道我的名字吗？[F70001][EB0001]".toCharArray());
        System.out.println();
    }

    public static boolean isHexChar(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f');
    }
}
