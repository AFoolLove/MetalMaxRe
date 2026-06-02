package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.SystemSprite;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 精灵模型编辑器
 *
 * @author AFoolLove
 */
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


    private final Map<Integer, SpriteModel> spriteModels = new HashMap<>();
    private final Map<Integer, SystemSpriteModel> systemSpriteModels = new HashMap<>();
    private final Map<Integer, BattleSpriteModel> battleSpriteModels = new HashMap<>();

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
        super(metalMaxRe, true);
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

        // 获取特殊精灵模型数据
        Map<Integer, byte[]> spriteModelData = new HashMap<>();
        loadIndexedData(SPRITE_MODEL_INDEX_ADDRESS, SPRITE_MODEL_ADDRESS, spriteModelData, 0x45);
        for (Map.Entry<Integer, byte[]> entry : spriteModelData.entrySet()) {
            byte[] value = entry.getValue();
            int head = value[0x00];
            int attribute = value[0x01];
            SpriteModel spriteModel = new SpriteModel(head, attribute, null);

            byte[] model = new byte[spriteModel.modelLength()];
            System.arraycopy(value, 1 + 1, model, 0, model.length);
            spriteModel.setModel(model);

            getSpriteModels().put(entry.getKey(), spriteModel);
        }

        // 获取系统精灵模型数据
        Map<Integer, byte[]> systemSpriteModelData = new HashMap<>();
        loadIndexedData(SYSTEM_SPRITE_MODEL_INDEX_ADDRESS, SYSTEM_SPRITE_MODEL_ADDRESS, systemSpriteModelData, 0x36);
        for (Map.Entry<Integer, byte[]> entry : systemSpriteModelData.entrySet()) {
            byte[] value = entry.getValue();
            int count = value[0x00];
            SystemSpriteModel systemSpriteModel = new SystemSpriteModel();
            for (int i = 0; i < count; i++) {
                int y = value[i * 0x04 + 1 + 0];
                int tile = value[i * 0x04 + 1 + 1];
                int attribute = value[i * 0x04 + 1 + 2];
                int x = value[i * 0x04 + 1 + 3];
                SystemSprite systemSprite = new SystemSprite(x, y, tile, attribute);
                systemSpriteModel.add(systemSprite);
            }
            getSystemSpriteModels().put(entry.getKey(), systemSpriteModel);
        }

        // 获取战斗精灵模型数据
        Map<Integer, byte[]> battleSpriteModelData = new HashMap<>();
        loadIndexedData(BATTLE_SPRITE_MODEL_INDEX_ADDRESS, BATTLE_SPRITE_MODEL_ADDRESS, battleSpriteModelData, 0xFE);

        DataAddress battleSpriteModelAttributeAddress = getDataAddress(BATTLE_SPRITE_MODEL_ATTRIBUTE_ADDRESS);
        byte[] modelAttributes = new byte[battleSpriteModelAttributeAddress.length()];
        getBuffer().get(battleSpriteModelAttributeAddress, modelAttributes);
        for (Map.Entry<Integer, byte[]> entry : battleSpriteModelData.entrySet()) {
            byte[] value = entry.getValue();
            if (value.length == 0) {
                getBattleSpriteModels().put(entry.getKey(), BattleSpriteModel.createEmptyModel());
                continue;
            }

            BattleSpriteModel battleSpriteModel = new BattleSpriteModel(modelAttributes[entry.getKey()], (byte) 0, null);

            byte offset = value[0x00];
            byte[] model = new byte[battleSpriteModel.getHeight() * battleSpriteModel.getWidth()];
            System.arraycopy(value, 1, model, 0x00, model.length);
            battleSpriteModel.setOffset(offset);
            battleSpriteModel.setModel(model);
            getBattleSpriteModels().put(entry.getKey(), battleSpriteModel);
        }
    }

    @Editor.Apply
    public void onApply() {
        // 写入特殊精灵模型数据
        Map<Integer, byte[]> spriteModelData = new HashMap<>();
        for (Map.Entry<Integer, SpriteModel> entry : getSpriteModels().entrySet()) {
            spriteModelData.put(entry.getKey(), entry.getValue().toByteArray());
        }
        int end = applyIndexedData(SPRITE_MODEL_INDEX_ADDRESS, SPRITE_MODEL_ADDRESS, false, spriteModelData, false, spriteModelData.size());
        if (end >= 0) {
            LOGGER.info("精灵模型编辑器：特殊精灵模型 剩余{}个空闲字节", end);
        } else {
            LOGGER.error("精灵模型编辑器：特殊精灵模型数据溢出！超出了数据上限{}字节", -end);
        }

        // 写入系统精灵模型数据
        Map<Integer, byte[]> systemSpriteModelData = new HashMap<>();
        for (Map.Entry<Integer, SystemSpriteModel> entry : getSystemSpriteModels().entrySet()) {
            systemSpriteModelData.put(entry.getKey(), entry.getValue().toByteArray());
        }
        end = applyIndexedData(SYSTEM_SPRITE_MODEL_INDEX_ADDRESS, SYSTEM_SPRITE_MODEL_ADDRESS, false, systemSpriteModelData, false, systemSpriteModelData.size());
        if (end >= 0) {
            LOGGER.info("精灵模型编辑器：系统精灵模型 剩余{}个空闲字节", end);
        } else {
            LOGGER.error("精灵模型编辑器：系统精灵模型数据溢出！超出了数据上限{}字节", -end);
        }

        // 获取战斗精灵模型数据
        DataAddress battleSpriteModelAttributeAddress = getDataAddress(BATTLE_SPRITE_MODEL_ATTRIBUTE_ADDRESS);
        byte[] modelAttributes = new byte[battleSpriteModelAttributeAddress.length()];

        Map<Integer, byte[]> battleSpriteModelData = new HashMap<>();
        for (Map.Entry<Integer, BattleSpriteModel> entry : getBattleSpriteModels().entrySet()) {
            BattleSpriteModel battleSpriteModel = entry.getValue();
            Integer index = entry.getKey();
            modelAttributes[index] = battleSpriteModel.getAttribute();
            if (battleSpriteModel.isEmptyModel()) {
                battleSpriteModelData.put(index, new byte[0]);
                continue;
            }
            battleSpriteModelData.put(index, battleSpriteModel.toByteArray());
        }
        end = applyIndexedData(BATTLE_SPRITE_MODEL_INDEX_ADDRESS, BATTLE_SPRITE_MODEL_ADDRESS, false, battleSpriteModelData, true, battleSpriteModelData.size());
        if (end >= 0) {
            LOGGER.info("精灵模型编辑器：战斗精灵模型 剩余{}个空闲字节", end);
        } else {
            LOGGER.error("精灵模型编辑器：战斗精灵模型数据溢出！超出了数据上限{}字节", -end);
        }
        // 写入战斗精灵模型属性
        getBuffer().put(battleSpriteModelAttributeAddress, modelAttributes);
    }

    @Override
    public Map<Integer, SpriteModel> getSpriteModels() {
        return spriteModels;
    }

    @Override
    public Map<Integer, SystemSpriteModel> getSystemSpriteModels() {
        return systemSpriteModels;
    }

    @Override
    public Map<Integer, BattleSpriteModel> getBattleSpriteModels() {
        return battleSpriteModels;
    }
}
