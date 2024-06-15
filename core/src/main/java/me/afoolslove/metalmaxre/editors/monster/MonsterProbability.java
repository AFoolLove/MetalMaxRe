package me.afoolslove.metalmaxre.editors.monster;

import org.jetbrains.annotations.Range;

/**
 * 出现怪物时，怪物的权重、数量、第二第三组怪物出现概率等
 *
 * @author AFoolLove
 */
public class MonsterProbability {
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
    public byte[] numbers = new byte[0x0E - 0x04];


    public void setWeights(byte[] weights) {
        this.weights = weights;
    }

    public void setNumbers(byte[] numbers) {
        this.numbers = numbers;
    }

    public byte[] getWeights() {
        return weights;
    }

    public byte[] getNumbers() {
        return numbers;
    }

    public byte getWeight(@Range(from = 0x00, to = 0x0E) int index) {
        return weights[index];
    }

    public void setWeight(@Range(from = 0x00, to = 0x0E) int index,
                          @Range(from = 0x00, to = 0xFF) int weight) {
        this.weights[index] = (byte) weight;
    }

    public byte getNumber(@Range(from = 0x00, to = 0x0E - 0x04) int index) {
        return numbers[index];
    }

    @Range(from = 0x00, to = 0x0F)
    public int getMinNumber(@Range(from = 0x00, to = 0x0E - 0x04) int index) {
        return (numbers[index] & 0xF0) >>> 4;
    }

    @Range(from = 0x00, to = 0x0F)
    public int getMaxNumber(@Range(from = 0x00, to = 0x0E - 0x04) int index) {
        return numbers[index] & 0x0F;
    }


    public void setNumber(@Range(from = 0x00, to = 0x0E - 0x04) int index,
                          @Range(from = 0x00, to = 0x0F) int number) {
        numbers[index] = (byte) (number & 0xFF);
    }

    public void setMinNumber(@Range(from = 0x00, to = 0x0E - 0x04) int index,
                             @Range(from = 0x00, to = 0x0F) int number) {
        number &= 0x0F;
        number <<= 4;

        numbers[index] &= 0x0F;
        numbers[index] |= number;
    }

    public void setMaxNumber(@Range(from = 0x00, to = 0x0E - 0x04) int index,
                             @Range(from = 0x00, to = 0x0F) int number) {
        number &= 0x0F;
        numbers[index] &= 0xF0;
        numbers[index] |= number;
    }

    /**
     * 计算权重之和
     * <p>
     * 无论是否有怪物都会算作有效权重
     */
    public int getWeightSum() {
        int sum = 0;
        for (byte weight : weights) {
            sum += weight;
        }
        return sum;
    }

    /**
     * 计算权重之和
     * <p>
     * 根据是否存在怪物，计算有效权重
     */
    public int getWeightSum(byte[] monsters) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            if (monsters[i] == 0x00) {
                continue;
            }
            sum += weights[i];
        }
        return sum;
    }

    /**
     * 通过怪物的权重获取出现概率
     *
     * @param monsters 怪物
     * @param index    需要获取概率怪物索引
     * @return 怪物出现概率，0~1
     */
    public float getWeightProbability(byte[] monsters, @Range(from = 0x00, to = 0x0E) int index) {
        int weightSum = getWeightSum(monsters);
        int weight = getWeight(index) & 0xFF;
        return (1.F / weightSum) * weight;
    }
}
