package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;

/**
 * 精灵模型编辑器
 * <p>
 * 如果精灵的显示方式为0x44时使用该模型展示
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
     * 获取精灵模型索引地址
     *
     * @return 精灵模型索引地址
     */
    DataAddress getSpriteModelIndexAddress();

    /**
     * 获取精灵模型数据地址
     *
     * @return 精灵模型数据地址
     */
    DataAddress getSpriteModelAddress();
}
