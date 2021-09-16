package me.afoolslove.metalmaxre.editor.palette;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.map.MapEditor;
import me.afoolslove.metalmaxre.editor.map.MapPropertiesEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 调色板编辑器
 * 游戏中，地图、精灵等的调色板
 * 调色板顺序不宜混存，所以调色板数据只提供修改
 * <p>
 * 起始：0x1DAE0
 * 结束：0x1DCCE
 * <p>
 * 2021年6月5日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class PaletteEditor extends AbstractEditor<PaletteEditor> {
    /**
     * 调色板数量
     */
    public static final int PALETTE_LIST_MAX_COUNT = 0xA5;

    /**
     * 调色板数据
     */
    public static final int PALETTE_START_OFFSET = 0x1DAE0 - 0x10;
    public static final int PALETTE_END_OFFSET = 0x1DCCE - 0x10;

    /**
     * 全局精灵调色板（非战斗时
     */
    public static final int PALETTE_LIST_SPRITE_START_OFFSET = 0x7D737 - 0x10;
    public static final int PALETTE_LIST_SPRITE_END_OFFSET = 0x7D746 - 0x10;

    /**
     * 精灵调色板（战斗时
     */
    public static final int PALETTE_LIST_BATTLE_SPRITE_START_OFFSET = 0x22B66 - 0x10;
    public static final int PALETTE_LIST_BATTLE_SPRITE_END_OFFSET = 0x22B71 - 0x10;

    /**
     * 所有调色板
     */
    private final List<PaletteList> paletteLists = new ArrayList<>();
    /**
     * 精灵调色板（非战斗时）
     */
    private final PaletteList spritePalette = new PaletteList();
    /**
     * 精灵调色板（战斗时）
     */
    private final PaletteList battleSpritePalette = new PaletteList();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        paletteLists.clear();
        spritePalette.clear();
        battleSpritePalette.clear();

        setPrgRomPosition(PALETTE_START_OFFSET);
        // 读取所有调色板集（9byte）
        for (int i = 0; i < (PALETTE_LIST_MAX_COUNT / 0x03); i++) {
            PaletteList palettes = new PaletteList();
            palettes.add(0x00, new Palette(buffer, bufferPosition));
            skip(3);
            palettes.add(0x01, new Palette(buffer, bufferPosition));
            skip(3);
            palettes.add(0x02, new Palette(buffer, bufferPosition));
            skip(3);
            // 固定颜色，改了也不会写入到游戏中
            palettes.add(0x03, new Palette(0x30, 0x10, 0x00));
            paletteLists.add(palettes);
        }

        // 读取精灵调色板，固定数值
        setPrgRomPosition(PALETTE_LIST_SPRITE_START_OFFSET);
        for (int i = 0; i < 0x04; i++) {
            skip();; // 跳过 0x0F
            spritePalette.add(new Palette(buffer, bufferPosition));
            skip(3);
        }

        // 读取战斗时的精灵调色板
        setPrgRomPosition(PALETTE_LIST_BATTLE_SPRITE_START_OFFSET);
        for (int i = 0; i < 0x04; i++) {
            battleSpritePalette.add(new Palette(buffer, bufferPosition));
            skip(3);
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        // 优先储存后加入的
        ArrayList<PaletteList> paletteLists = new ArrayList<>(this.paletteLists);
        int fromIndex = Math.max(0, paletteLists.size() - PALETTE_LIST_MAX_COUNT);
        setPrgRomPosition(PALETTE_START_OFFSET);
        for (int index = fromIndex, size = paletteLists.size(); index < size; index++) {
            PaletteList palettes = paletteLists.get(index);
            // 3个调色板，1个调色板3byte
            for (int i = 0; i < 0x03; i++) {
                put(buffer, palettes.get(i).getColors(), 1, 3);
            }
        }

        int end = bufferPosition - 1;
        if (end <= 0x1DCCE) {
            System.out.printf("调色板编辑器：剩余%d个空闲字节\n", 0x1DCCE - end);
        } else {
            System.out.printf("调色板编辑器：错误！超出了数据上限%d字节\n", end - 0x1DCCE);
        }

        // 写入精灵调色板
        setPrgRomPosition(PALETTE_LIST_SPRITE_START_OFFSET);
        for (int i = 0; i < 0x04; i++) {
            put(buffer, getSpritePalette().get(i).getColors());
        }

        // 写入战斗时的精灵调色板
        setPrgRomPosition(PALETTE_LIST_BATTLE_SPRITE_START_OFFSET);
        for (int i = 0; i < 0x04; i++) {
            byte[] palette = getBattleSpritePalette().get(i).getColors();
            // 只写入后三位
            put(buffer, palette, 1, 3);
        }
        return true;
    }

    /**
     * @return 所有调色板集的副本
     */
    public List<PaletteList> getPaletteLists() {
        return new ArrayList<>(paletteLists);
    }

    /**
     * @return 精灵的调色板
     */
    public PaletteList getSpritePalette() {
        return spritePalette;
    }

    /**
     * @return 战斗时的精灵调色板
     */
    public PaletteList getBattleSpritePalette() {
        return battleSpritePalette;
    }

    /**
     * 通过游戏中使用的数据索引获取调色板集
     * ！不是本程序的索引！
     *
     * @return 调色板
     */
    public PaletteList getPalettes(@Range(from = 0x9AD0, to = 0xFFFF) int position) {
        // 0x8000+0x1AD0=基础数据起始
        // 9byte 每组数据的长度
        // 获取索引
        int index = (position - (0x8000 + 0x1AD0)) / 9;
        return paletteLists.get(index);
    }

    /**
     * @return 地图的调色板集，不包含世界地图，世界地图为动态调色板
     */
    public PaletteList getMapPalettes(@Range(from = 0x01, to = MapEditor.MAP_MAX_COUNT - 1) int map) {
        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);
        return getPalettes(mapPropertiesEditor.getMapProperties(map).palette);
    }
}
