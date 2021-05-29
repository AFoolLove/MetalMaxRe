package me.afoolslove.metalmaxre.editor.sprite;

import java.util.Objects;

/**
 * 精灵
 * <p>
 * 精灵最大X、Y坐标为 0x3F(63)
 *
 * @author AFoolLove
 */
public class Sprite {
    /**
     * Y:可以被玩家推动
     */
    public static final int FLAG_CAN_PUSHED = 0B1000_0000;
    /**
     * Y:精灵可以无视地形移动（如果同时拥有 #FLAG_IGNORE_TERRAIN 和 #FLAG_CAN_PUSHED ，那么就可以被玩家推到墙里）
     */
    public static final int FLAG_IGNORE_TERRAIN = 0B0100_0000;
    /**
     * X:精灵始终朝着初始的方向
     */
    public static final int FLAG_LOCK_DIRECTION = 0B1000_0000;
    /**
     * X:精灵移动时不会播放移动时的动画
     */
    public static final int FLAG_DISABLE_MOVING_ANIM = 0B0100_0000;

    public byte type;
    public byte x, y;
    public byte talk1, talk2;
    public byte action;

    public Sprite() {
    }

    public Sprite(byte type, byte x, byte y, byte talk1, byte talk2, byte action) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.talk1 = talk1;
        this.talk2 = talk2;
        this.action = action;
    }

    public Sprite(int type, int x, int y, int talk1, int talk2, int action) {
        this.type = (byte) type;
        this.x = (byte) x;
        this.y = (byte) y;
        this.talk1 = (byte) talk1;
        this.talk2 = (byte) talk2;
        this.action = (byte) action;
    }

    /**
     * 获取精灵的X坐标
     *
     * @return 精灵的X坐标，其中还包含了完整属性
     */
    public byte getRawX() {
        return x;
    }

    /**
     * 获取精灵的X坐标
     *
     * @return 精灵的X坐标，其中不包含任何属性
     */
    public byte getX() {
        return (byte) (x & 0x3F);
    }

    /**
     * 获取精灵的Y坐标
     *
     * @return 精灵的Y坐标，其中还包含了完整属性
     */
    public byte getRawY() {
        return y;
    }

    /**
     * 获取精灵的Y坐标
     *
     * @return 精灵的Y坐标，其中不包含任何属性
     */
    public byte getY() {
        return (byte) (y & 0x3F);
    }

    public void setTalk(int talk1, int talk2) {
        this.talk1 = (byte) talk1;
        this.talk2 = (byte) talk2;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public byte[] toArray() {
        return new byte[]{type, x, y, talk1, talk2, action};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sprite)) {
            return false;
        }
        Sprite sprite = (Sprite) o;
        return type == sprite.type
                && getX() == sprite.getX()
                && getY() == sprite.getY()
                && talk1 == sprite.talk1
                && talk2 == sprite.talk2
                && action == sprite.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, getX(), getY(), talk1, talk2, action);
    }

    @Override
    public String toString() {
        return String.format("Sprite{type=%02X, x=%02X, y=%02X, talk1=%02X, talk2=%02X, action=%02X}", type, x, y, talk1, talk2, action);
    }
}
