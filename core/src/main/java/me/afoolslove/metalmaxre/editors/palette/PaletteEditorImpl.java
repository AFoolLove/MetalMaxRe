package me.afoolslove.metalmaxre.editors.palette;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

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
@Editor.TargetVersions
public class PaletteEditorImpl extends RomBufferWrapperAbstractEditor implements IPaletteEditor {
    private final DataAddress paletteAddress;
    private final DataAddress globalSpritePalettesAddress;
    private final DataAddress battleGlobalSpritePalettesAddress;

    /**
     * 所有调色板
     */
    private final List<List<PaletteRow>> palettes = new ArrayList<>();
    /**
     * 精灵调色板（非战斗时）
     */
    private final List<PaletteRow> spritePalette = new ArrayList<>();
    /**
     * 精灵调色板（战斗时）
     */
    private final List<PaletteRow> battleSpritePalette = new ArrayList<>();

    public PaletteEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x1DAE0 - 0x10, 0x1DCCE - 0x10),
                DataAddress.fromPRG(0x7D737 - 0x10, 0x7D746 - 0x10),
                DataAddress.fromPRG(0x22B66 - 0x10, 0x22B71 - 0x10));
    }

    public PaletteEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                             @NotNull DataAddress paletteAddress,
                             @NotNull DataAddress globalSpritePalettesAddress,
                             @NotNull DataAddress battleGlobalSpritePalettesAddress) {
        super(metalMaxRe);
        this.paletteAddress = paletteAddress;
        this.globalSpritePalettesAddress = globalSpritePalettesAddress;
        this.battleGlobalSpritePalettesAddress = battleGlobalSpritePalettesAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 读取前清空数据
        getPalettes().clear();
        getSpritePalette().clear();
        getBattleSpritePalette().clear();

        position(getPaletteAddress());
        // 读取所有调色板集（(3+3+3)byte ）
        for (int i = 0; i < getPaletteMaxCount(); i++) {
            List<PaletteRow> palettes = new ArrayList<>();
            palettes.add(0x00, new PaletteRow(getBuffer(), position()));
            offsetPosition(3);
            palettes.add(0x01, new PaletteRow(getBuffer(), position()));
            offsetPosition(3);
            palettes.add(0x02, new PaletteRow(getBuffer(), position()));
            offsetPosition(3);
            // 固定颜色，改了也不会写入到游戏中
            palettes.add(0x03, new PaletteRow(0x30, 0x10, 0x00));
            getPalettes().add(palettes);
        }

        // 读取精灵调色板，固定数值
        position(getGlobalSpritePalettesAddress());
        for (int i = 0; i < 0x04; i++) {
            offsetPosition(1); // 跳过 0x0F
            spritePalette.add(new PaletteRow(getBuffer(), position()));
            offsetPosition(3);
        }

        // 读取战斗时的精灵调色板
        position(getBattleGlobalSpritePalettesAddress());
        for (int i = 0; i < 0x04; i++) {
            battleSpritePalette.add(new PaletteRow(getBuffer(), position()));
            offsetPosition(3);
        }
    }

    @Editor.Apply
    public void onApply() {
        // 写入调色板
        position(getPaletteAddress());
        for (int i = 0; i < getPaletteMaxCount(); i++) {
            var paletteRows = getPalettes().get(i);
            // 3个调色板，1个调色板3byte
            for (int j = 0; j < 0x03; j++) {
                getBuffer().put(paletteRows.get(j).getPaletteRow(), 1, 3);
            }
        }

//        int end = position() - 1;
//        if (end <= 0x1DCCE) {
//            System.out.printf("调色板编辑器：剩余%d个空闲字节\n", 0x1DCCE - end);
//        } else {
//            System.out.printf("调色板编辑器：错误！超出了数据上限%d字节\n", end - 0x1DCCE);
//        }

        // 写入精灵调色板
        position(getGlobalSpritePalettesAddress());
        for (int i = 0; i < 0x04; i++) {
            getBuffer().put(getSpritePalette().get(i).getPaletteRow());
        }

        // 写入战斗时的精灵调色板
        position(getBattleGlobalSpritePalettesAddress());
        for (int i = 0; i < 0x04; i++) {
            // 只写入后三位
            getBuffer().put(getBattleSpritePalette().get(i).getPaletteRow(), 1, 3);
        }
    }

    public List<List<PaletteRow>> getPalettes() {
        return palettes;
    }

    @Override
    public List<PaletteRow> getSpritePalette() {
        return spritePalette;
    }

    @Override
    public List<PaletteRow> getBattleSpritePalette() {
        return battleSpritePalette;
    }

    @Override
    public DataAddress getPaletteAddress() {
        return paletteAddress;
    }

    @Override
    public DataAddress getGlobalSpritePalettesAddress() {
        return globalSpritePalettesAddress;
    }

    @Override
    public DataAddress getBattleGlobalSpritePalettesAddress() {
        return battleGlobalSpritePalettesAddress;
    }

    /**
     * 通过游戏中使用的数据索引获取调色板集
     * ！不是本程序的索引！
     *
     * @return 调色板
     */
    public List<PaletteRow> getPalettes(@Range(from = 0x9AD0, to = 0xFFFF) int position) {
        // 0x8000+0x1AD0=基础数据起始
        // 9byte 每组数据的长度
        // 获取索引
        int index = (position - (0x8000 + 0x1AD0)) / 9;
        return getPalettes().get(index);
    }

//    /**
//     * @return 地图的调色板集，不包含世界地图，世界地图为动态调色板
//     */
//    public List<PaletteRow> getMapPalettes(@Range(from = 0x01, to = MapEditor.MAP_MAX_COUNT - 1) int map) {
//        MapPropertiesEditor mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);
//        return getPalettes(mapPropertiesEditor.getMapProperties(map).palette);
//    }


    @Editor.TargetVersion("japanese")
    public static class JPaletteEditorImpl extends PaletteEditorImpl {
        public JPaletteEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe,
                    DataAddress.fromPRG(0x1DAE0 - 0x10, 0x1DCCE - 0x10),
                    DataAddress.fromPRG(0x3D737 - 0x10, 0x3D746 - 0x10),
                    DataAddress.fromPRG(0x22B66 - 0x10, 0x22B71 - 0x10));
        }

        @Editor.Load
        @Override
        public void onLoad() {
            super.onLoad();
        }

        @Editor.Apply
        @Override
        public void onApply() {
            super.onApply();
        }
    }

    @Editor.TargetVersion({"super_hack", "super_hack_general"})
    public static class SHPaletteEditorImpl extends PaletteEditorImpl {
        public SHPaletteEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe,
                    DataAddress.fromPRG(0x1DAE0 - 0x10, 0x1DCCE - 0x10),
                    DataAddress.fromPRG(0x81737 - 0x10, 0x81746 - 0x10),
                    DataAddress.fromPRG(0x22B66 - 0x10, 0x22B71 - 0x10));
        }

        @Editor.Load
        @Override
        public void onLoad() {
            super.onLoad();
        }

        @Editor.Apply
        @Override
        public void onApply() {
            super.onApply();
        }
    }
}