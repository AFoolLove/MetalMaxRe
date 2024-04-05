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

    public byte getHead() {
        return head;
    }

    public int intHead() {
        return getHead() & 0xFF;
    }

    public byte getAttribute() {
        return attribute;
    }

    public int intAttribute() {
        return getAttribute() & 0xFF;
    }

    public int getPaletteIndex() {
        return getHead() & 0B0000_0011;
    }

    public int getOffsetX() {
        // 0B0001_1100
        return NumberR.at(intHead(), 4, 3, true);
    }

    public int getOffsetY() {
        // 0B1110_0000
        return NumberR.at(intHead(), 7, 3, true);
    }

    public boolean isEvenHorizontalFlip() {
        return (getAttribute() & 0B0000_0001) != 0;
    }

    public boolean isOddHorizontalFlip() {
        return (getAttribute() & 0B0000_0010) != 0;
    }

    public int getWeight() {
        // 0B0001_1100
        return NumberR.at(intAttribute(), 4, 3, true) + 1;
    }

    public int getHeight() {
        // 0B1110_0000
        return NumberR.at(intAttribute(), 7, 3, true);
    }

    public byte[] getModel() {
        return model;
    }

    public int length() {
        return 2 + modelLength();
    }

    public int modelLength() {
        return getWeight() * (getHeight() + 1);
    }

    public void setHead(byte head) {
        this.head = head;
    }

    public void setHead(int head) {
        setHead((byte) (head & 0xFF));
    }

    public void setAttribute(byte attribute) {
        this.attribute = attribute;
    }

    public void setAttribute(int attribute) {
        setAttribute((byte) (attribute & 0xFF));
    }

    public void setPaletteIndex(int paletteIndex) {
        paletteIndex &= 0B0000_0011;

        byte head = getHead();
        head &= (byte) 0B1111_1100;

        head |= (byte) paletteIndex;
        setHead(head);
    }

    public void setOffsetX(int offsetX) {
        offsetX &= 0B0000_0111;
        offsetX <<= 2;

        byte head = getHead();
        head &= (byte) 0B1110_0011;

        head |= (byte) offsetX;
        setHead(head);
    }

    public void setOffsetY(int offsetY) {
        offsetY &= 0B0000_0111;
        offsetY <<= 5;

        byte head = getHead();
        head &= (byte) 0B0001_1111;

        head |= (byte) offsetY;
        setHead(head);
    }

    public void setEvenHorizontalFlip(boolean evenHorizontalFlip) {
        byte attribute = getAttribute();
        attribute &= (byte) 0B1111_1110;

        if (evenHorizontalFlip) {
            attribute |= (byte) 0B0000_0001;
        }
        setAttribute(attribute);
    }

    public void setOddHorizontalFlip(boolean oddHorizontalFlip) {
        byte attribute = getAttribute();
        attribute &= (byte) 0B1111_1101;

        if (oddHorizontalFlip) {
            attribute |= (byte) 0B0000_0010;
        }
        setAttribute(attribute);
    }

    public void setHorizontalFlip(boolean evenHorizontalFlip, boolean oddHorizontalFlip) {
        setEvenHorizontalFlip(evenHorizontalFlip);
        setOddHorizontalFlip(oddHorizontalFlip);
    }

    public void setWeight(int weight) {
        weight &= 0B0000_0111;
        weight <<= 2;

        byte attribute = getAttribute();
        attribute &= (byte) 0B1110_0011;

        attribute |= (byte) weight;
        setAttribute(attribute);
    }

    public void setHeight(int height) {
        height &= 0B0000_0111;
        height <<= 5;

        byte attribute = getAttribute();
        attribute &= (byte) 0B0001_1111;

        attribute |= (byte) height;
        setAttribute(attribute);
    }

    public void setModel(byte[] model) {
        this.model = model;
    }

    public void setModel(int weight, int height, byte[] model) {
        setWeight(weight);
        setHeight(height);
        setModel(model);
    }
}
