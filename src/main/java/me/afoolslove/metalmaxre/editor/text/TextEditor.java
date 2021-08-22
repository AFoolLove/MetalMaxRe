package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.Item;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.monster.MonsterEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public static final int X_17680 = 0x17680;
    public static final int X_21AF6 = 0x21AF6;

    public static final Map<Integer, Integer> POINTS = new HashMap<>();

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
        POINTS.put(X_17680, 0x1800F); // 对话
        POINTS.put(X_21AF6, 0x21E80); // 怪物名称
    }

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前初始化数据
        paragraphsMap.clear();

        for (Map.Entry<Integer, Integer> point : POINTS.entrySet()) {
            // 得到这段文本的数据长度
            byte[] bytes = new byte[point.getValue() - point.getKey() + 1];
            // 定位，读取
            setPosition(point.getKey());
            get(buffer, bytes);
            // 解析并通过换行分段
            String[] split = WordBank.toString(bytes).split("\n", -1);
            // 添加
            TextParagraphs textParagraphs = new TextParagraphs();
            textParagraphs.addAll(Arrays.asList(split));
            paragraphsMap.put(point.getKey(), textParagraphs);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        for (Map.Entry<Integer, TextParagraphs> entry : paragraphsMap.entrySet()) {
            int start = entry.getKey();
            int end = POINTS.get(entry.getKey());

            // 获取长度
            int length = end - start + 1;

            setPosition(start);
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
                // 多余的空间使用 0x9F 填充
                byte[] bytes = new byte[length];
                Arrays.fill(bytes, (byte) 0x9F);
                put(buffer, bytes);
            }
        }
        return true;
    }


    public Map<Integer, TextParagraphs> getParagraphsMap() {
        return paragraphsMap;
    }

    /**
     * @return 装备、道具的名称
     */
    public String getItemName(@Range(from = 0x00, to = 0xFF) int item) {
        if (item >= Item.ITEMS_MAX_COUNT) {
            return null;
        }
        return paragraphsMap.get(X_11A20).get(item);
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
        return paragraphsMap.get(X_21AF6).get(monsterId);
    }

    /**
     * @return 获取怪物名称
     */
    public String getMonsterName(byte monsterId) {
        return getMonsterName(monsterId & 0xFF);
    }
}
