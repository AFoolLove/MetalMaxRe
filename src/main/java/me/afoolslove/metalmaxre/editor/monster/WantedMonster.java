package me.afoolslove.metalmaxre.editor.monster;

import me.afoolslove.metalmaxre.DataValues;

/**
 * 被通缉的怪物
 *
 * @author AFoolLove
 */
public class WantedMonster extends Monster {
    /**
     * 赏金
     *
     * @see DataValues#VALUES
     */
    public byte bounty;

    /**
     * 设置赏金
     *
     * @param bounty 赏金，请使用 {@link DataValues#VALUES} 的索引
     * @see DataValues#VALUES
     * @see DataValues#get3ByteValue()
     */
    public void setBounty(byte bounty) {
        this.bounty = bounty;
    }

    /**
     * 赏金
     *
     * @return 赏金索引值，可以通过{@link DataValues#VALUES}获取真实数值
     * @see DataValues#VALUES
     * @see #getBountyValue()
     */
    public byte getBounty() {
        return bounty;
    }

    /**
     * @return 真实的赏金数值
     * @see DataValues#VALUES
     */
    public int getBountyValue() {
        return DataValues.VALUES.get(bounty & 0xFF);
    }

}
