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

    List<SpriteModel> getSpriteModels();

    default SpriteModel getSpriteModel(int index) {
        return getSpriteModels().get(index);
    }

    DataAddress getSpriteModelIndexAddress();

    DataAddress getSpriteModelAddress();
}
