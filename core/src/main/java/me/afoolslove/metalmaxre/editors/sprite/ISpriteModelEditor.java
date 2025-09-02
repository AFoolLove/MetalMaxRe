package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 精灵模型编辑器
 * <p>
 * 如果精灵的显示方式为0x44时使用{@link #getSpriteModels()}模型
 * <p>
 * 如果精灵的显示方式为0x04时使用{@link #getSystemSpriteModels()}模型
 *
 * @author AFoolLove
 */
public interface ISpriteModelEditor extends IRomEditor {
    @Override
    default String getId() {
        return "spriteModelEditor";
    }

    /**
     * 获取所有精灵模型
     *
     * @return 所有精灵模型
     */
    List<SpriteModel> getSpriteModels();

    /**
     * 获取精灵模型
     *
     * @param index 精灵模型索引
     * @return 精灵模型
     */
    default SpriteModel getSpriteModel(int index) {
        return getSpriteModels().get(index);
    }

    /**
     * 获取精灵模型
     *
     * @param sprite 精灵
     * @return 精灵模型
     */
    default SpriteModel getSpriteModel(@NotNull Sprite sprite) {
        if (sprite.intType() < 0x04) {
            // 没图像，不显示
            return null;
        }
        return getSpriteModels().get((sprite.intType() / 0x04) - 1);
    }

    /**
     * 获取所有系统精灵模型
     *
     * @return 系统精灵模型
     */
    List<SystemSpriteModel> getSystemSpriteModels();

    /**
     * 获取系统精灵模型
     *
     * @param index 精灵模型索引
     * @return 系统精灵模型
     */
    default SystemSpriteModel getSystemSpriteModel(int index) {
        return getSystemSpriteModels().get(index);
    }

    /**
     * 获取系统精灵模型
     *
     * @param sprite 精灵
     * @return 系统精灵模型
     */
    default SystemSpriteModel getSystemSpriteModel(@NotNull Sprite sprite) {
        if (sprite.intType() < 0x04) {
            // 没图像，不显示
            return null;
        }
        return getSystemSpriteModels().get((sprite.intType() / 0x04) - 1);
    }

    /**
     * 获取所有战斗精灵模型
     *
     * @return 战斗精灵模型
     */
    List<BattleSpriteModel> getBattleSpriteModels();

    /**
     * 获取战斗精灵模型
     *
     * @param index 精灵模型索引
     * @return 战斗精灵模型
     */
    default BattleSpriteModel getBattleSpriteModel(int index) {
        return getBattleSpriteModels().get(index);
    }
}
