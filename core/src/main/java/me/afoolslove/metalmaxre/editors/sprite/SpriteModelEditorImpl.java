package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpriteModelEditorImpl extends RomBufferWrapperAbstractEditor implements ISpriteModelEditor {
    private final DataAddress spriteModelIndexAddress;
    private final DataAddress spriteModelAddress;

    private final List<SpriteModel> spriteModels = new ArrayList<>();

    public SpriteModelEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x270E0 - 0x10, 0x27169 - 0x10),
                DataAddress.fromPRG(0x2716A - 0x10, 0x27388 - 0x10));
    }

    public SpriteModelEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                 @NotNull DataAddress spriteModelIndexAddress,
                                 @NotNull DataAddress spriteModelAddress) {
        super(metalMaxRe);
        this.spriteModelIndexAddress = spriteModelIndexAddress;
        this.spriteModelAddress = spriteModelAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 初始化
        getSpriteModels().clear();

        char[] indexes = new char[getSpriteModelIndexAddress().length() / 0x02];
        position(getSpriteModelIndexAddress());
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = getBuffer().getChar();
        }

        int bankOffset = getSpriteModelAddress().getBankOffset();
        for (int i = 0; i < indexes.length; i++) {
            int position = indexes[i] - 0x8000 - bankOffset;
            position += getSpriteModelAddress().getAbsStartAddress(getBuffer());

            int head = getBuffer().get(position++);
            int attribute = getBuffer().get(position++);
            SpriteModel spriteModel = new SpriteModel(head, attribute, null);

            byte[] model = new byte[spriteModel.modelLength()];
            getBuffer().get(position, model);
            spriteModel.setModel(model);

            getSpriteModels().add(spriteModel);
        }
    }

    @Editor.Apply
    public void onApply() {
        char[] indexes = new char[getSpriteModelIndexAddress().length() / 0x02];

        // 写入模型数据
        int baseSpriteModelIndex = 0x8000 + getSpriteModelAddress().getBankOffset();
        position(getSpriteModelAddress());
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = (char) baseSpriteModelIndex;

            SpriteModel spriteModel = getSpriteModel(i);
            getBuffer().put(spriteModel.getHead());
            getBuffer().put(spriteModel.getAttribute());
            getBuffer().put(spriteModel.getModel());

            baseSpriteModelIndex += spriteModel.length();
        }

        // 写入模型索引
        position(getSpriteModelIndexAddress());
        for (char index : indexes) {
            getBuffer().putChar(NumberR.toChar(index));
        }
    }

    @Override
    public List<SpriteModel> getSpriteModels() {
        return spriteModels;
    }

    @Override
    public DataAddress getSpriteModelIndexAddress() {
        return spriteModelIndexAddress;
    }

    @Override
    public DataAddress getSpriteModelAddress() {
        return spriteModelAddress;
    }
}
