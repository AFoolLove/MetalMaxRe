package me.afoolslove.metalmaxre.editor.text;

import java.util.Arrays;

public class WordHelper {
    public static String toString(WordMap wordMap, byte[] bytes, int offset, int length) {
        StringBuilder text = new StringBuilder();
        byte[] copy = Arrays.copyOfRange(bytes, offset, offset + length);

        offset = 0; // 初始化为0

        wordMap:
        for (var entry : wordMap.entrySet()) {
            if (entry.getValue().isMultiple()) {
                value:
                for (byte[] value : entry.getValue().castMultiple().getMultiple()) {
                    // 验证是否一致
                    for (int i = 0; i < value.length; i++) {
                        if (value[i] != copy[offset + i]) {
                            // 不一致，下一个
                            continue value;
                        }
                    }
                    // 一致，将key追加到文本
                    text.append(entry.getKey().getSingle());
                    offset += value.length;
                }
            } else if (entry.getValue().isSingle()) {
                var value = entry.getValue().castSingle().getSingle();
                // 验证是否一致
                for (int i = 0; i < value.length; i++) {
                    if (value[i] != copy[offset + i]) {
                        // 不一致
                        continue wordMap;
                    }
                }
                // 一致，将key追加到文本
                text.append(entry.getKey().getSingle());
                offset++;
            }
        }
        return text.toString();
    }
}
