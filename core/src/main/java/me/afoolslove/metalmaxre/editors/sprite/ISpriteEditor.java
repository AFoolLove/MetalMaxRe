package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;
import java.util.Map;

public interface ISpriteEditor extends IRomEditor {
    /**
     * @return 获取所有地图的精灵
     */
    Map<Integer, List<Sprite>> getSprites();

    /**
     * @return 获取世界地图的精灵
     */
    List<Sprite> getWorldSprites();

    /**
     * @return 获取精灵数据的索引
     */
    DataAddress getSpriteIndexAddress();

    /**
     * @return 获取精灵的属性数据
     */
    DataAddress getSpriteAddress();
}
