package me.afoolslove.metalmaxre.editors.treasure;

import me.afoolslove.metalmaxre.editors.IRomEditor;

import java.util.List;

/**
 * 宝藏编辑器
 *
 * @author AFoolLove
 */
public interface ITreasureEditor extends IRomEditor {
    /**
     * 获取所有宝藏的最大数量
     *
     * @return 所有宝藏的最大数量
     */
    int getTreasureMaxCount();

    /**
     * 获取世界地图随机调查获取的宝藏类型最大数量
     *
     * @return 世界地图随机调查获取的宝藏类型最大数量
     */
    int getRandomTreasureMaxCount();

    /**
     * 获取所有地图调查点的最大数量
     *
     * @return 所有地图调查点的最大数量
     */
    int getCheckPointMaxCount();

    /**
     * 获取所有宝藏
     * @return 所有宝藏
     */
    List<Treasure> getTreasures();


}
