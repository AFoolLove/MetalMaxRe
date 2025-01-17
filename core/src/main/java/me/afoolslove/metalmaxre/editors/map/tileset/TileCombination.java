package me.afoolslove.metalmaxre.editors.map.tileset;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * 将4个tile（储存tile ID）拼凑为一个地图图块
 * <p>
 * 占用4字节
 *
 * @author AFoolLove
 */
public class TileCombination {
    private final byte[] combinationData;

    public TileCombination() {
        this.combinationData = new byte[0x04];
    }

    public TileCombination(byte[] combinationData) {
        this();
        setCombinationData(combinationData);
    }

    public TileCombination(@NotNull TileCombination tileCombination, boolean copy) {
        this(copy ? tileCombination.combinationData.clone() : tileCombination.combinationData);
    }

    public TileCombination(@NotNull TileCombination tileCombination) {
        this(tileCombination, true);
    }

    public byte[] getCombinationData() {
        return combinationData;
    }

    public void getCombinationData(byte[] tileData, int offset, int length) {
        // 确认length不会超过数组长度
        length = Math.min(length, 0x10);
        length = Math.min(length, tileData.length - offset);
        System.arraycopy(tileData, offset, this.combinationData, 0, length);
    }

    /**
     * 设置组合数据
     *
     * @param tileData
     * @param offset
     */
    public void getCombinationData(byte[] tileData, int offset) {
        int length = Math.min(tileData.length - offset, 0x10);
        getCombinationData(tileData, offset, length);
    }

    /**
     * 设置组合数据
     *
     * @param combinationData 组合数据
     */
    public void setCombinationData(byte[] combinationData) {
        getCombinationData(combinationData, 0);
    }

    /**
     * 将组合数据清零
     */
    public void zero() {
        Arrays.fill(this.combinationData, (byte) 0x00);
    }
}
