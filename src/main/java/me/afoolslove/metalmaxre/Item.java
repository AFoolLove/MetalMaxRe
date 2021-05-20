package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editor.EditorManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有物品
 * 包含人类和战车的工具、装备
 *
 *
 * @author AFoolLove
 */
public class Item {
    public List<String> items = new ArrayList<>(0x100);

    public Item() {
//        ByteBuffer buffer = MetalMaxRe.getInstance().getBuffer();
        for (int i = 0; i < 0x100; i++) {
            // 占时使用ID作为名称
            items.add(String.format("item%02X", i));
        }
    }

    // offset 0
    // index 0

    // 00111100 .. 0       60 1
    // offset 7

    // 00111100 .. 00      60 2
}
