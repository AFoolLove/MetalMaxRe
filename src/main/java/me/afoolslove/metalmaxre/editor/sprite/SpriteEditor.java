package me.afoolslove.metalmaxre.editor.sprite;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.map.MapPropertiesEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 精灵编辑器
 * <p>
 * 起始：0x24010、0x24204
 * 结束：0x24203、0x25176
 *
 * @author AFoolLove
 */
public class SpriteEditor extends AbstractEditor {

    /**
     * 地图的精灵数据索引
     * <p>
     * 0x24010-0x24203
     * <p>
     * 0x0A是干嘛来的来着？
     */
    private final List<Character> spritesIndex = new ArrayList<>(0xF0 + 0x0A);

    /**
     * K：sprites data index
     * V：sprites
     * <p>
     * 0x24204-0x25176
     */
    private final Map<Character, List<Sprite>> sprites = new HashMap<>();

    /**
     * 地图的精灵数据索引
     */
    private final Map<Integer, Character> mapSprites = new HashMap<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取精灵数据索引
        buffer.position(0x24010);
        for (int i = 0xF0 + 0x0A; i > 0; i--) {
            spritesIndex.add(i, (char) ((buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 0x08)));
        }

        // 通过索引读取精灵组
        for (int i = 0xF0 + 0x0A; i > 0; i--) {
            char index = spritesIndex.get(i);

            if (index == 0x0000) {

            }

        }

        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        return true;
    }

    /**
     * @return 精灵组合
     */
    public Map<Character, List<Sprite>> getSprites() {
        return sprites;
    }

    /**
     * @return 指定地图的所有精灵
     */
    public List<Sprite> getSprites(int map) {
        return sprites.get(spritesIndex.get(map));
    }


}
