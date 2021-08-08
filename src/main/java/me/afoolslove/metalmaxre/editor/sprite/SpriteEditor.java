package me.afoolslove.metalmaxre.editor.sprite;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.map.MapEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * 精灵编辑器
 * <p>
 * 支持世界地图
 * <p>
 * 可以添加或修改任意地图的精灵属性
 * <p>
 * 起始：0x24010、0x24204
 * 结束：0x24203、0x25176
 * <p>
 * 末尾的0x0A个精灵组属于特殊精灵，不属于常规地图
 * <p>
 * 2021年5月30日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class SpriteEditor extends AbstractEditor<SpriteEditor> {
    public static final int SPRITE_INDEX_START_OFFSET = 0x24010 - 0x10;

    public static final int SPRITE_START_OFFSET = 0x24204 - 0x10;

    /**
     * 地图的精灵数据索引
     * <p>
     * 0x24010-0x24203
     * <p>
     * 0x0A是干嘛来的来着？
     * <p>
     * K：Map
     * V：sprites
     */
    private final HashMap<Integer, List<Sprite>> sprites = new HashMap<>(0xF0 + 0x0A);


    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 不提供精灵数据索引的修改！！

        // 读取前清空数据
        sprites.clear();

        // 读取精灵数据索引
        setPrgRomPosition(buffer, SPRITE_INDEX_START_OFFSET);
        Character[] spritesIndexes = new Character[0xF0 + 0x0A];

        for (int i = 0; i < 0xF0 + 0x0A; i++) {
            spritesIndexes[i] = (char) (getToInt(buffer) + (getToInt(buffer) << 0x08));
        }


        for (int i = 0; i < spritesIndexes.length; i++) {
            Character spritesIndex = spritesIndexes[i];
            if (spritesIndex == null) {
                // 跳过已设置的地图
                continue;
            }
            if (spritesIndex == 0x0000) {
                // 跳过没有精灵的地图
                getSprites().put(i, new ArrayList<>());
                continue;
            }

            // 读取精灵
            setPrgRomPosition(buffer, 0x24000 + spritesIndex - 0x8000);
            // 获取奖励数量
            int count = get(buffer);

            List<Sprite> spriteList = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                // 读取精灵的 类型、X、Y、对话1、对话2和行动方式
                spriteList.add(j, new Sprite(get(buffer), get(buffer), get(buffer), get(buffer), get(buffer), get(buffer)));
            }
            // 将精灵一样的地图一起设置
            for (int j = i; j < spritesIndexes.length; j++) {
                if (Objects.equals(spritesIndex, spritesIndexes[j])) {
                    // 设置为已读取
                    spritesIndexes[j] = null;
                    // 添加精灵
                    getSprites().put(j, spriteList);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 不提供精灵数据索引的修改！！

        // 指向精灵数据
        setPrgRomPosition(buffer, SPRITE_START_OFFSET);

        Character[] spritesIndexes = new Character[0xF0 + 0x0A];

        for (int map = 0; map < 0xF0 + 0x0A; map++) {
            List<Sprite> spriteList = getSprites(map);
            if (spriteList.isEmpty()) {
                // 跳过没有精灵的地图
                spritesIndexes[map] = 0x0000;
                continue;
            }

            // 排除已写入的精灵
            if (spritesIndexes[map] == null) {
                // 获取新的精灵数据索引
                char newSpritesIndex = (char) (bufferPosition - 0x24000 + 0x8000 - 0x10);
                // 将其它使用与此精灵数据一样的地图一起设置
                for (int nextMap = map; nextMap < 0xF0 + 0x0A; nextMap++) {
                    if (getSprites(nextMap) == spriteList) {
                        //
                        spritesIndexes[nextMap] = newSpritesIndex;
                    }
                }

                // 写入精灵数量
                put(buffer, (byte) spriteList.size());
                // 写入精灵
                for (Sprite sprite : spriteList) {
                    put(buffer, sprite.toByteArray());
                }
            }
        }

        int end = bufferPosition - 1;
        if (end <= 0x25176) {
            System.out.printf("精灵编辑器：剩余%d个空闲字节\n", 0x25176 - end);
        } else {
            System.out.printf("精灵编辑器：错误！超出了数据上限%d字节\n", end - 0x25176);
        }

        // 写入精灵数据索引
        setPrgRomPosition(buffer, SPRITE_INDEX_START_OFFSET);
        for (Character spritesIndex : spritesIndexes) {
            put(buffer, (byte) (spritesIndex & 0x00FF));
            put(buffer, (byte) ((spritesIndex & 0xFF00) >> 0x08));
        }
        return true;
    }

    /**
     * @return 精灵组合
     */
    public Map<Integer, List<Sprite>> getSprites() {
        return sprites;
    }

    /**
     * @return 指定地图的所有精灵
     */
    public List<Sprite> getSprites(@Range(from = 0x00, to = MapEditor.MAP_MAX_COUNT - 1) int map) {
        return sprites.get(map);
    }

    /**
     * @return 世界地图的所有精灵
     */
    public List<Sprite> getWorldSprites() {
        return sprites.get(0x00);
    }


}
