package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.editor.EditorManager;

import java.io.ByteArrayOutputStream;
import java.nio.CharBuffer;
import java.util.*;

/**
 * 文本段，使用 0x9F 结尾
 *
 * @author AFoolLove
 */
public class TextParagraphs extends ArrayList<String> {
    /**
     * @return 字符串转换为游戏文本
     * @see WordBank#toBytes(String)
     */
    public byte[] toBytes(int index) {
        return WordBank.toBytes(get(index));
    }

    /**
     * ！！！未完成！！！
     *
     * @return 将文本段中的文本变量替换为文本，该文本不可以使用 {@link WordBank#toBytes(String)}
     */
    public String getFormatted(int index) {
        String str = get(index);
        if (str == null || str.isEmpty()) {
            // 空的就算了吧
            return str;
        }

        StringBuilder text = new StringBuilder();

        CharBuffer buffer = CharBuffer.wrap(str.toCharArray());

        TextEditor textEditor = EditorManager.getEditor(TextEditor.class);

        while (buffer.hasRemaining()) {
            char ch = buffer.get();

            if (ch != '[') {
                text.append(ch);
                continue;
            }

            // 提取源字节为byte[]
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while (buffer.hasRemaining()) {
                ch = buffer.get();
                if (ch == ' ') {
                    // 跳过空字符
                    continue;
                }
                if (ch == ']') {
                    // 源字节结束
                    break;
                }
                byte b = (byte) ("0123456789ABCDEF".indexOf(ch) & 0x0F);

                if (!buffer.hasRemaining()) {
                    // 读取完毕
                    outputStream.write(b);
                    break;
                }
                // 读取低4位bit

                ch = buffer.get();
                if (ch == ']') {
                    // 源字节结束
                    outputStream.write(b);
                    break;
                }
                if (ch == ' ') {
                    // 下一个字节
                    outputStream.write(b);
                    continue;
                }
                // 设置为高位
                b <<= 4;
                // 添加低位
                b |= ("0123456789ABCDEF".indexOf(ch) & 0x0F);
                // 添加byte
                outputStream.write(b);
            }

            // 解析源字节
            if (outputStream.size() == 0) {
                // 没有源字节
                continue;
            }
            byte[] bytes = outputStream.toByteArray();

            // 写入源字节格式： [byte]{string}
            text.append('[');
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] == (byte) 0xF7) {
                    // F7 + 1 + 1       文本集索引，第一字节为下标，第二字节
                    if (i + 2 >= bytes.length) {
                        // 剩余字节不足以作为变量
                        // 写入并结束当前源字节
                        text.append(String.format("%02X", bytes[i]));
                        if (i + 1 < bytes.length) {
                            text.append(String.format("%02X", bytes[i + 1]));
                        }
                        if (i + 2 < bytes.length) {
                            text.append(String.format("%02X", bytes[i + 2]));
                        }
                        text.append(']');
                        break;
                    }

                    // 读取下标
                    int inx = bytes[i++];
                    // 读取文本区域
                    int pointStart = bytes[i];
                    int point = 0;
                    // 判断是否为已知的文本段
                    switch (pointStart) {
                        case 0x14:
                            point = 0x0BE90;
                            break;
                        case 0x09:
                            point = 0x10010;
                            break;
                        case 0x03:
                            point = 0x10DB3;
                            break;
                        case 0x0C:
                            point = 0x12010;
                            break;
                        case 0x0D:
                            point = 0x120E0;
                            break;
                        case 0x12:
                            point = 0x1157C;
                            break;
                        case 0x13:
                            point = 0x11933;
                            break;
                        default:
                            break;
                    }

                    if (point != 0) {
                        // 从已知的文本中直接获取
                        for (Map.Entry<Map.Entry<Integer, Integer>, TextParagraphs> entry : textEditor.getParagraphsMap().entrySet()) {
                            if (entry.getKey().getKey() == point) {
                                // 获取前再次格式化
                                text.append(String.format("[F7%02X%02X]{%s}", inx, pointStart, entry.getValue().getFormatted(inx)));
                                break;
                            }
                        }
                        // 下一个
                        continue;
                    }

                    switch (pointStart) {
                        case 0x00:
                            point = 0x11A20;
                            break;
                        case 0x01:
                            point = 0x21AF6;
                            break;
                        case 0x02:
                            point = 0x10129;
                            break;
                        case 0x04:
                            point = 0x112F2;
                            break;
                        case 0x05:
                            point = 0x14010;
                            break;
                        case 0x06:
                            point = 0x124EE;
                            break;
                        case 0x07:
                            point = 0x16010;
                            break;
                        case 0x08:
                            point = 0x21E80;
                            break;
                        case 0x0A:
                            point = 0x16BDD;
                            break;
                        case 0x0B:
                            point = 0x36010;
                            break;
                        case 0x0E:
                            point = 0x18010;
                            break;
                        case 0x0F:
                            point = 0x17680;
                            break;
                        case 0x10:
                            point = 0x13320;
                            break;
                        case 0x11:
                            point = 0x1F99A;
                            break;
                        case 0x15:
                            point = 0x384B5;
                            break;
                        default:
                            // 未知的点，不进行转换
                            break;
                    }
                }


            }

        }
        return text.toString();
    }
}
