package me.afoolslove.metalmaxre.editor.text.option;

/**
 * 对话时，对精灵的操作
 *
 * *必须有被对话的精灵，否则卡死
 */
public enum SpriteAction  {

    LOCK_UP(bytes((byte) 0xF5, (byte) 0x0C)),
    LOCK_DOWN(bytes((byte) 0xF5, (byte) 0x0D)),
    LOCK_LEFT(bytes((byte) 0xF5, (byte) 0x0E)),
    LOCK_RIGHT(bytes((byte) 0xF5, (byte) 0x0F)),

    MOVE_UP(bytes((byte) 0xF5, (byte) 0x12)),
    MOVE_DOWN(bytes((byte) 0xF5, (byte) 0x13)),
    MOVE_LEFT(bytes((byte) 0xF5, (byte) 0x14)),
    MOVE_RIGHT(bytes((byte) 0xF5, (byte) 0x15))

    ;

    private final byte[] value;

    SpriteAction(byte[] value) {
        this.value = value;
    }

    public byte[] toByteArray() {
        return value;
    }

    private static byte[] bytes(byte... bytes) {
        return bytes;
    }
}
