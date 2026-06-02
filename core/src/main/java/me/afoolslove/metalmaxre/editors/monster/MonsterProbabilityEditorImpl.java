package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.AbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

/**
 * 怪物概率编辑器
 *
 * @author AFoolLove
 */
public class MonsterProbabilityEditorImpl extends AbstractEditor implements IMonsterProbabilityEditor {
    /**
     * 怪物权重地址
     */
    public static final String MONSTER_WEIGHTS_ADDRESS = "monsterWeights";
    /**
     * 怪物数量地址
     */
    public static final String MONSTER_NUMBERS_ADDRESS = "monsterNumbers";

    private final MonsterProbability[] monsterProbability = new MonsterProbability[0x04];

    public MonsterProbabilityEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x398C9 - 0x10, 0x39900 - 0x10),
                DataAddress.fromPRG(0x39901 - 0x10, 0x39928 - 0x10)
        );
    }

    public MonsterProbabilityEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                        @NotNull DataAddress monsterWeightsAddress,
                                        @NotNull DataAddress monsterNumbersAddress) {
        super(metalMaxRe);
        putDataAddress(MONSTER_WEIGHTS_ADDRESS, monsterWeightsAddress);
        putDataAddress(MONSTER_NUMBERS_ADDRESS, monsterNumbersAddress);
    }

    @Editor.Load
    public void onLoad() {
        // 初始化
        for (int i = 0; i < monsterProbability.length; i++) {
            monsterProbability[i] = new MonsterProbability();
        }


        // 读取4组不同权重
        byte[][] weights = new byte[0x04][getMonsterWeightCount()];
        getBuffer().getAABytes(getDataAddress(MONSTER_WEIGHTS_ADDRESS), 0, getMonsterWeightCount(), weights);
        for (int i = 0; i < monsterProbability.length; i++) {
            monsterProbability[i].setWeights(weights[i]);
        }

        // 读取4组怪物数量
        byte[][] numbers = new byte[0x04][getMonsterNumberCount()];
        getBuffer().getAABytes(getDataAddress(MONSTER_NUMBERS_ADDRESS), 0, getMonsterNumberCount(), numbers);
        for (int i = 0; i < monsterProbability.length; i++) {
            monsterProbability[i].setNumbers(numbers[i]);
        }

    }

    @Editor.Apply
    public void onApply() {
        // 写入4组不同权重
        byte[][] weights = new byte[0x04][getMonsterWeightCount()];
        for (int i = 0; i < monsterProbability.length; i++) {
            weights[i] = monsterProbability[i].getWeights();
        }
        getBuffer().putAABytes(getDataAddress(MONSTER_WEIGHTS_ADDRESS), 0, getMonsterWeightCount(), weights);

        // 写入4组怪物数量
        byte[][] numbers = new byte[0x04][getMonsterNumberCount()];
        for (int i = 0; i < monsterProbability.length; i++) {
            numbers[i] = monsterProbability[i].getNumbers();
        }
        getBuffer().putAABytes(getDataAddress(MONSTER_NUMBERS_ADDRESS), 0, getMonsterNumberCount(), numbers);

    }

    @Override
    public MonsterProbability[] getMonsterProbability() {
        return monsterProbability;
    }
}
