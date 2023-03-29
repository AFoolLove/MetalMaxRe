package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class SpriteEditorImpl extends RomBufferWrapperAbstractEditor implements ISpriteEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpriteEditorImpl.class);
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
            // 获取精灵数量
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

        // 会被写入的精灵数据
        List<byte[]> spritesData = new ArrayList<>();
        // 精灵索引
        Character[] spritesIndexes = new Character[0xF0 + 0x0A];
        // 精灵索引起始
//        char spriteIndex = 0x81F4;
        char spriteIndex = (char) (getSpriteAddress().getStartAddress() - 0x24000 + 0x8000);
        final char endSpriteIndex = (char) (spriteIndex + getSpriteAddress().length());

        for (int mapId = 0, count = 0xF0 + 0x0A; mapId < count; mapId++) {
            if (spritesIndexes[mapId] != null) {
                // 已经设置
                continue;
            }

            List<Sprite> mapSprites = getSprites().get(mapId);
            if (mapSprites.isEmpty()) {
                // 该地图没有精灵
                continue;
            }
            if (spriteIndex == endSpriteIndex) {
                LOGGER.error("精灵编辑器：剩余的空间无法写入地图{}的所有精灵", NumberR.toHex(mapId));
                continue;
            }
            if (endSpriteIndex - spriteIndex < (0x01 + (mapSprites.size() * 0x06))) {
                // 剩余空间不能完整的写入了
                spriteIndex = endSpriteIndex;
                LOGGER.error("精灵编辑器：剩余的空间无法写入地图{}的所有精灵", NumberR.toHex(mapId));
                continue;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(0x01 + (mapSprites.size() * 0x06));
            // 数量
            outputStream.write(mapSprites.size() & 0xFF);
            for (Sprite mapSprite : mapSprites) {
                outputStream.writeBytes(mapSprite.toByteArray());
            }
            byte[] mapSpriteData = outputStream.toByteArray();

            // 将后面相同的数据一同设置
            for (int afterMapId = mapId; afterMapId < count; afterMapId++) {
                if (Objects.equals(getSprites().get(afterMapId), mapSprites)) {
                    spritesIndexes[afterMapId] = spriteIndex;
                    if (afterMapId != mapId) {
                        LOGGER.info("精灵编辑器：地图{}与{}使用相同的精灵",
                                NumberR.toHex(afterMapId),
                                NumberR.toHex(mapId));
                    }
                }
            }

            spritesData.add(mapSpriteData);
            spriteIndex += outputStream.size();
        }

        // 写入精灵数据索引
        position(getSpriteIndexAddress());
        for (Character spritesIndex : spritesIndexes) {
            if (spritesIndex == null) {
                // 没有精灵
                spritesIndex = 0x0000;
            }
            getBuffer().putChar(NumberR.toChar(spritesIndex));
        }

        // 写入精灵数据
        position(getSpriteAddress());
        for (byte[] spritesDatum : spritesData) {
            getBuffer().put(spritesDatum);
        }

        int end = getSpriteAddress().getEndAddress(-position() + 0x10 + 1);
        if (end >= 0) {
            if (end > 0) {
                // 使用0xFF填充未使用的数据
                byte[] fillBytes = new byte[end];
                Arrays.fill(fillBytes, (byte) 0x00);
                getBuffer().put(fillBytes);
            }
            LOGGER.info("精灵编辑器：剩余{}个空闲字节", end);
        } else {
            LOGGER.error("精灵编辑器：错误！超出了数据上限{}字节", -end);
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
