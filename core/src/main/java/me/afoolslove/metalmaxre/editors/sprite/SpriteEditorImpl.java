package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SpriteEditorImpl extends RomBufferWrapperAbstractEditor implements ISpriteEditor {
    private final DataAddress spritesIndexAddress;
    private final DataAddress spritesAddress;
    /**
     * 地图的精灵<p>
     * 0x0A是干嘛来的来着？<p>
     * K：Map<p>
     * V：sprites
     */
    private final HashMap<Integer, List<Sprite>> sprites = new HashMap<>(0xF0 + 0x0A);

    public SpriteEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x24010 - 0x10, 0x24203 - 0x10),
                DataAddress.fromPRG(0x24204 - 0x10, 0x25176 - 0x10));
    }

    public SpriteEditorImpl(@NotNull MetalMaxRe metalMaxRe, @NotNull DataAddress spritesIndexAddress, @NotNull DataAddress spritesAddress) {
        super(metalMaxRe);
        this.spritesIndexAddress = spritesIndexAddress;
        this.spritesAddress = spritesAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 不提供精灵数据索引的修改！！

        // 读取前清空数据
        sprites.clear();

        // 读取精灵数据索引
        position(getSpriteIndexAddress());
        Character[] spritesIndexes = new Character[0xF0 + 0x0A];

        for (int i = 0; i < 0xF0 + 0x0A; i++) {
            spritesIndexes[i] = getBuffer().getChar();
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
            prgPosition(0x24000 + spritesIndex - 0x8000);
            // 获取奖励数量
            int count = getBuffer().get();

            List<Sprite> spriteList = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                // 读取精灵的 类型、X、Y、对话1、对话2和行动方式
                spriteList.add(j, new Sprite(getBuffer().get(), getBuffer().get(), getBuffer().get(), getBuffer().get(), getBuffer().get(), getBuffer().get()));
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
    }

    @Editor.Apply
    public void onApply() {
        // 不提供精灵数据索引的修改！！

        // 指向精灵数据
        position(getSpriteAddress());

        Character[] spritesIndexes = new Character[0xF0 + 0x0A];

        final boolean isTrained = getBuffer().getHeader().isTrained();
        for (int map = 0; map < 0xF0 + 0x0A; map++) {
            List<Sprite> spriteList = getSprites().get(map);
            if (spriteList.isEmpty()) {
                // 跳过没有精灵的地图
                spritesIndexes[map] = 0x0000;
                continue;
            }

            // 排除已写入的精灵
            if (spritesIndexes[map] == null) {
                // 获取新的精灵数据索引
                char newSpritesIndex = (char) (position() - (isTrained ? 0x200 : 0x000) - 0x24000 + 0x8000 - 0x10);
                // 将其它使用与此精灵数据一样的地图一起设置
                for (int nextMap = map; nextMap < 0xF0 + 0x0A; nextMap++) {
                    if (getSprites().get(nextMap) == spriteList) {
                        //
                        spritesIndexes[nextMap] = newSpritesIndex;
                    }
                }

                // 写入精灵数量
                getBuffer().put((byte) spriteList.size());

                // 写入精灵
                for (Sprite sprite : spriteList) {
                    getBuffer().put(sprite.toByteArray());
                }
            }
        }

        int end = getSpriteAddress().getEndAddress(-position() + 0x10 + 1);
        if (end >= 0) {
            System.out.printf("精灵编辑器：剩余%d个空闲字节\n", end);
        } else {
            System.out.printf("精灵编辑器：错误！超出了数据上限%d字节\n", -end);
        }

        // 写入精灵数据索引
        position(getSpriteIndexAddress());
        for (Character spritesIndex : spritesIndexes) {
            getBuffer().putChar(NumberR.toChar(spritesIndex));
        }
    }

    @Override
    public Map<Integer, List<Sprite>> getSprites() {
        return sprites;
    }

    @Override
    public List<Sprite> getWorldSprites() {
        return getSprites().get(0x00);
    }

    @Override
    public DataAddress getSpriteIndexAddress() {
        return spritesIndexAddress;
    }

    @Override
    public DataAddress getSpriteAddress() {
        return spritesAddress;
    }
}
