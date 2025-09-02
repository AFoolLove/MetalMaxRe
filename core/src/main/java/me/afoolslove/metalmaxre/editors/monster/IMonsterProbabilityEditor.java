package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.editors.IRomEditor;

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
}
