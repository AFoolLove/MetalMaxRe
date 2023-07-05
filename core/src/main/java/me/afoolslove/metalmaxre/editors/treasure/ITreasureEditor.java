package me.afoolslove.metalmaxre.editors.treasure;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;
import java.util.Map;

/**
 * 宝藏编辑器
 *
 * @author AFoolLove
 */
public interface ITreasureEditor extends IRomEditor {
    @Override
    default String getId() {
        return "treasureEditor";
    }

    /**
     * 获取所有宝藏的最大数量
     *
     * @return 所有宝藏的最大数量
     */
    default int getTreasureMaxCount() {
        return 0x5B;
    }

    /**
     * 获取世界地图随机调查获取的宝藏类型最大数量
     *
     * @return 世界地图随机调查获取的宝藏类型最大数量
     */
    default int getRandomTreasureMaxCount() {
        return 0x06;
    }

    /**
     * 获取所有地图调查点的最大数量
     *
     * @return 所有地图调查点的最大数量
     */
    default int getCheckPointMaxCount() {
        return 0x06;
    }

    /**
     * 获取所有宝藏
     *
     * @return 所有宝藏
     */
    List<Treasure> getTreasures();

    /**
     * 随机进行调查获取的宝藏和宝藏的概率
     * <p>
     * * 仅世界地图
     *
     * @return 随机宝藏和概率
     */
    List<? extends Map.Entry<Byte, Byte>> getRandomTreasures();

    /**
     * 随机进行调查获取的默认宝藏和获取其它宝藏的概率（仅世界地图）
     * <p>
     * K：默认宝藏ID
     * <p>
     * V：其它宝藏的概率（仅世界地图）
     *
     * @return 默认和其它宝藏的概率
     */
    Map.Entry<Byte, Byte> getDefaultRandomTreasure();

    /**
     * 随机宝藏相关数据
     *
     * @return 随机宝藏相关数据
     */
    DataAddress getRandomTreasureAddress();

    /**
     * 特殊调查点
     *
     * @return 特殊调查点
     */
    DataAddress getCheckPointsAddress();

    /**
     * 宝藏数据地址
     *
     * @return 宝藏数据地址
     */
    DataAddress getTreasureAddress();
}
