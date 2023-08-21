package me.afoolslove.metalmaxre.utils;

import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.text.TextBuilder;
import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {
    private TextUtils() {
    }

    /**
     * 获取所有物品名称
     *
     * @param textEditor 文本编辑器
     * @param hasId      名称前缀是否包含id
     * @return 所有物品名称
     */
    public static List<String> getItemNames(@NotNull ITextEditor textEditor, boolean hasId, @NotNull ICharMap charMap) {
        if (hasId) {
            List<String> names = new ArrayList<>();
            List<TextBuilder> list = textEditor.getPage(0x00);
            int size = Math.min(list.size(), 0x100); // 防止超过 0xFF
            for (int i = 0; i < size; i++) {
                names.add(String.format("%02X %s", i, list.get(i).toText(charMap)));
            }
            return names;
        }
        return textEditor.getPage(0x00).stream().map(v -> v.toText(charMap)).toList();
    }

    /**
     * 获取所有怪物的名称
     *
     * @param textEditor 文本编辑器
     * @param hasId      名称前缀是否包含id
     * @return 所有怪物的名称
     */
    public static List<String> getMonsterNames(@NotNull ITextEditor textEditor, boolean hasId, @NotNull ICharMap charMap) {
        if (hasId) {
            List<String> names = new ArrayList<>();
            List<TextBuilder> list = textEditor.getPage(0x01);
            for (int i = 0; i < list.size(); i++) {
                names.add(String.format("%02X %s", i, list.get(i).toText(charMap)));
            }
            return names;
        }
        return textEditor.getPage(0x01).stream().map(v -> v.toText(charMap)).toList();
    }


}
