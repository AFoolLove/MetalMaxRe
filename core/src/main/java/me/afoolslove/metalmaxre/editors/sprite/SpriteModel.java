package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.utils.NumberR;

/**
 * 精灵模型
 *
 * @author AFoolLove
 */
public class SpriteModel {
    private byte head;
    private byte attribute;

    private byte[] model;

    public SpriteModel(byte head, byte attribute, byte[] model) {
        setHead(head);
        setAttribute(attribute);
        setModel(model);
    }

    public SpriteModel(int head, int attribute, byte[] model) {
        setHead(head);
        setAttribute(attribute);
        setModel(model);
    }

    /**
     * 获取头数据
     * <p>
     * 包含以下属性
     * <p>
     * 调色板索引
     * <p>
     * X坐标偏移
     * <p>
     * Y坐标偏移
     *
     * @return 头数据
     */
    public byte getHead() {
        return head;
    }

    public int intHead() {
        return getHead() & 0xFF;
    }

    /**
     * 获取属性数据
     * <p>
     * 包含以下数据
     * <p>
     * 偶数图块水平翻转
     * <p>
     * 奇数图块水平翻转
     * <p>
     * 模型宽度
     * <p>
     * 模型高度
     *
     * @return 属性数据
     */
    public byte getAttribute() {
        return attribute;
    }

    public int intAttribute() {
        return getAttribute() & 0xFF;
    }

    /**
     * 获取调色板索引
     *
     * @return 调色板索引
     */
    public int getPaletteIndex() {
        return getHead() & 0B0000_0011;
    }

    /**
     * 获取X坐标偏移
     * <p>
     * 偏移比率为：1:4像素
     *
     * @return X坐标偏移
     */
    public int getOffsetX() {
        // 0B0001_1100
        return NumberR.at(intHead(), 4, 3, true);
    }

    /**
     * 获取Y坐标偏移
     * <p>
     * 偏移比率为：1:4像素
     *
     * @return Y坐标偏移
     */
    public int getOffsetY() {
        // 0B1110_0000
        return NumberR.at(intHead(), 7, 3, true);
    }

    /**
     * 获取偶数图块水平翻转
     *
     * @return 偶数图块水平翻转
     */
    public boolean isEvenHorizontalFlip() {
        return (getAttribute() & 0B0000_0001) != 0;
    }

    /**
     * 获取奇数图块水平翻转
     *
     * @return 奇数图块水平翻转
     */
    public boolean isOddHorizontalFlip() {
        return (getAttribute() & 0B0000_0010) != 0;
    }

    /**
     * 获取模型宽度
     *
     * @return 模型宽度
     */
    public int getWidth() {
        // 0B0001_1100
        return NumberR.at(intAttribute(), 4, 3, true) + 1;
    }

    /**
     * 获取模型高度
     *
     * @return 模型高度
     */
    public int getHeight() {
        // 0B1110_0000
        return NumberR.at(intAttribute(), 7, 3, true) + 1;
    }

    /**
     * 获取模型数据
     *
     * @return 模型数据
     */
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

    /**
     * 设置头数据
     *
     * @param head 头数据
     */
    public void setHead(byte head) {
        this.head = head;
    }

    /**
     * 设置头数据
     *
     * @param head 头数据
     */
    public void setHead(int head) {
        setHead((byte) (head & 0xFF));
    }

    /**
     * 设置属性数据
     *
     * @param attribute 属性数据
     */
    public void setAttribute(byte attribute) {
        this.attribute = attribute;
    }

    /**
     * 设置属性数据
     *
     * @param attribute 属性数据
     */
    public void setAttribute(int attribute) {
        setAttribute((byte) (attribute & 0xFF));
    }

    /**
     * 设置调色板索引
     *
     * @param paletteIndex 调色板索引
     */
    public void setPaletteIndex(int paletteIndex) {
        paletteIndex &= 0B0000_0011;

        byte head = getHead();
        head &= (byte) 0B1111_1100;

        head |= (byte) paletteIndex;
        setHead(head);
    }

    /**
     * 设置X坐标偏移
     * <p>
     * 偏移比率为：1:4像素
     *
     * @param offsetX X坐标偏移
     */
    public void setOffsetX(int offsetX) {
        offsetX &= 0B0000_0111;
        offsetX <<= 2;

        byte head = getHead();
        head &= (byte) 0B1110_0011;

        head |= (byte) offsetX;
        setHead(head);
    }

    /**
     * 设置Y坐标偏移
     * <p>
     * 偏移比率为：1:4像素
     *
     * @param offsetY Y坐标偏移
     */
    public void setOffsetY(int offsetY) {
        offsetY &= 0B0000_0111;
        offsetY <<= 5;

        byte head = getHead();
        head &= (byte) 0B0001_1111;

        head |= (byte) offsetY;
        setHead(head);
    }

    /**
     * 设置坐标偏移
     * <p>
     * 偏移比率为：1:4像素
     *
     * @param offsetX X坐标偏移
     * @param offsetY Y坐标偏移
     */
    public void setOffset(int offsetX, int offsetY) {
        setOffsetX(offsetX);
        setOffsetY(offsetY);
    }

    /**
     * 设置偶数图块水平翻转
     *
     * @param evenHorizontalFlip 偶数图块水平翻转
     */
    public void setEvenHorizontalFlip(boolean evenHorizontalFlip) {
        byte attribute = getAttribute();
        attribute &= (byte) 0B1111_1110;

        if (evenHorizontalFlip) {
            attribute |= (byte) 0B0000_0001;
        }
        setAttribute(attribute);
    }

    /**
     * 设置奇数图块水平翻转
     *
     * @param oddHorizontalFlip 奇数图块水平翻转
     */
    public void setOddHorizontalFlip(boolean oddHorizontalFlip) {
        byte attribute = getAttribute();
        attribute &= (byte) 0B1111_1101;

        if (oddHorizontalFlip) {
            attribute |= (byte) 0B0000_0010;
        }
        setAttribute(attribute);
    }

    /**
     * 设置图块水平翻转
     *
     * @param evenHorizontalFlip 偶数图块水平翻转
     * @param oddHorizontalFlip  奇数图块水平翻转
     */
    public void setHorizontalFlip(boolean evenHorizontalFlip, boolean oddHorizontalFlip) {
        setEvenHorizontalFlip(evenHorizontalFlip);
        setOddHorizontalFlip(oddHorizontalFlip);
    }

    /**
     * 设置模型宽度
     *
     * @param width 模型宽度
     */
    public void setWidth(int width) {
        width = Math.max(1, width);
        width -= 1;

        width &= 0B0000_0111;
        width <<= 2;

        byte attribute = getAttribute();
        attribute &= (byte) 0B1110_0011;

        attribute |= (byte) width;
        setAttribute(attribute);
    }

    /**
     * 设置模型高度
     *
     * @param height 模型高度
     */
    public void setHeight(int height) {
        height = Math.max(1, height);
        height -= 1;

        height &= 0B0000_0111;
        height <<= 5;

        byte attribute = getAttribute();
        attribute &= (byte) 0B0001_1111;

        attribute |= (byte) height;
        setAttribute(attribute);
    }

    /**
     * 设置模型数据
     *
     * @param model 模型数据
     */
    public void setModel(byte[] model) {
        this.model = model;
    }

    /**
     * 设置模型数据
     *
     * @param width  模型宽度
     * @param height 模型高度
     * @param model  模型数据
     */
    public void setModel(int width, int height, byte[] model) {
        setWidth(width);
        setHeight(height);
        setModel(model);
    }
}
