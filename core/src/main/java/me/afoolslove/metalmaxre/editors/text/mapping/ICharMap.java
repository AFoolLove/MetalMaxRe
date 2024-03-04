package me.afoolslove.metalmaxre.editors.text.mapping;

import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * 字符映射
 */
public interface ICharMap {
    String HEX_DIGITS = "0123456789ABCDEF";

    /**
     * 获取所有文本中的操作码，对应的是操作码的参数长度
     *
     * @return 文本中的操作码，对应的是操作码的参数长度
     */
    Map<Byte, Integer> getOpcodes();

    /**
     * 获取所有字符映射
     *
     * @return 所有字符映射
     */
    List<SingleMapEntry<Character, Object>> getValues();

    /**
     * 通过字符获取文字字节数组
     *
     * @param ch 字符
     * @return 文字字节数组，数组值可能是{@code byte[]}、{@code byte}
     */
    @NotNull
    default Object[] getValues(char ch) {
        List<Object> values = new ArrayList<>();
        for (SingleMapEntry<Character, Object> value : getValues()) {
            if (value.getKey() == ch) {
                values.add(value.getValue());
            }
        }
        return values.toArray();
    }

    /**
     * 通过字符获取文字字节，只获取一个
     *
     * @param ch 字符
     * @return 文字字节，数组值可能是{@code byte[]}、{@code byte}以及{@code null}
     */
    @Nullable
    default Object getValue(char ch) {
        for (SingleMapEntry<Character, Object> value : getValues()) {
            if (value.getKey() == ch) {
                return value.getValue();
            }
        }
        return null;
    }

    /**
     * 判断是否存在该字符
     *
     * @param ch 字符
     * @return 是否存在该字符
     */
    default boolean hasValue(char ch) {
        Object[] values = getValues(ch);
        return values != null && values.length > 0;
    }

    default byte[] toBytes(@Nullable String text, @Nullable ICharMapListener listener) {
        return toBytes(this, text, listener);
    }

    default byte[] toBytes(char[] chars, @Nullable ICharMapListener listener) {
        return toBytes(this, chars, listener);
    }

    static byte[] toBytes(@NotNull ICharMap charMap, @Nullable String text, @Nullable ICharMapListener listener) {
        if (text == null || text.isEmpty()) {
            // 空字符直接返回
            return new byte[0];
        }
        return toBytes(charMap, text.toCharArray(), listener);  // 转换为字符集
    }

    /**
     * 将字符数组转换为字节数组
     *
     * @param chars    字符数组
     * @param listener 未知字符会通知该接口
     * @return 字符数组转换后的字节数组
     */
    static byte[] toBytes(@NotNull ICharMap charMap, char[] chars, @Nullable ICharMapListener listener) {
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

            // 获取字符映射
            Object[] values = charMap.getValues(ch);
            if (values != null && values.length > 0) {
                // 找到映射
                Object tmp = values[0];
                if (tmp instanceof byte[] bytes) {
                    // 写入多字节
                    outputStream.write(bytes, 0, bytes.length);
                } else {
                    // 写入单字节
                    outputStream.write((byte) tmp);
                }
            } else {
                // 未找到映射
                if (listener != null) {
                    // 通知监听器
                    byte[] bytes = listener.onUnknownChar(ch);
                    if (bytes != null) {
                        outputStream.write(bytes, 0, bytes.length);
                    }
                }
            }
            i++;
        }
        return outputStream.toByteArray();
    }


    default String toString(byte[] bytes) {
        return toString(this, bytes, 0, bytes.length);
    }

    default String toString(byte[] bytes, int offset, int length) {
        return toString(this, bytes, offset, length);
    }


    static String toString(@NotNull ICharMap charMap, byte[] bytes) {
        return toString(charMap, bytes, 0, bytes.length);
    }

    /**
     * @return 将字节数组通过词库转换为能读懂的文本
     */
    static String toString(@NotNull ICharMap charMap, byte[] bytes, int offset, int length) {
        StringBuilder text = new StringBuilder();
        byte[] copy = Arrays.copyOfRange(bytes, offset, offset + length);

        // OPCODES 单独配置，不属于 charMap

        maps:
        for (int i = 0; i < length; i++) {
            byte[] copyOfRange = Arrays.copyOfRange(copy, i, Math.min(i + 2, copy.length));

            // 在字库中查找对应的字符文本
            for (Map.Entry<Character, ?> entry : charMap.getValues()) {
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

            Integer opcodeValue = charMap.getOpcodes().get(copy[i]);
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
                if ((copy[i] & 0xFF) == 0xF6) { // 读取到 0x9F 或 0x63 后结束
                    // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                    // 0x63             (结束)
                    // 0x8C + 2byte     (填充数量，填充字符)
                    // 0x43 + 1byte     (引用09页的文本)
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
                            case 0x43:
                                text.append(" 43");
                                // 引用09页的文本
                                if (++i >= copy.length) {
                                    // 读取完毕
                                    // 没有字符，直接结束
                                    text.append(']');
                                    return text.toString();
                                }
                                // 引用的字符
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
                } else {// 其余的直接读取相应的字节数量
                    int len = Math.min(copy.length - i - 1, opcodeValue);
                    for (int j = 0; j < len; j++) {
                        text.append(String.format("%02X", copy[i + j + 1]));
                    }
                    i += len;
                    // 读取结束
                    text.append(']');
                }
            } else {
                // 能到这里的只有纯字节
                text.append(']');
            }
        }
        return text.toString();
    }

    /**
     * 生成默认的操作码映射
     */
    default Map<Byte, Integer> createDefaultOpcodes() {
        Map<Byte, Integer> OPCODES = new LinkedHashMap<>();
        OPCODES.put((byte) 0x2F, 0);  // 在全屏界面时，最上方的边框正中间替换一个 0xFA（$6030），通常表现为焊点。未完全解析
        OPCODES.put((byte) 0x30, 0);  // 在对话界面时（包括全屏界面），对话框的上边框普通焊点变更为有层次感的焊点0xFD（$6255），缺陷为下一个字符会重复两次，如302400，文本会显示为“架0”而不是正确的“眼”，所以通常后面连接FF使用
        OPCODES.put((byte) 0x31, 0);  // 在对话界面时（包括全屏界面），对话框的下边框普通焊点变更为有层次感的焊点0xF8（$6395），缺陷为下一个字符会消失，如312400，文本会显示为“0”而不是正确的“眼”，所以通常后面连接FF使用
        OPCODES.put((byte) 0x32, 0);  // 在对话界面时（包括全屏界面），对话框两边中间分割的下方普通焊点变更为有层次感的焊点0xF7（$638B），缺陷为下一个字符会消失，如322400，文本会显示为“0”而不是正确的“眼”，所以通常后面连接FF使用
        OPCODES.put((byte) 0x33, 0);  // 对$2001写入0x0E，不显示精灵，缺陷为下一个字符会消失，如332400，文本会显示为“0”而不是正确的“眼”，所以通常后面连接00使用
        OPCODES.put((byte) 0x34, 0);  // $59D = 0x03
        OPCODES.put((byte) 0x42, 0);  // 忽略字符
        OPCODES.put((byte) 0xE2, 0);  // ($DF,$E0,$E1) 三字节的数字
        OPCODES.put((byte) 0xE3, 0);  // 进行选择，但操作结果由系统程序接管（不是剧情指令接管）
        OPCODES.put((byte) 0xE4, 0);  // 等待确认后继续对话
        OPCODES.put((byte) 0xE5, 0);  // 换行
        OPCODES.put((byte) 0xE6, 0);  // 读取$0370作为玩家id，获取玩家的名称到$051D
        OPCODES.put((byte) 0xE7, 0);  // 换行，但($C4,$C5)的值+#$40，通常在战斗状态文本中使用
        OPCODES.put((byte) 0xE8, 0);  // 读取$051D的文本。通常先执行E6后再执行E8作为当前控制的角色名称
        OPCODES.put((byte) 0xE9, 0);  // $E2 物品的名称
        OPCODES.put((byte) 0xEA, 1);  // 索引 13=0x11933-0x11A1F 的文本，第二字节为下标
        OPCODES.put((byte) 0xEB, 2);  // 进行选择
        OPCODES.put((byte) 0xEC, 1);  // 引用 14=0x0BE90-0x0C00F 文本集
        OPCODES.put((byte) 0xED, 1);  // 空白占位，第一字节为数量
        OPCODES.put((byte) 0xEE, 1);  // 变更全局对话速度，不会自行恢复速度，默认为0
        OPCODES.put((byte) 0xEF, 2);  // 横向重复一个字符，第一个为重复数量，第二个被重复的字符
        OPCODES.put((byte) 0xF0, 1);  // 引用 0C=0x12010-0x120DF 文本集，名称，引用前会自动加上 [E4][E5]
        OPCODES.put((byte) 0xF1, 1);  // 对话 sleep
        OPCODES.put((byte) 0xF2, 1);  // 引用 09=0x10010-0x10128 文本集
        OPCODES.put((byte) 0xF3, 1);  // 引用 11=0x1F99A-0x2000F 文本集
        OPCODES.put((byte) 0xF4, 1);  // 重复文本，参数为重复数量，遇见0XFE结束，全屏文本时使用，否则文字刷屏死机
        OPCODES.put((byte) 0xF5, 1);  // 与NPC对话时，对NPC移动操作，未与NPC对话引用会死机(等待NPC移动完成，但没有目标NPC)
        OPCODES.put((byte) 0xF6, 0);  // 后面的字符将直接显示，不进行转换为中文等
        OPCODES.put((byte) 0xF7, 2);  // 跨文本集引用文本段，第一字节为第几段文本，第二字节为哪一页的文本集
        OPCODES.put((byte) 0xF8, 2);  // 读取1-3个字节，变更为文本，向右对齐
        OPCODES.put((byte) 0xF9, 2);  // 读取1-3个字节，变更为文本，向左对齐
        OPCODES.put((byte) 0xFA, 1);  // 引用目标地址的文本
        OPCODES.put((byte) 0xFB, 1);  // 引用目标地址的文本，目标地址的第一位作为$CC文本段，目标地址的第二位作为$CD文本页
        OPCODES.put((byte) 0xFC, 1);  // 引用数据
        OPCODES.put((byte) 0xFD, 1);  // 玩家名称
        OPCODES.put((byte) 0xFE, 0);  // 对话时按键确认后带名称换行
        // E3 + 1byte = 进行选择，由系统控制（非剧情脚本）
        // EB + 2byte = 进行选择，选择 是 读取第一个字节的文本索引，选择 否 读取第二个字节的文本索引，用在当前文本结尾处
        // ED + 1byte = 空白占位，第一个字节为需要用空白占位的字符数量
        // EE + 1byte = 对话速度，第一个字节为接下来的对话显示速度 ！！！永久有效！！！
        // F0 + 1byte = 对话时，第一个字节为选择另一个人换行进行说话
        // F1 + 1byte = 对话sleep一段时间，第一个字节为sleep时间，时间单位未知，多数为 0x3C
        // F3 + 1byte = 战斗时为当前进行操作的 怪物/玩家 名称
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
        // F8 + 2byte = 读取0x33CBA的两个字节目标内存地址，第一个为索引，第二个为显示几个字节，最大为2(3个字节)
        // F9 + 2byte = 读取0x33CBA的两个字节目标内存地址，第一个为索引，第二个为显示几个字节，最大为2(3个字节)
        // FA + 1byte = 读取0x33CBA的两个字节目标内存地址，目标地址的第一位作为$CC文本段，目标地址的第二位作为$CD文本页
        // FB + 1byte = 读取0x33CBA的两个字节目标内存地址，第一个为索引，第二个为显示几个字节，最大为2(3个字节)
        // FC + 1byte = 读取0x33CBA的两个字节目标内存地址
        // FC + 07 = 强度中，玩家的名称
        // FC + 3F = 玩家存档 1 名称
        // FC + 40 = 玩家存档 2 名称
        return OPCODES;
    }

    @FunctionalInterface
    interface ICharMapListener {
        /**
         * 出现了未知字符
         *
         * @param ch 未知字符
         * @return 可以自定义添加字节替换未知的字符
         */
        byte[] onUnknownChar(char ch);
    }
}
