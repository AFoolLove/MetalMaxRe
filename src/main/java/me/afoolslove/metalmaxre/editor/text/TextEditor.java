package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.Item;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.map.DogSystemEditor;
import me.afoolslove.metalmaxre.editor.monster.MonsterEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文本编辑器
 * <p>
 * 未找出所有文本的位置（可以直接将整个文件转换为String，但。。。
 * <p>
 * 2021年6月13日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class TextEditor extends AbstractEditor<TextEditor> {
    public static final int X_0BE90 = 0x0BE90;
    public static final int X_10010 = 0x10010;
    public static final int X_10DB3 = 0x10DB3;
    public static final int X_1157C = 0x1157C;
    public static final int X_11933 = 0x11933;
    public static final int X_11A20 = 0x11A20;
    public static final int X_11F75 = 0x11F75;
    public static final int X_12010 = 0x12010;
    public static final int X_120E0 = 0x120E0;
    public static final int X_14010 = 0x14010;
    public static final int X_17680 = 0x17680;
    public static final int X_1F99A = 0x1F99A;
    public static final int X_21AF6 = 0x21AF6;
    public static final int X_36010 = 0x36010;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 已知的文本段
     */
    public static final Map<Integer, Integer> POINTS = new LinkedHashMap<>();

    /**
     * 解析为文本的已知文本段
     */
    private final Map<Integer, TextParagraphs> paragraphsMap = new HashMap<>();

    static {
        // K: startPosition
        // V: endPosition
        POINTS.put(X_0BE90, 0x0C00F); // 0x00
        POINTS.put(X_10010, 0x10DB2); // 0x01
        POINTS.put(X_10DB3, 0x1157B); // 0x02
        POINTS.put(X_1157C, 0x11932); // 0x03
        POINTS.put(X_11933, 0x11A1F); // 0x04
        POINTS.put(X_11A20, 0x11F74); // 0x05
        POINTS.put(X_11F75, 0x1200F); // 0x06
        POINTS.put(X_12010, 0x120DF); // 0x07
        POINTS.put(X_120E0, 0x132FE); // 0x08
        POINTS.put(X_14010, 0x15A9F); // 对话，未完全验证对话数据
        POINTS.put(X_17680, 0x1800F); // 对话
        POINTS.put(X_1F99A, 0x20F83); // 对话，未完全验证对话数据
        POINTS.put(X_21AF6, 0x21E80); // 怪物名称
        POINTS.put(X_36010, 0x37CA5); // 对话，未完全验证对话数据
    }

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前初始化数据
        paragraphsMap.clear();
        POINTS.forEach((key, value) -> {
            try {
                EXECUTOR.submit(() -> {
                    TextParagraphs textParagraphs = new TextParagraphs();

                    // 得到这段文本的数据长度
                    byte[] bytes = new byte[value - key + 1];
                    // 定位，读取
                    getPrgRom(buffer, key - 0x10, bytes);

                    StringBuilder builder = new StringBuilder();

                    // 最大缓存4字节
                    byte[] temp = new byte[4];
                    // 文本起始
                    data:
                    for (int position = key; position <= value; ) {
                        final int tempLength = Math.min(temp.length, value - position + 1);
                        get(buffer, position, temp, 0, tempLength);

                        // 在字库中查找对应的字符文本
                        allFonts:
                        for (Map.Entry<Character, ?> entry : WordBank.ALL_FONTS) {
                            if (entry.getValue() instanceof byte[] bs) {
                                for (int i = 0; i < bs.length; i++) {
                                    if (temp[i] != bs[i]) {
                                        continue allFonts;
                                    }
                                }
                                builder.append(entry.getKey());
                                position += bs.length;
                                continue data;
                            } else {
                                if (Objects.equals(entry.getValue(), temp[0])) {
                                    if (temp[0] == (byte) 0x9F) {
                                        // 断句
                                        // 保存当前文本，并清空缓存文本
                                        textParagraphs.add(builder.toString());
                                        builder.setLength(0);
                                    } else {
                                        // 单byte对应的字符
                                        builder.append(entry.getKey());
                                    }
                                    position++;
                                    continue data;
                                }
                            }
                        }


                        builder.append(String.format("[%02X", temp[0]));
                        // 单字节的操作码
                        var opcode = WordBank.OPCODES.get(temp[0]);
                        if (opcode == null) {
                            builder.append(']');
                            position++;
                            continue data;
                        }

                        switch (temp[0] & 0xFF) {
                            case 0xF6:  // 读取到 0x9F 或 0x63 后结束
                                // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                                // 0x63             (结束)
                                // 0x8C + 2byte     (填充数量，填充字符)
                                // 0x43 ???
                                whileF6:
                                while (true) {
                                    if (++position >= value + 1) {
                                        // 读取数据完毕
                                        builder.append(']');
                                        break data;
                                    }

                                    switch (get(buffer, position) & 0xFF) {
                                        case 0x9E: // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                                            // 写入 9E
                                            builder.append(" 9E");
                                            // 空格填充
                                            if (++position >= value + 1) {
                                                // 读取完毕
                                                // 没有数量，直接结束
                                                builder.append(']');
                                                break data;
                                            }
                                            // 填充数量
                                            builder.append(String.format("%02X ", get(buffer, position)));
                                            break;
                                        case 0x63: // 0x63             (结束)
                                            // 0xF6 的结束符
                                            builder.append(" 63]");
                                            continue data;
                                        case 0x8C: // 0x8C + 2byte     (填充数量，填充字符)
                                            builder.append(" 8C");
                                            // 使用指定字符填充指定数量
                                            if (++position >= value + 1) {
                                                // 读取完毕
                                                // 没有字符，也没有数量，直接结束
                                                builder.append(']');
                                                break data;
                                            }
                                            // 填充数量
                                            builder.append(String.format("%02X", get(buffer, position)));
                                            if (++position >= value + 1) {
                                                // 读取完毕
                                                // 没有字符，直接结束
                                                builder.append(']');
                                                break data;
                                            }
                                            // 填充字符
                                            builder.append(String.format("%02X ", get(buffer, position)));
                                            break;
                                        case 0x9F:
                                            // 文本段结束，另一个的开始
                                            builder.append("]");

                                            // 断句
                                            // 保存当前文本，并清空缓存文本
                                            textParagraphs.add(builder.toString());
                                            builder.setLength(0);

                                            // emm？要不要结束？
                                            break whileF6;
                                        default:
                                            // 写入不认识的字节
                                            builder.append(String.format("%02X", get(buffer, position)));
                                            break;
                                    }
                                }
                                break;
                            default:
                                // 其余的直接读取相应的字节数量
                                int len = Math.min(tempLength, opcode);
                                for (int j = 0; j < len; j++) {
                                    builder.append(String.format("%02X", temp[j]));
                                }
                                // 至少 +1 opcode
                                position += Math.max(1, len);
                                // 读取结束
                                builder.append(']');
                                break;
                        }
                    }
                    paragraphsMap.put(key, textParagraphs);
                    return null;
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        for (Map.Entry<Integer, TextParagraphs> entry : paragraphsMap.entrySet()) {
            int start = entry.getKey();
            int end = POINTS.get(entry.getKey());

            // 获取长度
            int length = end - start + 1;

            setPrgRomPosition(start - 0x10);
            // 转换为字节写入

            for (int i = 0, size = entry.getValue().size(); i < size; i++) {
                String text = entry.getValue().get(i);

                if (length < 0) {
                    // 已经没有空间可以写入了
                    System.out.println("多余的文本段：" + text);
                    continue;
                }

                if (text != null && !text.isEmpty()) {
                    // 空字符不进行解析
                    byte[] bytes1 = WordBank.toBytes(text);
                    // 最大写入的字节数量
                    int len = Math.min(bytes1.length, length);
                    // 写入
                    put(buffer, bytes1, 0, len);
                    // 计算剩余可写入的空间
                    length -= len;
                }

                // 剩余可写入的空间至少为1写入 0x9F
                // 最后一个文本段不写入0x9F
                if (length > 0 && i != size - 1) {
                    // 文本段结束
                    put(buffer, 0x9F);
                    length--;
                }
            }

            // test
            if (length < 0) {
                System.out.print("");
            }

            if (length > 0) {
                // 注：多余的空间填充伴随着 BUG
                // [42] 概率高，会导致文本错误
                // [ED 00] 概率低，要求高，如果填充在 [F6] 之后，会以文本的方式显示出来

                skip(-1);
                for (int i = length / 2; i > 0; i--) {
                    put(buffer, 0xED);
                    put(buffer, 0x00);
                }
                length &= 0B0000_0001;
                if (length != 0) {
                    put(buffer, 0x42);
                }
                put(buffer, 0x9F);
            }
        }
        return true;
    }


    public Map<Integer, TextParagraphs> getParagraphsMap() {
        return paragraphsMap;
    }

    /**
     * 获取城镇的名称
     *
     * @param town 城镇
     * @return 城镇的名称
     */
    public String getTownName(@Range(from = 0x00, to = DogSystemEditor.TOWN_MAX_COUNT - 1) int town) {
        TextParagraphs textParagraphs = paragraphsMap.get(X_120E0);
        return textParagraphs.size() > (0x30 + town) ? textParagraphs.get(0x30 + town) : null;
    }

    /**
     * @return 装备、道具的名称
     */
    public String getItemName(@Range(from = 0x00, to = 0xFF) int item) {
        if (item >= Item.ITEMS_MAX_COUNT) {
            return null;
        }
        TextParagraphs textParagraphs = paragraphsMap.get(X_11A20);
        return textParagraphs.size() > item ? textParagraphs.get(item) : null;
    }

    /**
     * @return 装备、道具的名称
     */
    public String getItemName(byte item) {
        return getItemName(item & 0xFF);
    }

    /**
     * @return 获取怪物名称
     */
    public String getMonsterName(@Range(from = 0x00, to = 0xFF) int monsterId) {
        if (monsterId >= MonsterEditor.MONSTER_COUNT) {
            return null;
        }
        TextParagraphs textParagraphs = paragraphsMap.get(X_21AF6);
        return textParagraphs.size() > monsterId ? textParagraphs.get(monsterId) : null;
    }

    /**
     * 设置城镇的名称
     *
     * @param town 城镇
     * @param name 新的城镇名称
     */
    public void setTownName(@Range(from = 0x00, to = DogSystemEditor.TOWN_MAX_COUNT - 1) int town, @NotNull String name) {
        paragraphsMap.get(X_120E0).set(0x30 + town, name);
    }

    /**
     * @return 获取怪物名称
     */
    public String getMonsterName(byte monsterId) {
        return getMonsterName(monsterId & 0xFF);
    }


    /**
     * 设置物品的名称
     *
     * @param item 物品的ID
     * @param name 物品的新名称
     */
    public void setItemName(byte item, String name) {
        paragraphsMap.get(X_11A20).set(item, name);
    }

    /**
     * 设置物品的名称
     *
     * @param item 物品的ID
     * @param name 物品的新名称
     */
    public void setItemName(@Range(from = 0x00, to = 0xFF) int item, @NotNull String name) {
        if (item >= Item.ITEMS_MAX_COUNT) {
            return;
        }
        paragraphsMap.get(X_11A20).set(item, name);
    }

    /**
     * 设置怪物的名称
     *
     * @param monsterId 怪物的ID
     * @param name      怪物新名称
     */
    public void setMonsterName(@Range(from = 0x00, to = 0xFF) int monsterId, @NotNull String name) {
        if (monsterId >= MonsterEditor.MONSTER_COUNT) {
            return;
        }
        paragraphsMap.get(X_21AF6).set(monsterId, name);
    }


    /**
     * 设置怪物的名称
     *
     * @param monsterId 怪物的ID
     * @param name      怪物新名称
     */
    public void setMonsterName(byte monsterId, @NotNull String name) {
        paragraphsMap.get(X_21AF6).set(monsterId, name);
    }
}
