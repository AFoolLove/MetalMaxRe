package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.SystemSprite;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpriteModelEditorImpl extends RomBufferWrapperAbstractEditor implements ISpriteModelEditor {
    private final DataAddress spriteModelIndexAddress;
    private final DataAddress spriteModelAddress;

    private final DataAddress systemSpriteModelIndexAddress;
    private final DataAddress systemSpriteModelAddress;

    private final DataAddress battleSpriteModelAttributeAddress;
    private final DataAddress battleSpriteModelIndexAddress;
    private final DataAddress battleSpriteModelAddress;


    private final List<SpriteModel> spriteModels = new ArrayList<>();
    private final List<SystemSpriteModel> systemSpriteModels = new ArrayList<>();
    private final List<BattleSpriteModel> battleSpriteModels = new ArrayList<>();

    public SpriteModelEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x270E0 - 0x10, 0x27169 - 0x10),
                DataAddress.fromPRG(0x2716A - 0x10, 0x27388 - 0x10),
                DataAddress.fromPRG(0x2605A - 0x10, 0x260C5 - 0x10),
                DataAddress.fromPRG(0x260C6 - 0x10, 0x2636B - 0x10),
                DataAddress.fromPRG(0x2680C - 0x10, 0x26A07 - 0x10),
                DataAddress.fromPRG(0x26A08 - 0x10, 0x26B05 - 0x10),
                DataAddress.fromPRG(0x26B06 - 0x10, 0x27040 - 0x10)
        );
    }

    public SpriteModelEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                 @NotNull DataAddress spriteModelIndexAddress,
                                 @NotNull DataAddress spriteModelAddress,
                                 @NotNull DataAddress systemSpriteModelIndexAddress,
                                 @NotNull DataAddress systemSpriteModelAddress,
                                 @NotNull DataAddress battleSpriteModelIndexAddress,
                                 @NotNull DataAddress battleSpriteModelAttributeAddress,
                                 @NotNull DataAddress battleSpriteModelAddress) {
        super(metalMaxRe);
        this.spriteModelIndexAddress = spriteModelIndexAddress;
        this.spriteModelAddress = spriteModelAddress;
        this.systemSpriteModelIndexAddress = systemSpriteModelIndexAddress;
        this.systemSpriteModelAddress = systemSpriteModelAddress;
        this.battleSpriteModelIndexAddress = battleSpriteModelIndexAddress;
        this.battleSpriteModelAttributeAddress = battleSpriteModelAttributeAddress;
        this.battleSpriteModelAddress = battleSpriteModelAddress;
    }

    @Editor.Load
    public void onLoad() {
        // 初始化
        getSpriteModels().clear();
        getSystemSpriteModels().clear();
        getBattleSpriteModels().clear();

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

        indexes = new char[getSystemSpriteModelIndexAddress().length() / 0x02];
        position(getSystemSpriteModelIndexAddress());
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = getBuffer().getChar();
        }

        bankOffset = getSystemSpriteModelAddress().getBankOffset();
        for (int i = 0; i < indexes.length; i++) {
            int position = indexes[i] - 0x8000 - bankOffset;
            position += getSystemSpriteModelAddress().getAbsStartAddress(getBuffer());

            int count = getBuffer().get(position++);
            SystemSpriteModel systemSpriteModel = new SystemSpriteModel();
            for (int c = 0; c < count; c++) {
                int y = getBuffer().getToInt(position++);
                int tile = getBuffer().getToInt(position++);
                int attribute = getBuffer().getToInt(position++);
                int x = getBuffer().getToInt(position++);
                SystemSprite systemSprite = new SystemSprite(x, y, tile, attribute);
                systemSpriteModel.add(systemSprite);
            }
            getSystemSpriteModels().add(systemSpriteModel);
        }

        // 获取战斗精灵模型数据
        byte[] modelAttributes = new byte[getBattleSpriteModelAttributeAddress().length()];
        getBuffer().get(getBattleSpriteModelAttributeAddress(), modelAttributes);

        indexes = new char[getBattleSpriteModelIndexAddress().length() / 0x02];
        position(getBattleSpriteModelIndexAddress());
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = getBuffer().getChar();
        }
        bankOffset = getBattleSpriteModelAddress().getBankOffset();
        for (int i = 0; i < indexes.length; i++) {
            int position = indexes[i] - 0x8000 - bankOffset;
            position += getBattleSpriteModelAddress().getAbsStartAddress(getBuffer());
            BattleSpriteModel battleSpriteModel = new BattleSpriteModel(modelAttributes[i], (byte) 0, null);

            byte offset = getBuffer().get(position++);
            byte[] model = new byte[battleSpriteModel.getHeight() * battleSpriteModel.getWidth()];
            getBuffer().get(position, model);
            battleSpriteModel.setOffset(offset);
            battleSpriteModel.setModel(model);
            getBattleSpriteModels().add(battleSpriteModel);
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


        indexes = new char[getSystemSpriteModelIndexAddress().length() / 0x02];
        // 写入系统精灵模型数据
        baseSpriteModelIndex = 0x8000 + getSystemSpriteModelAddress().getBankOffset();
        position(getSystemSpriteModelAddress());
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = (char) baseSpriteModelIndex;

            List<SystemSprite> systemSpriteModel = getSystemSpriteModel(i);
            // 数量
            getBuffer().put(systemSpriteModel.size());
            for (SystemSprite systemSprite : systemSpriteModel) {
                getBuffer().put(systemSprite.toArrayByte());
            }
            baseSpriteModelIndex += 1 + (systemSpriteModel.size() * 0x04);
        }

        // 写入模型索引
        position(getSystemSpriteModelIndexAddress());
        for (char index : indexes) {
            getBuffer().putChar(NumberR.toChar(index));
        }

        // 写入战斗精灵模型数据
        indexes = new char[getBattleSpriteModelIndexAddress().length() / 0x02];
        byte[] attributes = new byte[getBattleSpriteModelAttributeAddress().length()];
        baseSpriteModelIndex = 0x8000 + getBattleSpriteModelAddress().getBankOffset();
        position(getBattleSpriteModelAddress());
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = (char) baseSpriteModelIndex;
            BattleSpriteModel battleSpriteModel = getBattleSpriteModels().get(i);
            attributes[i] = battleSpriteModel.getAttribute();
            getBuffer().put(battleSpriteModel.getOffset());
            getBuffer().put(battleSpriteModel.getModel());
            baseSpriteModelIndex += 1 + (battleSpriteModel.getWidth() * battleSpriteModel.getHeight());
        }

        // 写入战斗精灵模型属性
        position(getBattleSpriteModelAttributeAddress());
        getBuffer().put(attributes);
        // 写入战斗精灵模型索引
        position(getBattleSpriteModelIndexAddress());
        for (char index : indexes) {
            getBuffer().putChar(NumberR.toChar(index));
        }
    }

    @Override
    public List<SpriteModel> getSpriteModels() {
        return spriteModels;
    }

    @Override
    public List<SystemSpriteModel> getSystemSpriteModels() {
        return systemSpriteModels;
    }

    @Override
    public List<BattleSpriteModel> getBattleSpriteModels() {
        return battleSpriteModels;
    }

    @Override
    public DataAddress getSpriteModelIndexAddress() {
        return spriteModelIndexAddress;
    }

    @Override
    public DataAddress getSpriteModelAddress() {
        return spriteModelAddress;
    }

    @Override
    public DataAddress getSystemSpriteModelIndexAddress() {
        return systemSpriteModelIndexAddress;
    }

    @Override
    public DataAddress getSystemSpriteModelAddress() {
        return systemSpriteModelAddress;
    }


    @Override
    public DataAddress getBattleSpriteModelIndexAddress() {
        return battleSpriteModelIndexAddress;
    }

    @Override
    public DataAddress getBattleSpriteModelAttributeAddress() {
        return battleSpriteModelAttributeAddress;
    }

    @Override
    public DataAddress getBattleSpriteModelAddress() {
        return battleSpriteModelAddress;
    }
}
