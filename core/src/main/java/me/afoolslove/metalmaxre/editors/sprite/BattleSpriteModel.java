package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.utils.NumberR;

/**
 * 战斗时的精灵模型
 * <p>
 * 玩家、攻击动画等
 * <p>
 * offset: 0B1111_0000 X偏移、0B0000_1111 Y偏移
 * <p>
 * attribute: 0B0000_0011 调色板索引、0B0001_1100 高、0B1110_0000 宽
 *
 * @author AFoolLove
 */
public class BattleSpriteModel {
    private byte offset;
    private byte attribute;

    private byte[] model;

    public BattleSpriteModel(byte attribute, byte offset, byte[] model) {
        this.attribute = attribute;
        this.offset = offset;
        this.model = model;
    }

    public int getOffsetX() {
        return NumberR.at(offset, 7, 4, true);
    }

    public int getOffsetY() {
        return NumberR.at(offset, 3, 4, true);
    }

    public byte getOffset() {
        return offset;
    }

    public int getWidth() {
        return NumberR.at(attribute, 7, 3, true) + 1;
    }

    public int getHeight() {
        return NumberR.at(attribute, 4, 3, true) + 1;
    }

    public int getPaletteIndex() {
        return NumberR.at(attribute, 7, 3, true);
    }

    public byte getAttribute() {
        return attribute;
    }

    public byte[] getModel() {
        return model;
    }

    /**
     * 获取全部数据长度
     *
     * @return 全部数据长度
     */
    public int length() {
        return 2 + modelLength();
    }

    /**
     * 获取模型数据长度
     *
     * @return 模型数据长度
     */
    public int modelLength() {
        return getWidth() * getHeight();
    }

    public void setOffset(byte offset) {
        this.offset = offset;
    }

    public void setOffsetX(byte offsetX) {
        this.offset = NumberR.set(offset, offsetX, 7, 4);
    }

    public void setOffsetY(byte offsetY) {
        this.offset = NumberR.set(offset, offsetY, 3, 4);
    }

    public void setWidth(byte width) {
        this.attribute = NumberR.set(attribute, (byte) (width - 1), 7, 3);
    }

    public void setHeight(byte height) {
        this.attribute = NumberR.set(attribute, (byte) (height - 1), 4, 3);
    }

    public void setPaletteIndex(byte paletteIndex) {
        this.attribute = NumberR.set(attribute, paletteIndex, 1, 2);
    }

    public void setModel(byte[] model) {
        this.model = model;
    }

    public boolean isEmptyModel() {
        return this instanceof EmptyBattleSpriteModel;
    }

    public static EmptyBattleSpriteModel createEmptyModel() {
        return new EmptyBattleSpriteModel();
    }

    /**
     * 空的战斗精灵模型
     */
    public static class EmptyBattleSpriteModel extends BattleSpriteModel {

        public EmptyBattleSpriteModel() {
            super((byte) 0, (byte) 0, null);
        }
    }
}
