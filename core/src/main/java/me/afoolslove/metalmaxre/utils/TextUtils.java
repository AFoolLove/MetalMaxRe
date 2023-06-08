package me.afoolslove.metalmaxre.utils;

import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.text.TextBuilder;
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
    public static List<String> getItemNames(@NotNull ITextEditor textEditor, boolean hasId) {
        if (hasId) {
            List<String> names = new ArrayList<>();
            List<TextBuilder> list = textEditor.getPage(0x00);
            for (int i = 0; i < list.size(); i++) {
                names.add(String.format("%02X %s", i, list.get(i).toText()));
            }
            return names;
        }
        return textEditor.getPage(0x00).stream().map(TextBuilder::toText).toList();
    }

    /**
     * 获取所有怪物的名称
     *
     * @param textEditor 文本编辑器
     * @param hasId      名称前缀是否包含id
     * @return 所有怪物的名称
     */
    public static List<String> getMonsterNames(@NotNull ITextEditor textEditor, boolean hasId) {
        if (hasId) {
            List<String> names = new ArrayList<>();
            List<TextBuilder> list = textEditor.getPage(0x01);
            for (int i = 0; i < list.size(); i++) {
                names.add(String.format("%02X %s", i, list.get(i).toText()));
            }
            return names;
        }
        return textEditor.getPage(0x01).stream().map(TextBuilder::toText).toList();
    }


}
