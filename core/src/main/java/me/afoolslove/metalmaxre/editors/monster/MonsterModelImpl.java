package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

public class MonsterModelImpl extends RomBufferWrapperAbstractEditor {
    private final DataAddress monsterModelIndex = DataAddress.fromPRG(0x22C31 - 0x10, 0x22CB3 - 0x10);
    private final DataAddress monsterModelPaletteIndex = DataAddress.fromPRG(0x229CF - 0x10, 0x22A51 - 0x10);
    private final DataAddress monsterModelDoublePalette = DataAddress.fromPRG(0x22A52 - 0x10, 0x22A87 - 0x10);
    private final DataAddress monsterModelPalette = DataAddress.fromPRG(0x22A88 - 0x10);
    private final DataAddress monsterModelTileSet = DataAddress.fromPRG(0x22CB4 - 0x10, 0x22D02 - 0x10);
    private final DataAddress monsterModelIncrementalTileSet = DataAddress.fromPRG(0x22CB4 - 0x10, 0x22D02 - 0x10);
    private final DataAddress monsterModelSize = DataAddress.fromPRG(0x22D0A - 0x10, 0x22D57 - 0x10);
    private final DataAddress monsterModelLayoutIndex = DataAddress.fromPRG(0x22EB4 - 0x10, 0x22F51 - 0x10);
    private final DataAddress monsterModelLayout = DataAddress.fromPRG(0x22F52 - 0x10, 0x23223 - 0x10);

    private final DataAddress monsterModelLayout2 = DataAddress.fromPRG(0x22D59 - 0x10);


    private final byte[] modelIndex = new byte[0x83];
    private final byte[] modelPaletteIndex = new byte[0x83];
    private final byte[] modelDoublePalette = new byte[0x1B * 0x02];
    //    private final byte[] modelPalette = new byte[];
    private final byte[] modelTileSet = new byte[0x4F];
    private final byte[] modelSize = new byte[0x4F];
    private final byte[] modelLayoutIndex = new byte[0x4F * 0x02];

    private final byte[] modelLayout2 = new byte[0x48];


    public MonsterModelImpl(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
    }

    @Editor.Load
    public void onLoad() {
        getBuffer().get(getMonsterModelIndex(), modelIndex);
        getBuffer().get(getMonsterModelPaletteIndex(), modelPaletteIndex);
        getBuffer().get(getMonsterModelDoublePalette(), modelDoublePalette);
        getBuffer().get(getMonsterModelTileSet(), modelTileSet);
        getBuffer().get(getMonsterModelSize(), modelSize);
        getBuffer().get(getMonsterModelLayoutIndex(), modelLayoutIndex);
//        getBuffer().get(getMonsterModelLayout(), modelLayout);
        getBuffer().get(getMonsterModelLayout2(), modelLayout2);
    }

    @Editor.Apply
    public void onApply() {

    }

    public byte[] getModelIndex() {
        return modelIndex;
    }

    public byte[] getModelPaletteIndex() {
        return modelPaletteIndex;
    }

    public byte[] getModelDoublePalette() {
        return modelDoublePalette;
    }

    public byte[] getModelTileSet() {
        return modelTileSet;
    }

    public byte[] getModelSize() {
        return modelSize;
    }

    public byte[] getModelLayoutIndex() {
        return modelLayoutIndex;
    }


    public byte[] getModelLayout2() {
        return modelLayout2;
    }

    public DataAddress getMonsterModelIndex() {
        return monsterModelIndex;
    }

    public DataAddress getMonsterModelPaletteIndex() {
        return monsterModelPaletteIndex;
    }

    public DataAddress getMonsterModelDoublePalette() {
        return monsterModelDoublePalette;
    }

    public DataAddress getMonsterModelPalette() {
        return monsterModelPalette;
    }

    public DataAddress getMonsterModelTileSet() {
        return monsterModelTileSet;
    }

    public DataAddress getMonsterModelIncrementalTileSet() {
        return monsterModelIncrementalTileSet;
    }

    public DataAddress getMonsterModelSize() {
        return monsterModelSize;
    }

    public DataAddress getMonsterModelLayoutIndex() {
        return monsterModelLayoutIndex;
    }

    public DataAddress getMonsterModelLayout() {
        return monsterModelLayout;
    }

    public DataAddress getMonsterModelLayout2() {
        return monsterModelLayout2;
    }
}
