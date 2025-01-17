package me.afoolslove.metalmaxre.editors.map.tileset;


/**
 * 图块组合集合
 * <p>
 * 包含0x40(64)个图块
 *
 * @author AFoolLove
 */
public class TileCombinationSet {
    public static final int TILE_COMBINATIONS_LENGTH = 0x40;

    private final TileCombination[] combinations;

    public TileCombinationSet() {
        this(new TileCombination[TILE_COMBINATIONS_LENGTH]);
    }

    public TileCombinationSet(TileCombination[] combinations) {
        this.combinations = combinations;
    }

    public TileCombination[] getCombinations() {
        return combinations;
    }

    public TileCombination getCombination(int tileId) {
        return combinations[tileId & 0x3F];
    }

    public TileCombination getCombination(byte tileId) {
        return combinations[tileId & 0x3F];
    }

    /**
     * 将null值填充为默认数据
     * <p>
     * 保留原始数据
     */
    public void fill() {
        for (int i = 0; i < TILE_COMBINATIONS_LENGTH; i++) {
            TileCombination combination = this.combinations[i];
            if (combination == null) {
                this.combinations[i] = new TileCombination();
            }
        }
    }

    /**
     * 将所有数据设置为初始值
     * <p>
     * 不保留数据
     */
    public void fillZero() {
        for (int i = 0; i < TILE_COMBINATIONS_LENGTH; i++) {
            TileCombination combination = this.combinations[i];
            if (combination == null) {
                this.combinations[i] = new TileCombination();
            } else {
                combination.zero();
            }
        }
    }
}
