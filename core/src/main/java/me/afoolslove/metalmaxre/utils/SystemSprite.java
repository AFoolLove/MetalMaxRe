package me.afoolslove.metalmaxre.utils;

/**
 * 系统内存精灵格式
 *
 * @author AFoolLove
 */
public class SystemSprite extends Point2B {
    public static final int VERTICAL_FLIP = 0B1000_0000;
    public static final int HORIZONTAL_FLIP = 0B0100_0000;
    public static final int PALETTE_INDEX = 0B0000_0011;

    private byte tile;
    private byte attribute;

    public SystemSprite() {
        this(0, 0, 0, 0);
    }

    public SystemSprite(byte x, byte y, byte tile, byte attribute) {
        set(x, y);
        setTile(tile);
        setAttribute(attribute);
    }

    public SystemSprite(int x, int y, int tile, int attribute) {
        set(x, y);
        setTile(tile);
        setAttribute(attribute);
    }


    @Override
    public byte getY() {
        return (byte) (getRawY() + 1);
    }

    public byte getRawY() {
        return super.getY();
    }

    @Override
    public int intY() {
        return (intRawY() + 1) & 0xFF;
    }

    public int intRawY() {
        return super.intY();
    }

    @Override
    public byte getX() {
        return getRawX();
    }

    public byte getRawX() {
        return super.getX();
    }

    public int intX() {
        return intRawX() & 0xFF;
    }

    public int intRawX() {
        return super.getX();
    }

    public byte getTile() {
        return tile;
    }

    public int intTile() {
        return getTile() & 0xFF;
    }

    public byte getAttribute() {
        return attribute;
    }

    public int intAttribute() {
        return getAttribute() & 0xFF;
    }

    public boolean isVerticalFlip() {
        return (getAttribute() & VERTICAL_FLIP) != 0;
    }

    public boolean isHorizontalFlip() {
        return (getAttribute() & HORIZONTAL_FLIP) != 0;
    }

    public int getPaletteIndex() {
        return getAttribute() & PALETTE_INDEX;
    }

    /**
     * 设置垂直翻转
     */
    public SystemSprite setVerticalFlip(boolean verticalFlip) {
        byte attribute = getAttribute();
        attribute &= (byte) ~VERTICAL_FLIP;
        if (verticalFlip) {
            attribute |= (byte) VERTICAL_FLIP;
        }
        setAttribute(attribute);
        return this;
    }

    /**
     * 设置水平翻转
     */
    public SystemSprite setHorizontalFlip(boolean horizontalFlip) {
        byte attribute = getAttribute();
        attribute &= (byte) ~HORIZONTAL_FLIP;
        if (horizontalFlip) {
            attribute |= HORIZONTAL_FLIP;
        }
        setAttribute(attribute);
        return this;
    }

    /**
     * 设置调色板索引
     */
    public SystemSprite setPaletteIndex(int paletteIndex) {
        byte attribute = getAttribute();
        attribute &= (byte) ~PALETTE_INDEX;
        attribute |= (byte) (paletteIndex & PALETTE_INDEX);
        setAttribute(attribute);
        return this;
    }


    public SystemSprite setTile(byte tile) {
        this.tile = tile;
        return this;
    }

    public SystemSprite setTile(int tile) {
        return setTile((byte) (tile & 0xFF));
    }

    public SystemSprite setAttribute(byte attribute) {
        this.attribute = attribute;
        return this;
    }

    public SystemSprite setAttribute(int attribute) {
        return setAttribute((byte) (attribute & 0xFF));
    }

    public byte[] toArrayByte() {
        byte[] bytes = new byte[0x04];
        bytes[0x00] = getRawY();
        bytes[0x01] = getTile();
        bytes[0x02] = getAttribute();
        bytes[0x03] = getRawX();
        return bytes;
    }

    @Override
    public String toString() {
        return "%s tile=%02X, attribute=%02X".formatted(super.toString(), intTile(), intAttribute());
    }
}
