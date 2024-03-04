package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.editors.text.action.*;
import me.afoolslove.metalmaxre.editors.text.mapping.CharMapCN;
import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class TextBuilder implements IBaseText {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextBuilder.class);

    private final List<IBaseText> texts = new ArrayList<>();
    private boolean has9F = false;

    public TextBuilder() {
    }

    public TextBuilder(IBaseText text) {
        texts.add(text);
    }

    public TextBuilder(boolean has9F) {
        has9F(has9F);
    }

    public TextBuilder(IBaseText text, boolean has9F) {
        texts.add(text);
        has9F(has9F);
    }

    public TextBuilder add(@NotNull IBaseText baseText, @NotNull ICharMap charMap) {
        // 如果上一个和当前都是Text，直接添加到上一个里，而不是添加到当前list里
        if (baseText instanceof Text text && !texts.isEmpty()) {
            IBaseText lastBaseText = texts.get(texts.size() - 1);
            if (lastBaseText instanceof Text lastText) {
                // 添加到上一个Text里
                lastText.append(text, charMap);
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
    public byte[] toByteArray(@NotNull ICharMap charMap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (IBaseText text : texts) {
            outputStream.writeBytes(text.toByteArray(charMap));
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
    public String toText(@NotNull ICharMap charMap) {
        return toText(false, charMap);
    }

    public String toText(boolean include9F, @NotNull ICharMap charMap) {
        StringBuilder builder = new StringBuilder();
        for (IBaseText text : texts) {
            builder.append(text.toText(charMap));
        }
        if (include9F && has9F()) {
            builder.append("[9F]");
        }
        return builder.toString();
    }

    @Override
    public int length(@NotNull ICharMap charMap) {
        int length = has9F() ? 1 : 0;
        for (IBaseText text : texts) {
            length += text.length(charMap);
        }
        return length;
    }

    public String toString(@NotNull ICharMap charMap) {
        return String.format("size=%d,text=%s", texts.size(), toText(charMap));
    }

    @Override
    public String toString() {
        return String.format("size=%d,-charMap is null-", texts.size());
    }

    /**
     * 将游戏中的字节转换为可操作的对象
     */
    public static List<TextBuilder> fromBytes(byte[] bytes, @NotNull ICharMap charMap) {
        List<TextBuilder> textBuilders = new ArrayList<>();

        // 储存一段对象文本
        TextBuilder textBuilder = null;
        // 临时储存纯文本
        StringBuilder text = new StringBuilder();

        text:
        for (int position = 0; position < bytes.length; ) {
            // 在字库中查找对应的字符文本
            allFonts:
            for (Map.Entry<Character, ?> entry : charMap.getValues()) {
                if (entry.getValue() instanceof byte[] bs) {
                    if (!bytesStartsWith(bytes, position, bs)) {
                        continue allFonts;
                    }
                    text.append(entry.getKey());
                    position += bs.length;
                    continue text;
                } else {
                    if (Objects.equals(entry.getValue(), bytes[position])) {
                        if (bytes[position] == (byte) 0x9F) {
                            if (textBuilder == null) {
                                textBuilder = new TextBuilder();
                            }
                            // 断句
                            // 保存当前文本，并清空缓存文本
                            if (!text.isEmpty()) {
                                textBuilder.add(new Text(text), charMap);
                                text.setLength(0);
                            }
                            textBuilder.has9F(true);

                            textBuilders.add(textBuilder);

                            textBuilder = null;
                        } else {
                            // 单byte对应的字符
                            text.append(entry.getKey());
                        }
                        position++;
                        continue text;
                    }
                }
            }

            // 单字节的操作码
            Integer opcodeLength = charMap.getOpcodes().get(bytes[position]);
            if (opcodeLength == null) {
                // 未知的数据
                text.append(String.format("[%02X]", bytes[position]));
                position++;
                continue text;
            }
            final int opcode = bytes[position] & 0xFF;

            switch (opcode) {
                case 0xE3: // 仅进行选择？？？？
                    if (textBuilder == null) {
                        textBuilder = new TextBuilder();
                    }
                    // 保存当前文本，并清空缓存文本
                    text.append("[E3]");
                    textBuilder.add(new Text(text), charMap);
                    text.setLength(0);
                    // 因为选择只能放在最后，所以结束当前文本
                    textBuilders.add(textBuilder);
                    position++;

                    textBuilder = null;
                    break;
                case 0xEB: // 进行选择
                    if (textBuilder == null) {
                        textBuilder = new TextBuilder();
                    }
                    // 保存当前文本，并清空缓存文本
                    if (!text.isEmpty()) {
                        textBuilder.add(new Text(text), charMap);
                        text.setLength(0);
                    }
                    // 添加选择action
                    textBuilder.add(new SelectAction(bytes[position + 0x01], bytes[position + 0x02]), charMap);
                    position += 3;

                    // 因为选择只能放在最后，所以结束当前文本
                    textBuilders.add(textBuilder);

                    textBuilder = null;
                    break;
                case 0xEE: // 全局文本速度
                    if (textBuilder == null) {
                        textBuilder = new TextBuilder();
                    }
                    // 保存当前文本，并清空缓存文本
                    if (!text.isEmpty()) {
                        textBuilder.add(new Text(text), charMap);
                        text.setLength(0);
                    }
                    // 添加设置文本速度
                    textBuilder.add(new TextSpeedAction(bytes[position + 0x01]), charMap);
                    position += 2;
                    break;
                case 0xED: // 空白占位
                    if (textBuilder == null) {
                        textBuilder = new TextBuilder();
                    }
                    // 保存当前文本，并清空缓存文本
                    if (!text.isEmpty()) {
                        textBuilder.add(new Text(text), charMap);
                        text.setLength(0);
                    }
                    // 添加占位
                    textBuilder.add(new SpaceAction(bytes[position + 0x01]), charMap);
                    position += 2;
                    break;
                case 0xF1: // 文本等待
                    if (textBuilder == null) {
                        textBuilder = new TextBuilder();
                    }
                    // 保存当前文本，并清空缓存文本
                    if (!text.isEmpty()) {
                        textBuilder.add(new Text(text), charMap);
                        text.setLength(0);
                    }
                    // 添加文本等待
                    textBuilder.add(new WaitTimeAction(bytes[position + 0x01]), charMap);
                    position += 2;
                    break;
                case 0xF5: // 精灵动作
                    if (textBuilder == null) {
                        textBuilder = new TextBuilder();
                    }
                    // 保存当前文本，并清空缓存文本
                    if (!text.isEmpty()) {
                        textBuilder.add(new Text(text), charMap);
                        text.setLength(0);
                    }
                    // 添加精灵动作
                    textBuilder.add(new SpriteAction(bytes[position + 0x01]), charMap);
                    position += 2;
                    break;
                case 0xF6:  // 读取到 0x9F 或 0x63 后结束
                    text.append("[F6");
                    // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                    // 0x63             (结束)
                    // 0x8C + 2byte     (填充数量，填充字符)
                    // 0x43 ???
                    whileF6:
                    while (true) {
                        if (++position >= bytes.length) {
                            // 读取数据完毕
                            text.append(']');
                            break text;
                        }

                        switch (bytes[position] & 0xFF) {
                            case 0x9E: // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                                // 写入 9E
                                text.append(" 9E");
                                // 空格填充
                                if (++position >= bytes.length) {
                                    // 读取完毕
                                    // 没有数量，直接结束
                                    text.append(']');
                                    break text;
                                }
                                // 填充数量
                                text.append(String.format("%02X ", bytes[position]));
                                break;
                            case 0x63: // 0x63             (结束)
                                // 0xF6 的结束符
                                text.append(" 63]");
                                position++;
                                continue text;
                            case 0x8C: // 0x8C + 2byte     (填充数量，填充字符)
                                text.append(" 8C");
                                // 使用指定字符填充指定数量
                                if (++position >= bytes.length) {
                                    // 读取完毕
                                    // 没有字符，也没有数量，直接结束
                                    text.append(']');
                                    break text;
                                }
                                // 填充数量
                                text.append(String.format("%02X", bytes[position]));
                                if (++position >= bytes.length) {
                                    // 读取完毕
                                    // 没有字符，直接结束
                                    text.append(']');
                                    break text;
                                }
                                // 填充字符
                                text.append(String.format("%02X ", bytes[position]));
                                break;
                            case 0x9F:
                                if (textBuilder == null) {
                                    textBuilder = new TextBuilder();
                                }
                                // 文本段结束，另一个的开始
                                text.append("]");

                                // 断句
                                // 保存当前文本，并清空缓存文本
                                textBuilder.add(new Text(text.toString()), charMap);
                                text.setLength(0);
                                textBuilder.has9F(true);

                                textBuilders.add(textBuilder);

                                textBuilder = null;

                                position++;
                                // emm？要不要结束？
                                break whileF6;
                            default:
                                // 写入不认识的字节
                                text.append(String.format("%02X", bytes[position]));
                                break;
                        }
                    }
                    break;
                default:
                    // 未知或无特殊数据的，直接读取相应的字节数量
                    int len = opcodeLength + 1; // 包含opcode
                    if (len == 1) {
                        text.append(String.format("[%02X]", bytes[position]));
                        position++;
                    } else {
                        if ((len + position) >= bytes.length) {
                            len = bytes.length - position;
                        }

                        text.append('[');
                        for (int j = 0; j < len; j++) {
                            text.append(String.format("%02X", bytes[position + j]));
                        }
                        // 读取结束
                        text.append(']');

                        position += len;
                    }

            }
        }
        if (!text.isEmpty()) {
            if (textBuilder == null) {
                textBuilder = new TextBuilder(new Text(text));
            }
        }
        if (textBuilder != null) {
            textBuilders.add(textBuilder);
        }
        return textBuilders;
    }

    /**
     * 在数组中查找是否存在指定的数组
     */
    private static boolean bytesStartsWith(byte[] bytes, int index, byte[] data) {
        if (bytes.length - index < data.length) {
            // bytes里的数据，不够验证data
            return false;
        }

        for (int i = 0; i < data.length; i++) {
            if (bytes[index + i] != data[i]) {
                return false;
            }
        }
        return true;
    }

    public static List<TextBuilder> fromTexts(String text, @NotNull ICharMap charMap) {
        if (text.isEmpty()) {
            return new LinkedList<>();
        }
        // 删除{}中的文本
        text = text.replaceAll("^\\{.+}$", "");

        text = text.replaceAll("\\\\t", "\t");
        text = text.replaceAll("\\\\r", "\r");
        text = text.replaceAll("\\\\n", "\n");
        text = text.replaceAll("\\\\b", "\b");

        text = text.replaceAll("\r\n", "");
        return fromTexts(text.toCharArray(), charMap);
    }

    /**
     * 将文本段转换为 List<TextBuilder>
     *
     * @param text 文本
     * @return List<TextBuilder>
     */
    public static List<TextBuilder> fromTexts(char[] text, @NotNull ICharMap charMap) {
        if (text == null || text.length == 0) {
            // null或空文本返回空的list
            return new LinkedList<>();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 用于十六进制拼接
        final StringBuilder builder = new StringBuilder();
        charsLoop:
        for (int i = 0; i < text.length; ) {
            char ch = text[i];

            if (ch == '\\' || ch == '\r') {
                i++;
                continue;
            }

            // 不需要{}中的任何文本
            if (ch == '{') {
                while (++i < text.length) {
                    if (text[i] == '}') {
                        i++;
                        continue charsLoop;
                    }
                }
            }

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
                    // []内的字节读取完毕，转换为字节后写入
                    outputStream.writeBytes(readBracketHexToBytes(builder.toString().toCharArray()));
                    // 清空
                    builder.setLength(0);

                    i++;
                    continue;
                }
            }

            // 从重复单byte中获取
            Object temp = charMap.getValue(ch);
            if (temp != null) {
                if (temp instanceof byte[] bytes) {
                    outputStream.writeBytes(bytes);
                } else {
                    outputStream.write(((byte) temp));
                }
            } else {
                // 没有这个字符
                LOGGER.debug("未知字符：{}", ch);
            }
            i++;
        }
        return fromBytes(outputStream.toByteArray(), charMap);
    }

    /**
     * 读取十六进制文本为文本段列表
     */
    public static List<TextBuilder> readBracketHex(char[] chars, @NotNull ICharMap charMap) {
        return fromBytes(readBracketHexToBytes(chars), charMap);
    }

    /**
     * 读取十六进制文本为字节数组
     */
    public static byte[] readBracketHexToBytes(char[] chars) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(chars.length);

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
                outputStream.write(ICharMap.HEX_DIGITS.indexOf(ch));
                continue;
            }

            // 转换为大写
            if (chars[i] >= 'a' && chars[i] <= 'f') {
                chars[i] -= 32;
            }

            // 高F(4bit)+低F(4bit)
            outputStream.write((ICharMap.HEX_DIGITS.indexOf(ch) << 4) + ICharMap.HEX_DIGITS.indexOf(chars[i++]));
        }
        return outputStream.toByteArray();
    }

    public static void main(String[] args) {
        CharMapCN charMap = new CharMapCN();
        List<TextBuilder> textBuilders = fromTexts("你的名字是[E8]，想知道我的名字吗？[F70001][EB0001]".toCharArray(), charMap);
        System.out.println();
    }

    public static boolean isHexChar(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f');
    }
}
