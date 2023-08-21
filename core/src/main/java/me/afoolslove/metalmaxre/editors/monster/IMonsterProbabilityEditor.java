package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

/**
 * 怪物概率编辑器
 *
 * @author AFoolLove
 */
public interface IMonsterProbabilityEditor extends IRomEditor {
    @Override
    default String getId() {
        return "monsterProbability";
    }

    /**
     * 每组权重的数量
     */
    default int getMonsterWeightCount() {
        return 0x0E;
    }

    /**
     * 每组数量的数量
     */
    default int getMonsterNumberCount() {
        return 0x0A;
    }

    /**
     * 获取4组概率
     */
    MonsterProbability[] getMonsterProbability();

    /**
     * 怪物权重地址
     */
    DataAddress getMonsterWeightsAddress();

    /**
     * 怪物数量地址
     */
    DataAddress getMonsterNumbersAddress();
}
