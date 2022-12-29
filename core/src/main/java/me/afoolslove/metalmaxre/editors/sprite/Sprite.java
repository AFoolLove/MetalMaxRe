package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

public class Sprite extends Point2B {
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

    private byte type; // 精灵的类型
    private byte talk1, talk2; // 精灵的对话
    private byte action; // 精灵的行动方式

    public Sprite() {
        this(0, 0, 0, 0, 0, 0);
    }

    public Sprite(byte x, byte y) {
        super(x, y);
    }

    public Sprite(@Range(from = 0x00, to = 0xFF) int x, @Range(from = 0x00, to = 0xFF) int y) {
        super(x, y);
    }

    public Sprite(@Range(from = 0x00, to = 0xFF) int type,
                  @Range(from = 0x00, to = 0xFF) int x,
                  @Range(from = 0x00, to = 0xFF) int y,
                  @Range(from = 0x00, to = 0xFF) int talk1,
                  @Range(from = 0x00, to = 0xFF) int talk2,
                  @Range(from = 0x00, to = 0xFF) int action) {
        super(x, y);
        this.type = (byte) (type & 0xFF);
        this.talk1 = (byte) (talk1 & 0xFF);
        this.talk2 = (byte) (talk2 & 0xFF);
        this.action = (byte) (action & 0xFF);
    }

    public Sprite(@NotNull Point2B point2B) {
        super(point2B);
    }

    public Sprite(@NotNull Sprite sprite) {
        super(sprite);
        this.type = sprite.type;
        this.talk1 = sprite.talk1;
        this.talk2 = sprite.talk2;
        this.action = sprite.action;
    }

    /**
     * 获取精灵的X坐标
     *
     * @return 精灵的X坐标，其中还包含了完整属性
     */
    public byte getRawX() {
        return super.getX();
    }

    @Range(from = 0x00, to = 0xFF)
    public int intRawX() {
        return super.getX() & 0xFF;
    }

    /**
     * 获取精灵的X坐标
     *
     * @return 精灵的X坐标，其中不包含任何属性
     */
    @Range(from = 0x00, to = 0x3F)
    @Override
    public byte getX() {
        return (byte) (super.getX() & 0x3F);
    }

    @Range(from = 0x00, to = 0xFF)
    @Override
    public int intX() {
        return super.getX() & 0x3F;
    }

    /**
     * 获取精灵的Y坐标
     *
     * @return 精灵的Y坐标，其中还包含了完整属性
     */
    public byte getRawY() {
        return super.getY();
    }

    @Range(from = 0x00, to = 0xFF)
    public int intRawY() {
        return super.getY() & 0xFF;
    }

    /**
     * 获取精灵的Y坐标
     *
     * @return 精灵的Y坐标，其中不包含任何属性
     */
    @Range(from = 0x00, to = 0x3F)
    @Override
    public byte getY() {
        return (byte) (super.getY() & 0x3F);
    }

    @Range(from = 0x00, to = 0x3F)
    @Override
    public int intY() {
        return super.getY() & 0x3F;
    }

    /**
     * 获取精灵的类型
     *
     * @return 精灵的类型
     */
    public byte getType() {
        return type;
    }

    public int intType() {
        return getType() & 0xFF;
    }

    public int getTalk() {
        return NumberR.toInt(true, talk1, talk2);
    }

    public byte getTalk1() {
        return talk1;
    }

    public byte getTalk2() {
        return talk2;
    }

    public byte getAction() {
        return action;
    }

    public int intAction() {
        return getAction() & 0xFF;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setType(@Range(from = 0x00, to = 0xFF) int type) {
        this.type = (byte) (type & 0xFF);
    }

    public void setTalk(byte talk1, byte talk2) {
        this.talk1 = talk1;
        this.talk2 = talk2;
    }

    public void setTalk(@Range(from = 0x00, to = 0xFF) int talk1,
                        @Range(from = 0x00, to = 0xFF) int talk2) {
        this.talk1 = (byte) (talk1 & 0xFF);
        this.talk2 = (byte) (talk2 & 0xFF);
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public void setAction(@Range(from = 0x00, to = 0xFF) int action) {
        this.action = (byte) (action & 0xFF);
    }

    /**
     * 设置是否可以被推动
     */
    public void canPushed(boolean enable) {
        int y = getRawY();
        if (enable) {
            y |= FLAG_CAN_PUSHED;
        } else {
            y &= (0B1111_1111 ^ FLAG_CAN_PUSHED);
        }
        setY(y);
    }

    /**
     * 设置是否可以忽略地形移动
     */
    public void ignoreTerrain(boolean enable) {
        int y = getRawY();
        if (enable) {
            y |= FLAG_IGNORE_TERRAIN;
        } else {
            y &= (0B1111_1111 ^ FLAG_IGNORE_TERRAIN);
        }
        setY(y);
    }

    /**
     * 设置是否始终朝向初始的方向
     */
    public void lockDirection(boolean enable) {
        int x = getRawX();
        if (enable) {
            x |= FLAG_LOCK_DIRECTION;
        } else {
            x &= (0B1111_1111 ^ FLAG_LOCK_DIRECTION);
        }
        setX(x);
    }

    /**
     * 设置移动时是否禁用移动动画
     */
    public void disableMovingAnim(boolean enable) {
        int x = getRawX();
        if (enable) {
            x |= FLAG_DISABLE_MOVING_ANIM;
        } else {
            x &= (0B1111_1111 ^ FLAG_DISABLE_MOVING_ANIM);
        }
        setX(x);
    }

    /**
     * @return 是否可以被玩家推动
     */
    public boolean hasCanPushed() {
        return (getRawY() & FLAG_CAN_PUSHED) == FLAG_CAN_PUSHED;
    }

    /**
     * @return 是否可以忽略地形移动（可以往墙里走
     */
    public boolean hasIgnoreTerrain() {
        return (getRawY() & FLAG_IGNORE_TERRAIN) == FLAG_IGNORE_TERRAIN;
    }

    /**
     * @return 是否始终朝向初始的方向
     */
    public boolean hasLockDirection() {
        return (getRawX() & FLAG_LOCK_DIRECTION) == FLAG_LOCK_DIRECTION;
    }

    /**
     * @return 是否禁用移动时的动画（人偶或假人等使用
     */
    public boolean hasDisableMovingAnim() {
        return (getRawX() & FLAG_DISABLE_MOVING_ANIM) == FLAG_DISABLE_MOVING_ANIM;
    }

    /**
     * @return 转换为数组
     */
    public byte[] toByteArray() {
        return new byte[]{type, getRawX(), getRawY(), talk1, talk2, action};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sprite sprite)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return type == sprite.type
               && talk1 == sprite.talk1
               && talk2 == sprite.talk2
               && action == sprite.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, getRawX(), getRawY(), talk1, talk2, action);
    }

    @Override
    public String toString() {
        return String.format("Sprite{type=%02X, x=%02X, y=%02X, talk1=%02X, talk2=%02X, action=%02X}", type, getRawX(), getRawY(), talk1, talk2, action);
    }
}
