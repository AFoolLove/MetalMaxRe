package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.editors.IRomEditor;

import java.util.List;
import java.util.Map;

/**
 * 精灵编辑器
 *
 * @author AFoolLove
 */
public interface ISpriteEditor extends IRomEditor {
    @Override
    default String getId() {
        return "spriteEditor";
    }

    /**
     * @return 获取所有地图的精灵
     */
    Map<Integer, List<Sprite>> getSprites();

    /**
     * @return 获取世界地图的精灵
     */
    List<Sprite> getWorldSprites();
}
