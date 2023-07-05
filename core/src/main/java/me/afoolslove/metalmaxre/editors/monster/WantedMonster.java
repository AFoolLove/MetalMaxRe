package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;

/**
 * 被通缉的怪物
 *
 * @author AFoolLove
 */
public class WantedMonster extends Monster {
    /**
     * 赏金
     *
     * @see IDataValueEditor#getValues()
     * @see IDataValueEditor#getValue(int)
     */
    public byte bounty;

    /**
     * 设置赏金
     *
     * @param bounty 赏金，请使用 {@link IDataValueEditor#getValues()} 的索引
     * @see IDataValueEditor#getValues()
     * @see IDataValueEditor#getValue(int)
     * @see IDataValueEditor#get3ByteValues()
     */
    public void setBounty(byte bounty) {
        this.bounty = bounty;
    }

    /**
     * 赏金
     *
     * @return 赏金索引值，可以通过{@link IDataValueEditor#getValues()}获取真实数值
     * @see IDataValueEditor#getValues()
     * @see IDataValueEditor#getValue(int)
     * @see #getBountyValue(IDataValueEditor)
     */
    public byte getBounty() {
        return bounty;
    }

    /**
     * @return 真实的赏金数值
     * @see IDataValueEditor#getValues()
     * @see IDataValueEditor#getValue(int)
     */
    public int getBountyValue(IDataValueEditor dataValueEditor) {
        return dataValueEditor.getValue(bounty & 0xFF).intValue();
    }

}
