package me.afoolslove.metalmaxre.editors.text.action;

import me.afoolslove.metalmaxre.editors.text.IBaseText;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * 会话时操作目标精灵
 * <p>
 * *一定需要目标精灵，否则会因为找不到目标精灵一直等待（假死）
 */
public class SpriteAction implements IBaseText {
    /**
     * 精灵面向上
     */
    public static final SpriteAction LOCK_UP = new SpriteAction((byte) 0x0C);
    /**
     * 精灵面向下
     */
    public static final SpriteAction LOCK_DOWN = new SpriteAction((byte) 0x0D);
    /**
     * 精灵面向左
     */
    public static final SpriteAction LOCK_LEFT = new SpriteAction((byte) 0x0E);
    /**
     * 精灵面向右
     */
    public static final SpriteAction LOCK_RIGHT = new SpriteAction((byte) 0x0F);
    /**
     * 精灵向上移动1格
     */
    public static final SpriteAction MOVE_UP = new SpriteAction((byte) 0x12);
    /**
     * 精灵向下移动1格
     */
    public static final SpriteAction MOVE_DOWN = new SpriteAction((byte) 0x13);
    /**
     * 精灵向左移动1格
     */
    public static final SpriteAction MOVE_LEFT = new SpriteAction((byte) 0x14);
    /**
     * 精灵向右移动1格
     */
    public static final SpriteAction MOVE_RIGHT = new SpriteAction((byte) 0x15);

    private final byte[] value = {(byte) 0xF5, 0x00};

    public SpriteAction(byte action) {
        setAction(action);
    }

    /**
     * 获取精灵的操作
     *
     * @return 精灵的操作
     */
    public byte getAction() {
        return value[0x01];
    }

    /**
     * 设置精灵的操作
     *
     * @param action 精灵的操作方式
     */
    public void setAction(byte action) {
        value[0x01] = action;
    }

    /**
     * 设置精灵的操作
     *
     * @param action 精灵的操作方式
     */
    public void setAction(@NotNull SpriteAction action) {
        value[0x01] = action.getAction();
    }

    public byte[] toByteArray() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public String toText() {
        return String.format("[%02X%02X]", value[0], value[1]);
    }

    @Override
    public int length() {
        return 2;
    }

    @Override
    public String toString() {
        if (this == LOCK_UP || getAction() == LOCK_UP.getAction()) {
            return "Sprite LOCK_UP";
        } else if (this == LOCK_DOWN || getAction() == LOCK_DOWN.getAction()) {
            return "Sprite LOCK_DOWN";
        } else if (this == LOCK_LEFT || getAction() == LOCK_LEFT.getAction()) {
            return "Sprite LOCK_LEFT";
        } else if (this == LOCK_RIGHT || getAction() == LOCK_RIGHT.getAction()) {
            return "Sprite LOCK_RIGHT";
        } else if (this == MOVE_UP || getAction() == MOVE_UP.getAction()) {
            return "Sprite MOVE_UP";
        } else if (this == MOVE_DOWN || getAction() == MOVE_DOWN.getAction()) {
            return "Sprite MOVE_DOWN";
        } else if (this == MOVE_LEFT || getAction() == MOVE_LEFT.getAction()) {
            return "Sprite MOVE_LEFT";
        } else if (this == MOVE_RIGHT || getAction() == MOVE_RIGHT.getAction()) {
            return "Sprite MOVE_RIGHT";
        } else {
            return String.format("Sprite %02X", getAction());
        }
    }
}
