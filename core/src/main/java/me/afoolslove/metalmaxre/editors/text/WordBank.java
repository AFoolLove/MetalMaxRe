package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;

/**
 * 字库
 * 文字（char）对应游戏中的文字（byte）
 *
 * @author AFoolLove
 */
public class WordBank {
    /**
     * 文字对应多个字节
     */
    public static final Map<Character, byte[]> FONTS = new HashMap<>();
    /**
     * 文字对应单个字节
     */
    public static final Map<Character, Byte> FONTS_SINGLE = new HashMap<>();

    /**
     * 重复多字节
     *
     * @see WordBank#FONTS
     */
    public static final Map<Character, byte[]> FONTS_REPEATED = new HashMap<>();
    /**
     * 重复单字节
     *
     * @see WordBank#FONTS_SINGLE
     */
    public static final Map<Character, Byte> FONTS_SINGLE_REPEATED = new HashMap<>();


    public static final List<Map.Entry<Character, ?>> ALL_FONTS = new ArrayList<>();

    public static final String HEX_DIGITS = "0123456789ABCDEF";


    /**
     * 对话中可以执行的操作
     * 不可更改！
     */
    public static final Map<Byte, Integer> OPCODES = new HashMap<>();

    static {

        // 添加 0-9 A-Z
        for (byte i = 0; i <= 9; i++) {
            FONTS_SINGLE.put((char) ('0' + i), i);
        }
        for (char i = 'A', j = 0x0A; i <= 'Z'; i++) {
            FONTS_SINGLE.put(i, (byte) (j + (i - 'A')));
        }
        FONTS_SINGLE.put('-', (byte) (0x62));
        FONTS_SINGLE.put('▼', (byte) (0x63));  // 下标
        FONTS_SINGLE.put('。', (byte) (0x64));
        FONTS_SINGLE.put('.', (byte) (0x65));
        FONTS_SINGLE.put('*', (byte) (0x8E));  // 类似于 *
        FONTS_SINGLE.put('…', (byte) (0x8F));  // 两个 ..
        FONTS_SINGLE.put('^', (byte) (0x90));  // 对话时名称与文字中间的符号
        FONTS_SINGLE.put('!', (byte) (0x91));
        FONTS_SINGLE.put('?', (byte) (0x92));
        FONTS_SINGLE.put(':', (byte) (0x93));
//        FONTS_SINGLE.put('弹', (byte) (0x94));
//        FONTS_SINGLE.put('货', (byte) (0x95));
        FONTS_SINGLE.put('#', (byte) (0x96));
//        FONTS_SINGLE.put('无', (byte) (0x97));
//        FONTS_SINGLE.put('攻', (byte) (0x98));
        FONTS_SINGLE.put('/', (byte) (0x99));
//        FONTS_SINGLE.put('击', (byte) (0x9A));
//        FONTS_SINGLE.put('防', (byte) (0x9B));
//        FONTS_SINGLE.put('t', (byte) (0x9C));
//        FONTS_SINGLE.put('@', (byte) (0x9D));
//        FONTS_SINGLE.put('', (byte) (0x9E)); // 可自定义图块
        FONTS_SINGLE.put(' ', (byte) (0xFF)); // 空格，占一个英文字符，全角空格占两个英文字符'　'
        FONTS_SINGLE.put('\t', (byte) (0xE5)); // 换行
        FONTS_SINGLE.put('\r', (byte) (0xFE)); // 带名称换行
        FONTS_SINGLE.put('\n', (byte) (0x9F)); // 文本结束
        FONTS_SINGLE.put('\b', (byte) (0x42)); // 忽略字符


        // 使用小写英文字母对应另外一组相同字符
        // 数据不一样，但显示一样
        FONTS_SINGLE_REPEATED.put('a', (byte) 0x0A);
        FONTS_SINGLE_REPEATED.put('b', (byte) 0x0B);
        FONTS_SINGLE_REPEATED.put('c', (byte) 0x0C);
        FONTS_SINGLE_REPEATED.put('d', (byte) 0x0D);
        FONTS_SINGLE_REPEATED.put('e', (byte) 0x0E);
        FONTS_SINGLE_REPEATED.put('f', (byte) 0x0F);
        FONTS_SINGLE_REPEATED.put('g', (byte) 0x10);
        FONTS_SINGLE_REPEATED.put('h', (byte) 0x11);
        FONTS_SINGLE_REPEATED.put('i', (byte) 0x12);
        FONTS_SINGLE_REPEATED.put('j', (byte) 0x13);
        FONTS_SINGLE_REPEATED.put('k', (byte) 0x14);
        FONTS_SINGLE_REPEATED.put('l', (byte) 0x15);
        FONTS_SINGLE_REPEATED.put('m', (byte) 0x80);
        FONTS_SINGLE_REPEATED.put('n', (byte) 0x81);
        FONTS_SINGLE_REPEATED.put('o', (byte) 0x82);
        FONTS_SINGLE_REPEATED.put('p', (byte) 0x83);
        FONTS_SINGLE_REPEATED.put('q', (byte) 0x84);
        FONTS_SINGLE_REPEATED.put('r', (byte) 0x85);
        FONTS_SINGLE_REPEATED.put('s', (byte) 0x86);
        FONTS_SINGLE_REPEATED.put('t', (byte) 0x87);
        FONTS_SINGLE_REPEATED.put('u', (byte) 0x88);
        FONTS_SINGLE_REPEATED.put('v', (byte) 0x89);
        FONTS_SINGLE_REPEATED.put('w', (byte) 0x8A);
        FONTS_SINGLE_REPEATED.put('x', (byte) 0x8B);
        FONTS_SINGLE_REPEATED.put('y', (byte) 0x8C);
        FONTS_SINGLE_REPEATED.put('z', (byte) 0x8D);


        // 对话时按键确认后带名称换行
        OPCODES.put((byte) 0x33, 1);  // 不显示精灵和上半部分，第二字节未知
        OPCODES.put((byte) 0xE2, 0);
        OPCODES.put((byte) 0xE3, 0);
        OPCODES.put((byte) 0xE4, 0);  // 等待确认后继续对话
        OPCODES.put((byte) 0xE5, 0);  // 换行
        OPCODES.put((byte) 0xE6, 0);  // 未知，没有实际占用文本？似乎通过判断读取文本
        OPCODES.put((byte) 0xE7, 0);
        OPCODES.put((byte) 0xE8, 0);  // 当前控制的角色名称
        OPCODES.put((byte) 0xE9, 0);
        OPCODES.put((byte) 0xEA, 1);  // 索引 11933-11A1F 的文本，第二字节为下标
        OPCODES.put((byte) 0xEB, 2);  // 进行选择
        OPCODES.put((byte) 0xEC, 1);  // 引用 0xBE90 文本集
        OPCODES.put((byte) 0xED, 1);  // 空白占位，第一字节为数量
        OPCODES.put((byte) 0xEE, 1);  // 变更对话速度，不会自行恢复速度
        OPCODES.put((byte) 0xEF, 2);  // 横向重复一个字符，第一个为重复数量，第二个被重复的字符
        OPCODES.put((byte) 0xF0, 1);  // 引用 0x12010 文本集，名称，引用前会自动加上 [E4][E5]
        OPCODES.put((byte) 0xF1, 1);  // 对话 sleep
        OPCODES.put((byte) 0xF2, 1);  // 引用 0x10010 文本集
        OPCODES.put((byte) 0xF3, 1);  // 引用 0x1F99A 文本集
        OPCODES.put((byte) 0xF4, 1);  // 重复文本，直到遇见 0XFE ！！？
        OPCODES.put((byte) 0xF5, 1);  // 与NPC对话时，对NPC移动操作，未与NPC对话引用会死机(等待NPC移动完成，但没有目标NPC)
        OPCODES.put((byte) 0xF6, 0);  // 后面的字符将直接显示，不进行转换为中文等
        OPCODES.put((byte) 0xF7, 2);  // 跨文本集引用文本段，第一字节为第几段文本，第二字节为哪一页的文本集
        OPCODES.put((byte) 0xF8, 2);  // 读取1-3个字节，变更为文本，向右对齐
        OPCODES.put((byte) 0xF9, 2);  // 读取1-3个字节，变更为文本，向左对齐
        OPCODES.put((byte) 0xFA, 1);  // 动态的文本段
        OPCODES.put((byte) 0xFB, 1);  // 引用文本，动态文本？未知
        OPCODES.put((byte) 0xFC, 1);  //
        OPCODES.put((byte) 0xFD, 1);  // 玩家名称
        OPCODES.put((byte) 0xFE, 0);
        // E3 + 1byte = 进行选择，选择 是 空操作，选择 否 读取第一个字节文本?
        // EB + 2byte = 进行选择，选择 是 读取第一个字节的文本索引，选择 否 读取第二个字节的文本索引，用在当前文本结尾处
        // ED + 1byte = 空白占位，第一个字节为需要用空白占位的字符数量
        // EE + 1byte = 对话速度，第一个字节为接下来的对话显示速度 ！！！永久有效！！！
        // F0 + 1byte = 对话时，第一个字节为选择另一个人换行进行说话
        // F1 + 1byte = 对话sleep一段时间，第一个字节为sleep时间，时间单位未知，多数为 0x3C
        // F3 + 1byte = 战斗时为当前进行操作的 怪物/玩家 名称
        // F8 + 2byte = 读取0x33CBA的两个字节目标内存地址，第一个为索引，第二个为显示几个字节，最大为2，3个字节
        // FB + 1byte = ?
        // F5 + 0C = NPC对话时朝上，对话结束后恢复，一定要NPC！！！
        // F5 + 0D = NPC对话时朝下，对话结束后恢复，一定要NPC！！！
        // F5 + 0E = NPC对话时朝左，对话结束后恢复，一定要NPC！！！
        // F5 + 0F = NPC对话时朝右，对话结束后恢复，一定要NPC！！！
        // F5 + 12 = NPC对话时向上走一步
        // F5 + 13 = NPC对话时向下走一步
        // F5 + 14 = NPC对话时向左走一步
        // F5 + 15 = NPC对话时向右走一步
        // F5 + 16 = NPC对话时向右走一步
        // F5 + 17 = NPC对话时向玩家走一步
        // FC + 07 = 强度中，玩家的名称
        // FC + 3F = 玩家存档 1 名称
        // FC + 40 = 玩家存档 2 名称
        // FE      = 对话时按键确认后带名称换行

        // 读取字库
        InputStream resourceAsStream = ResourceManager.getAsStream("/fonts.txt");
        if (resourceAsStream != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
                bufferedReader.readLine(); // 忽略第一行

                byte row; // 行数，代表文字的第二个byte
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    // 读取第二个byte
                    row = (byte) Integer.parseInt(line.substring(0, 2), 16);

                    // 1行最多12个文字
                    for (int i = 0; i < 12; i++) {
                        // 3byte前面的字节，之后每隔2byte就是一个文字
                        int column = 3 + (i * 2);
                        if (line.length() <= column) {
                            // 后面没有字符可以读取了，小于12个文字
                            break;
                        }

                        // 数据值起始为0x24
                        byte[] value = {(byte) (0x24 + i), row};

                        // 添加文字
                        char key = line.charAt(column);
                        if (key != ' ') {
                            // 空格就算了吧
                            if (FONTS.containsKey(key)) {
                                // 如果已经添加过，添加进重复Map中
                                FONTS_REPEATED.put(key, value);
                            } else {
                                // 直接添加
                                FONTS.putIfAbsent(key, value);
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 不会真的会读取失败吧？
            System.out.println("读取字库失败！");
        }


        ALL_FONTS.addAll(FONTS_SINGLE.entrySet());
        ALL_FONTS.addAll(FONTS_SINGLE_REPEATED.entrySet());
        ALL_FONTS.addAll(FONTS.entrySet());
        ALL_FONTS.addAll(FONTS_REPEATED.entrySet());
    }

    private WordBank() {
    }

    public static byte[] toBytes(@Nullable String text) {
        if (text == null || text.isEmpty()) {
            // 空字符直接返回
            return new byte[0];
        }
        return toBytes(text.toCharArray());  // 转换为字符集
    }

    /**
     * @return 字符串转换为游戏中的文本
     */
    public static byte[] toBytes(@Nullable char[] chars) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 通过字符获取游戏文本字节
        charsLoop:
        for (int i = 0; i < chars.length; ) {
            char ch = chars[i];

            // 被 [] 所包围的文本属于十六进制 1byte，使用十六进制解析
            if (ch == '[') {
                // 梦的开始.jpg
                while (true) {
                    // 获取源字节
                    ch = chars[++i];

                    if (ch == ' ') {
                        // 为空就获取下一个字符
                        continue;
                    }

                    if (ch == ']') {
                        // 源字节读取结束
                        i++;
                        continue charsLoop;
                    }

                    if (++i >= chars.length) {
                        // 所有字节读取完毕
                        // 写入单个源字节
                        outputStream.write(Integer.parseInt(String.valueOf(ch), 16));
                        return outputStream.toByteArray();
                    }

                    char next = chars[i];
                    if (next == ' ') {
                        // 写入单个源字节
                        outputStream.write(Integer.parseInt(String.valueOf(ch), 16));
                        continue;
                    }

                    if (next == ']') {
                        // 读取到结束符
                        i++;

                        // ']' : FF F] <-this
                        // 写入单个源字节
                        outputStream.write(Integer.parseInt(String.valueOf(ch), 16));
                        continue charsLoop;
                    } else {
                        // 写入两个字符组成的十六进制
                        if (ch >= 'a' && ch <= 'f') {
                            ch -= 32;
                        }
                        if (next >= 'a' && next <= 'f') {
                            next -= 32;
                        }
                        outputStream.write((HEX_DIGITS.indexOf(ch) * 0x10) + HEX_DIGITS.indexOf(next));
                    }
                }
            }

            // 从重复单byte中获取
            Object temp = FONTS_SINGLE_REPEATED.get(ch);
            if (temp == null) {
                // 从单byte中获取
                temp = FONTS_SINGLE.get(ch);
            }
            if (temp == null) {
                // 从多byte中获取
                temp = FONTS.get(ch);
            }
            if (temp == null) {
                // 从重复多byte中获取
                temp = FONTS_REPEATED.get(ch);
            }
            if (temp != null) {
                if (temp instanceof byte[] bytes) {
                    // 写入多字节
                    outputStream.write(bytes, 0, bytes.length);
                } else {
                    // 写入单字节
                    outputStream.write((byte) temp);
                }
            } else {
                // 没有这个字符
                System.out.println("未知字符：" + ch);
            }
            i++;
        }
        return outputStream.toByteArray();
    }

    /**
     * @return 将字节数组通过词库转换为能读懂的文本
     */
    public static String toString(byte[] bytes) {
        return toString(bytes, 0, bytes.length);
    }


    /**
     * @return 将字节数组通过词库转换为能读懂的文本
     */
    public static String toString(byte[] bytes, int offset, int length) {
        return toString(ALL_FONTS, bytes, offset, length);
    }

    /**
     * @return 将字节数组通过词库转换为能读懂的文本
     */
    public static String toString(List<Map.Entry<Character, ?>> allFonts, byte[] bytes, int offset, int length) {
        StringBuilder text = new StringBuilder();
        byte[] copy = Arrays.copyOfRange(bytes, offset, offset + length);

        // OPCODES 单独配置，不属于 allFonts

        maps:
        for (int i = 0; i < length; i++) {
            byte[] copyOfRange = Arrays.copyOfRange(copy, i, Math.min(i + 2, copy.length));

            // 在字库中查找对应的字符文本
            for (Map.Entry<Character, ?> entry : allFonts) {
                if (entry.getValue() instanceof byte[]) {
                    if (Arrays.equals((byte[]) entry.getValue(), copyOfRange)) {
                        // 多byte对应的字符
                        text.append(entry.getKey());
                        i++;
                        continue maps;
                    }
                } else {
                    if (Objects.equals(entry.getValue(), copy[i])) {
                        // 单byte对应的字符
                        text.append(entry.getKey());
                        continue maps;
                    }
                }
            }

            Integer opcodeValue = OPCODES.get(copy[i]);
            // 如果上一个是源字符结尾 ']' 就合并
            if (text.length() > 0 && text.charAt(text.length() - 1) == ']') {
                // 合并
                text.setCharAt(text.length() - 1, ' ');
            } else {
                // 无法合并
                text.append('[');
            }
            text.append(String.format("%02X", copy[i]));
            if (opcodeValue != null) {
                switch (copy[i] & 0xFF) {
                    case 0xF6: // 读取到 0x9F 或 0x63 后结束
                        // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                        // 0x63             (结束)
                        // 0x8C + 2byte     (填充数量，填充字符)
                        // 0x43 ???
                        whileF6:
                        while (true) {
                            if (++i >= copy.length) {
                                // 读取数据完毕
                                text.append(']');
                                return text.toString();
                            }

                            switch (copy[i] & 0xFF) {
                                case 0x9E: // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                                    // 写入 9E
                                    text.append(" 9E");
                                    // 空格填充
                                    if (++i >= copy.length) {
                                        // 读取完毕
                                        // 没有数量，直接结束
                                        text.append(']');
                                        return text.toString();
                                    }
                                    // 填充数量
                                    text.append(String.format("%02X ", copy[i]));
                                    break;
                                case 0x63: // 0x63             (结束)
                                    // 0xF6 的结束符
                                    text.append(" 63]");
                                    continue maps;
                                case 0x8C: // 0x8C + 2byte     (填充数量，填充字符)
                                    text.append(" 8C");
                                    // 使用指定字符填充指定数量
                                    if (++i >= copy.length) {
                                        // 读取完毕
                                        // 没有字符，也没有数量，直接结束
                                        text.append(']');
                                        return text.toString();
                                    }
                                    // 填充数量
                                    text.append(String.format("%02X", copy[i]));
                                    if (++i >= copy.length) {
                                        // 读取完毕
                                        // 没有字符，直接结束
                                        text.append(']');
                                        return text.toString();
                                    }
                                    // 填充字符
                                    text.append(String.format("%02X ", copy[i]));
                                    break;
                                case 0x9F:
                                    // 文本段结束，另一个的开始
                                    text.append("]\n");
                                    // emm？要不要结束？
                                    break whileF6;
                                default:
                                    // 写入不认识的字节
                                    text.append(String.format("%02X", copy[i]));
                                    break;
                            }
                        }
                        break;
                    default:
                        // 其余的直接读取相应的字节数量
                        int len = Math.min(copy.length - i - 1, opcodeValue);
                        for (int j = 0; j < len; j++) {
                            text.append(String.format("%02X", copy[i + j + 1]));
                        }
                        i += len;
                        // 读取结束
                        text.append(']');
                        break;
                }
            } else {
                // 能到这里的只有纯字节
                text.append(']');
            }
        }
        return text.toString();
    }
}
