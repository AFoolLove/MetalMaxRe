package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.SystemSprite;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SpriteModelEditorImpl extends RomBufferWrapperAbstractEditor implements ISpriteModelEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpriteModelEditorImpl.class);

    /**
     * 获取精灵模型索引地址
     */
    public static final String SPRITE_MODEL_INDEX_ADDRESS = "spriteModelIndex";
    /**
     * 获取精灵模型数据地址
     */
    public static final String SPRITE_MODEL_ADDRESS = "spriteModel";

    /**
     * 获取系统精灵模型索引地址
     */
    public static final String SYSTEM_SPRITE_MODEL_INDEX_ADDRESS = "systemSpriteModelIndex";
    /**
     * 获取系统精灵模型数据地址
     */
    public static final String SYSTEM_SPRITE_MODEL_ADDRESS = "systemSpriteModel";

    /**
     * 获取战斗精灵模型索引地址
     */
    public static final String BATTLE_SPRITE_MODEL_INDEX_ADDRESS = "battleSpriteModelIndex";
    /**
     * 获取战斗精灵模型属性地址
     */
    public static final String BATTLE_SPRITE_MODEL_ATTRIBUTE_ADDRESS = "battleSpriteModelAttribute";
    /**
     * 获取战斗精灵模型数据地址
     */
    public static final String BATTLE_SPRITE_MODEL_ADDRESS = "battleSpriteModel";


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
        super(metalMaxRe, false);
        putDataAddress(SPRITE_MODEL_INDEX_ADDRESS, spriteModelIndexAddress);
        putDataAddress(SPRITE_MODEL_ADDRESS, spriteModelAddress);
        putDataAddress(SYSTEM_SPRITE_MODEL_INDEX_ADDRESS, systemSpriteModelIndexAddress);
        putDataAddress(SYSTEM_SPRITE_MODEL_ADDRESS, systemSpriteModelAddress);
        putDataAddress(BATTLE_SPRITE_MODEL_INDEX_ADDRESS, battleSpriteModelIndexAddress);
        putDataAddress(BATTLE_SPRITE_MODEL_ATTRIBUTE_ADDRESS, battleSpriteModelAttributeAddress);
        putDataAddress(BATTLE_SPRITE_MODEL_ADDRESS, battleSpriteModelAddress);
    }

    @Editor.Load
    public void onLoad() {
        // 初始化
        getSpriteModels().clear();
        getSystemSpriteModels().clear();
        getBattleSpriteModels().clear();

        DataAddress spriteModelIndexAddress = getDataAddress(SPRITE_MODEL_INDEX_ADDRESS);
        char[] indexes = new char[spriteModelIndexAddress.length() / 0x02];
        position(spriteModelIndexAddress);
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = getBuffer().getChar();
        }

        DataAddress spriteModelAddress = getDataAddress(SPRITE_MODEL_ADDRESS);
        int bankOffset = spriteModelAddress.getBankOffset();
        for (int i = 0; i < indexes.length; i++) {
            int position = indexes[i] - 0x8000 - bankOffset;
            position += spriteModelAddress.getAbsStartAddress(getBuffer());

            int head = getBuffer().get(position++);
            int attribute = getBuffer().get(position++);
            SpriteModel spriteModel = new SpriteModel(head, attribute, null);

            byte[] model = new byte[spriteModel.modelLength()];
            getBuffer().get(position, model);
            spriteModel.setModel(model);

            getSpriteModels().add(spriteModel);
        }

        DataAddress systemSpriteModelIndexAddress = getDataAddress(SYSTEM_SPRITE_MODEL_INDEX_ADDRESS);
        indexes = new char[systemSpriteModelIndexAddress.length() / 0x02];
        position(systemSpriteModelIndexAddress);
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = getBuffer().getChar();
        }

        DataAddress systemSpriteModelAddress = getDataAddress(SYSTEM_SPRITE_MODEL_ADDRESS);
        bankOffset = systemSpriteModelAddress.getBankOffset();
        for (int i = 0; i < indexes.length; i++) {
            int position = indexes[i] - 0x8000 - bankOffset;
            position += systemSpriteModelAddress.getAbsStartAddress(getBuffer());

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
        DataAddress battleSpriteModelAddress = getDataAddress(BATTLE_SPRITE_MODEL_ADDRESS);
        byte[] modelAttributes = new byte[battleSpriteModelAddress.length()];
        getBuffer().get(battleSpriteModelAddress, modelAttributes);

        DataAddress battleSpriteModelIndexAddress = getDataAddress(BATTLE_SPRITE_MODEL_INDEX_ADDRESS);
        indexes = new char[battleSpriteModelIndexAddress.length() / 0x02];
        position(battleSpriteModelIndexAddress);
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = getBuffer().getChar();
        }
        bankOffset = battleSpriteModelAddress.getBankOffset();
        for (int i = 0; i < indexes.length; i++) {
            char modelIndex = indexes[i];
            if (modelIndex == 0xFFFF) {
                getBattleSpriteModels().add(BattleSpriteModel.createEmptyModel());
                continue;
            }
            int position = modelIndex - 0x8000 - bankOffset;
            position += battleSpriteModelAddress.getAbsStartAddress(getBuffer());
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
        // 溢出标志
        boolean overflow = false;

        DataAddress spriteModelIndexAddress = getDataAddress(SPRITE_MODEL_INDEX_ADDRESS);
        char[] indexes = new char[spriteModelIndexAddress.length() / 0x02];

        // 写入模型数据
        DataAddress spriteModelAddress = getDataAddress(SPRITE_MODEL_ADDRESS);
        int baseSpriteModelIndex = 0x8000 + spriteModelAddress.getBankOffset();
        position(spriteModelAddress);
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = (char) baseSpriteModelIndex;

            SpriteModel spriteModel = getSpriteModel(i);
            overflow = overflow || !spriteModelAddress.range(position() - 0x10 + spriteModel.length());
            if (overflow) {
                // 写不下了
                LOGGER.error("精灵模型编辑器：精灵模型数据溢出 模型ID：{} 长度为：{}", "%02X".formatted(i), spriteModel.length());
                continue;
            }
            getBuffer().put(spriteModel.getHead());
            getBuffer().put(spriteModel.getAttribute());
            getBuffer().put(spriteModel.getModel());

            baseSpriteModelIndex += spriteModel.length();
        }

        // 写入模型索引
        position(spriteModelIndexAddress);
        for (char index : indexes) {
            getBuffer().putChar(NumberR.toChar(index));
        }

        DataAddress systemSpriteModelIndexAddress = getDataAddress(SYSTEM_SPRITE_MODEL_INDEX_ADDRESS);
        indexes = new char[systemSpriteModelIndexAddress.length() / 0x02];
        // 写入系统精灵模型数据
        DataAddress systemSpriteModelAddress = getDataAddress(SYSTEM_SPRITE_MODEL_ADDRESS);
        baseSpriteModelIndex = 0x8000 + systemSpriteModelAddress.getBankOffset();
        position(systemSpriteModelAddress);
        overflow = false;
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = (char) baseSpriteModelIndex;

            List<SystemSprite> systemSpriteModel = getSystemSpriteModel(i);

            overflow = overflow || !systemSpriteModelAddress.range(position() - 0x10 + (systemSpriteModel.size() * 0x04));
            if (overflow) {
                // 写不下了
                LOGGER.error("精灵模型编辑器：系统精灵模型数据溢出 模型ID：{} 长度为：{}", "%02X".formatted(i), systemSpriteModel.size() * 0x04);
                continue;
            }
            // 数量
            getBuffer().put(systemSpriteModel.size());
            for (SystemSprite systemSprite : systemSpriteModel) {
                getBuffer().put(systemSprite.toArrayByte());
            }
            baseSpriteModelIndex += 1 + (systemSpriteModel.size() * 0x04);
        }

        // 写入模型索引
        position(systemSpriteModelIndexAddress);
        for (char index : indexes) {
            getBuffer().putChar(NumberR.toChar(index));
        }

        // 写入战斗精灵模型数据
        DataAddress battleSpriteModelIndexAddress = getDataAddress(BATTLE_SPRITE_MODEL_INDEX_ADDRESS);
        DataAddress battleSpriteModelAttributeAddress = getDataAddress(BATTLE_SPRITE_MODEL_ATTRIBUTE_ADDRESS);
        DataAddress battleSpriteModelAddress = getDataAddress(BATTLE_SPRITE_MODEL_ADDRESS);

        indexes = new char[battleSpriteModelIndexAddress.length() / 0x02];
        byte[] attributes = new byte[battleSpriteModelAttributeAddress.length()];
        baseSpriteModelIndex = 0x8000 + battleSpriteModelAddress.getBankOffset();
        position(battleSpriteModelAddress);
        overflow = false;
        for (int i = 0; i < indexes.length; i++) {
            BattleSpriteModel battleSpriteModel = getBattleSpriteModels().get(i);
            if (battleSpriteModel.isEmptyModel()) {
                indexes[i] = 0xFFFF;
                continue;
            }
            indexes[i] = (char) baseSpriteModelIndex;

            overflow = overflow || !battleSpriteModelAddress.range(position() - 0x10 + battleSpriteModel.length());
            if (overflow) {
                // 写不下了
                LOGGER.error("精灵模型编辑器：战斗精灵模型数据溢出 模型ID：{} 长度为：{}", "%02X".formatted(i), battleSpriteModel.length());
                continue;
            }
            attributes[i] = battleSpriteModel.getAttribute();
            getBuffer().put(battleSpriteModel.getOffset());
            getBuffer().put(battleSpriteModel.getModel());
            baseSpriteModelIndex += 1 + (battleSpriteModel.getWidth() * battleSpriteModel.getHeight());
        }

        // 写入战斗精灵模型属性
        position(battleSpriteModelAttributeAddress);
        getBuffer().put(attributes);
        // 写入战斗精灵模型索引
        position(battleSpriteModelIndexAddress);
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
}
