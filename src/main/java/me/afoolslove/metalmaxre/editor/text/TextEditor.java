package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.Item;
import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.*;

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
    public static final List<Map.Entry<Integer, Integer>> POINTS = new ArrayList<>();

    private final Map<Map.Entry<Integer, Integer>, TextParagraphs> paragraphsMap = new HashMap<>();

    static {
        POINTS.add(Map.entry(0x0BE90, 0x0C00F)); // 0x00
        POINTS.add(Map.entry(0x10010, 0x10DB2)); // 0x01
        POINTS.add(Map.entry(0x10DB3, 0x1157B)); // 0x02
        POINTS.add(Map.entry(0x1157C, 0x11932)); // 0x03
        POINTS.add(Map.entry(0x11933, 0x11A1F)); // 0x04
        POINTS.add(Map.entry(0x11A20, 0x11F74)); // 0x05
        POINTS.add(Map.entry(0x11F75, 0x1200F)); // 0x06
        POINTS.add(Map.entry(0x12010, 0x120DF)); // 0x07
        POINTS.add(Map.entry(0x120E0, 0x132FE)); // 0x08
    }

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前初始化数据
        paragraphsMap.clear();

        for (Map.Entry<Integer, Integer> point : POINTS) {
            // 得到这段文本的数据长度
            byte[] bytes = new byte[point.getValue() - point.getKey() + 1];
            // 定位，读取
            buffer.position(point.getKey());
            buffer.get(bytes);
            // 解析并通过换行分段
            String[] split = WordBank.toString(bytes).split("\n", -1);
            // 添加
            TextParagraphs textParagraphs = new TextParagraphs();
            textParagraphs.addAll(Arrays.asList(split));
            paragraphsMap.put(point, textParagraphs);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        for (Map.Entry<Map.Entry<Integer, Integer>, TextParagraphs> entry : paragraphsMap.entrySet()) {
            Map.Entry<Integer, Integer> point = entry.getKey();

            // 获取长度
            int length = point.getValue() - point.getKey() + 1;

            buffer.position(point.getKey());
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
                    buffer.put(bytes1, 0, len);
                    // 计算剩余可写入的空间
                    length -= len;
                }

                // 剩余可写入的空间至少为1写入 0x9F
                // 最后一个文本段不写入0x9F
                if (length > 0 && i != size - 1) {
                    // 文本段结束
                    buffer.put((byte) 0x9F);
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
                buffer.put(bytes);
            }
        }
        return true;
    }


    public Map<Map.Entry<Integer, Integer>, TextParagraphs> getParagraphsMap() {
        return paragraphsMap;
    }

    /**
     * @return 装备、道具的名称
     */
    public String getItemName(@Range(from = 0x00, to = 0xFF) int item) {
        if (item >= Item.ITEMS_MAX_COUNT){
            return null;
        }
        return paragraphsMap.get(POINTS.get(0x05)).get(item);
    }

    /**
     * @return 装备、道具的名称
     */
    public String getItemName(byte item) {
        if ((item & 0xFF) >= Item.ITEMS_MAX_COUNT){
            return null;
        }
        return paragraphsMap.get(POINTS.get(0x05)).get(item & 0xFF);
    }
}
