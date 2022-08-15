package me.afoolslove.metalmaxre.editors.monster;

import org.jetbrains.annotations.Range;

/**
 * 出现怪物时，怪物的权重、数量、第二第三组怪物出现概率等
 *
 * @author AFoolLove
 */
public enum MonsterProbability {
    /**
     * 概率A
     */
    A,
    /**
     * 概率B
     */
    B,
    /**
     * 概率C
     */
    C,
    /**
     * 概率D
     */
    D;

    /**
     * 怪物的权重，值越高，出现的概率越高
     * 注：所有权重相加不能大于255（可以相等）
     * 0x00-0x0A：普通怪物的权重
     * 0x0B-0x0E：特殊怪物组合的权重
     */
    public byte[] weights = new byte[0x0E];

    /**
     * 怪物出现的最小数量和最大数量
     * D7-D4：最小数量
     * D3-D0：最大数量
     * <p>
     * 不包含特殊怪物组合
     */
    public byte[] counts = new byte[0x0E - 0x04];


    public void setWeights(byte[] weights) {
        this.weights = weights;
    }

    public void setCounts(byte[] counts) {
        this.counts = counts;
    }

    public byte[] getWeights() {
        return weights;
    }

    public byte[] getCounts() {
        return counts;
    }

    public byte getWeight(@Range(from = 0x00, to = 0x0E) int index) {
        return weights[index];
    }

    public void setWeight(@Range(from = 0x00, to = 0x0E) int index,
                          @Range(from = 0x00, to = 0xFF) int weight) {
        this.weights[index] = (byte) weight;
    }

    public byte getCount(@Range(from = 0x00, to = 0x0E - 0x04) int index) {
        return counts[index];
    }

    @Range(from = 0x00, to = 0x0F)
    public int getMinCount(@Range(from = 0x00, to = 0x0E - 0x04) int index) {
        return (counts[index] & 0xF0) >>> 4;
    }

    @Range(from = 0x00, to = 0x0F)
    public int getMaxCount(@Range(from = 0x00, to = 0x0E - 0x04) int index) {
        return counts[index] & 0x0F;
    }


    public void setCount(@Range(from = 0x00, to = 0x0E - 0x04) int index,
                         @Range(from = 0x00, to = 0x0F) int count) {
        counts[index] = (byte) (count & 0xFF);
    }

    public void setMinCount(@Range(from = 0x00, to = 0x0E - 0x04) int index,
                            @Range(from = 0x00, to = 0x0F) int count) {
        count &= 0x0F;
        count <<= 4;

        counts[index] &= 0x0F;
        counts[index] |= count;
    }

    public void setMaxCount(@Range(from = 0x00, to = 0x0E - 0x04) int index,
                            @Range(from = 0x00, to = 0x0F) int count) {
        count &= 0x0F;
        counts[index] &= 0xF0;
        counts[index] |= count;
    }
}
