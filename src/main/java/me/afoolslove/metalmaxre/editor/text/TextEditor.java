package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * 文本编辑器
 * <p>
 * 未找出所有文本的位置（可以直接将整个文件转换为String，但。。。
 *
 * @author AFoolLove
 */
public class TextEditor extends AbstractEditor {
    public static final List<Map.Entry<Integer, Integer>> POINTS = new ArrayList<>();

    private final Map<Map.Entry<Integer, Integer>, TextParagraphs> paragraphsMap = new HashMap<>();

    static {
        POINTS.add(Map.entry(0x0BE90, 0x0C00F));
        POINTS.add(Map.entry(0x10010, 0x10DB2));
        POINTS.add(Map.entry(0x10DB3, 0x1157B));
        POINTS.add(Map.entry(0x1157C, 0x11932));
        POINTS.add(Map.entry(0x11933, 0x11A1F));
        POINTS.add(Map.entry(0x11A20, 0x11F74));
        POINTS.add(Map.entry(0x11F75, 0x1200F));
        POINTS.add(Map.entry(0x12010, 0x120DF));
        POINTS.add(Map.entry(0x120E0, 0x132FE));
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
            String s = WordBank.toString(bytes);
            String[] split = s.split("\n", -1);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i = 0; i < split.length; i++) {
                byte[] bytes1 = WordBank.toBytes(split[i]);
                byteArrayOutputStream.write(bytes1, 0, bytes1.length);
                if (i != split.length - 1 || s.charAt(s.length() - 1) == '\n') {
                    byteArrayOutputStream.write(0x9F);
                }
            }

            if (byteArrayOutputStream.toByteArray().length != bytes.length) {
                System.out.println();
            }
            // 添加
            TextParagraphs textParagraphs = new TextParagraphs();
            textParagraphs.addAll(Arrays.asList(split));
            paragraphsMap.put(point, textParagraphs);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        return false;
    }
}
