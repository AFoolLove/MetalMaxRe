package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.Point2B;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Objects;

public class Sprite extends Point2B {
    /**
     * Y bit7: 可以被玩家推动
     */
    public static final int FLAG_CAN_PUSHED = 0B1000_0000;
    /**
     * Y bit6: 精灵可以无视地形移动（如果同时拥有 #FLAG_IGNORE_TERRAIN 和 #FLAG_CAN_PUSHED ，那么就可以被被推到墙里）
     */
    public static final int FLAG_IGNORE_TERRAIN = 0B0100_0000;
    /**
     * X bit7: 精灵始终朝着初始的方向
     */
    public static final int FLAG_LOCK_DIRECTION = 0B1000_0000;
    /**
     * X bit6: 精灵移动时不会播放移动时的动画
     */
    public static final int FLAG_DISABLE_MOVING_ANIM = 0B0100_0000;

    /**
     * type低2位掩码：朝向（0-3）
     */
    public static final int TYPE_DIRECTION_MASK = 0B0000_0011;
    /**
     * 朝向：上
     */
    public static final int SPRITE_DIRECTION_UP = 0;
    /**
     * 朝向：下
     */
    public static final int SPRITE_DIRECTION_DOWN = 1;
    /**
     * 朝向：左
     */
    public static final int SPRITE_DIRECTION_LEFT = 2;
    /**
     * 朝向：右
     */
    public static final int SPRITE_DIRECTION_RIGHT = 3;
    /**
     * 精灵模型类型：一般精灵
     */
    public static final int SPRITE_MODEL_SPRITE = 0x00;
    /**
     * 精灵模型类型：特殊精灵
     */
    public static final int SPRITE_MODEL_SPECIAL = 0x01;
    /**
     * 精灵模型类型：系统精灵
     */
    public static final int SPRITE_MODEL_SYSTEM = 0x02;

    /**
     * talk1 bit1-0：精灵模型类型掩码（数据位于 0x347BB-0x347BD）
     */
    public static final int SPRITE_MODEL_MASK = 0B0000_0011;
    /**
     * talk1 bit7：计算机功能模式（该值为1时，使用 0x31341 的数据，D6-D2 为计算机功能参数）
     */
    public static final int COMPUTER_MODE = 0B1000_0000;
    /**
     * talk1 bit7：计算机功能模式，功能类型掩码
     */
    public static final int COMPUTER_MODE_MASK = 0B0111_1100;
    /**
     * talk1 bit6：清单模式（该值为1时，使用 0x23CA5 的数据，D5-D2 为商品类型）
     */
    public static final int LIST_MODE = 0B0100_0000;
    /**
     * talk1 bit6：清单模式，清单类型掩码（该值为1时，使用 0x23CA5 的数据，D5-D2 为商品类型）
     */
    public static final int LIST_MODE_MASK = 0B0011_1100;
    /**
     * talk1 bit5-2：不能为0，任意值为1时为文本页
     */
    public static final int TEXT_MODE = 0B0011_1100;
    /**
     * talk1 bit5-2：不能为0，任意值为1时为文本页
     */
    public static final int TEXT_MODE_MASK = TEXT_MODE;
    /**
     * talk1 bit7-2：全为0时，静态脚本模式
     */
    public static final int STATIC_SCRIPT_MODE_MASK = 0B1111_1100;
    /**
     * talk1 bit7-5：特殊模式
     */
    public static final int MODE_MASK = 0B1110_0000;

    private byte type; // (图像 & 朝向)，图像 = type/0x04，朝向 = type%0x04
    private byte talk1; // 对话/功能控制字节
    private byte talk2; // 对话参数/文本段/计算机编号/商品页
    private byte action; // 行动方式

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
     * 获取精灵的类型原始值（包含图像ID与朝向信息）
     *
     * @return 类型字节
     */
    public byte getType() {
        return type;
    }

    public int intType() {
        return getType() & 0xFF;
    }

    /**
     * 获取精灵的图像ID（type / 0x04）
     *
     * @return 图像ID
     */
    @Range(from = 0x00, to = 0x3F)
    public int getImageId() {
        return intType() >> 2;
    }

    /**
     * 获取精灵的朝向（type % 0x04）：<br>
     * {@link #SPRITE_DIRECTION_UP}=0 上, {@link #SPRITE_DIRECTION_DOWN}=1 下, {@link #SPRITE_DIRECTION_LEFT}=2 左, {@link #SPRITE_DIRECTION_RIGHT}=3 右
     *
     * @return 朝向值 0-3
     */
    @Range(from = SPRITE_DIRECTION_UP, to = SPRITE_DIRECTION_RIGHT)
    public int getDirection() {
        return intType() & TYPE_DIRECTION_MASK;
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

    /**
     * 获取精灵模型类型（talk1 bit1-0）
     * 数据位于 ROM 0x347BB-0x347BD
     *
     * @return 模型类型 0-3
     */
    @Range(from = 0, to = 3)
    public int getSpriteModelType() {
        return getTalk1() & SPRITE_MODEL_MASK;
    }

    /**
     * 判断是否处于计算机功能模式（talk1 bit7 == 1）
     * 使用 ROM 0x31341 的数据，talk2 bit6-bit2 为计算机编号
     */
    public boolean isComputerMode() {
        return (getTalk1() & COMPUTER_MODE) == COMPUTER_MODE;
    }

    /**
     * 判断是否处于清单模式（talk1 bit6 == 1）
     * 使用 ROM 0x23CA5 的数据，talk2 为商品页编号
     */
    public boolean isListMode() {
        return !isComputerMode() && (getTalk1() & LIST_MODE) == LIST_MODE;
    }

    /**
     * 判断是否为普通对话模式（talk1 bit7=0 且 bit6=0）
     * 此时 talk1 bit5-bit2 为文本页码（04-0F），talk2 为文本段号
     */
    public boolean isTextMode() {
        return !isComputerMode() && !isListMode() && (getTalk1() & TEXT_MODE) != 0;
    }

    /**
     * 判断是否为静态脚本模式（talk1 bit7-bit2为0）
     */
    public boolean isStaticScriptMode() {
        return (getTalk1() & STATIC_SCRIPT_MODE_MASK) == 0;
    }

    /**
     * 获取计算机类型（仅当 {@link #isComputerMode()} 为 true 时有效）
     * 取自 talk1
     *
     * @return 计算机类型
     */
    public int getComputerType() {
        return (getTalk1() & COMPUTER_MODE_MASK) >> 2;
    }

    /**
     * 获取计算机编号（仅当 {@link #isComputerMode()} 为 true 时有效）
     * 取自 talk2
     *
     * @return 计算机编号
     */
    public int getComputerIndex() {
        return getTalk2() & 0xFF;
    }

    /**
     * 获取清单类型（仅当 {@link #isListMode()} 为 true 时有效）
     * 取自 talk1
     *
     * @return 商品页编号
     */
    public int getListType() {
        return (getTalk1() & LIST_MODE_MASK) >> 2;
    }

    /**
     * 获取商品页编号（仅当 {@link #isListMode()} 为 true 时有效）
     * 取自 talk2
     *
     * @return 商品页编号
     */
    public int getListPage() {
        return getTalk2() & 0xFF;
    }

    /**
     * 获取文本页码（仅当 {@link #isTextMode()} 为 true 时有效）
     * 取自 talk1 bit5-bit2
     *
     * @return 文本页码，通常范围 01-0F
     */
    @Range(from = 0x01, to = 0x0F)
    public int getTextPage() {
        return (getTalk1() & TEXT_MODE_MASK) >> 2;
    }

    /**
     * 获取对话文本段号（仅当 {@link #isTextMode()} 为 true 时有效）
     * 取自 talk2
     *
     * @return 文本段号
     */
    public int getTextSegment() {
        return getTalk2() & 0xFF;
    }

    /**
     * 获取静态脚本编号（仅当 {@link #isStaticScriptMode()} 为 true 时有效）
     *
     * @return 静态脚本编号
     */
    public int getStaticScript() {
        return getTalk2() & 0xFF;
    }

    public byte getAction() {
        return action;
    }

    public int intAction() {
        return getAction() & 0xFF;
    }

    /**
     * 设置类型原始值
     */
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

    /**
     * 设置计算机功能模式
     *
     * @param computerType  计算机类型
     * @param computerIndex 计算机编号
     */
    public void setComputerMode(@Range(from = 0x00, to = 0x1F) int computerType,
                                @Range(from = 0x00, to = 0xFF) int computerIndex) {
        int talk1 = this.talk1 & ~MODE_MASK;
        talk1 |= COMPUTER_MODE;     // 设置模式
        talk1 |= computerType << 2; // 设置类型
        this.talk1 |= (byte) talk1;
        this.talk2 = (byte) (computerIndex & 0xFF);
    }

    /**
     * 设置清单模式
     *
     * @param listType 清单类型
     * @param listPage 清单页
     */
    public void setListMode(@Range(from = 0x00, to = 0x0F) int listType,
                            @Range(from = 0x00, to = 0xFF) int listPage) {
        int talk1 = this.talk1 & ~MODE_MASK;
        talk1 |= LIST_MODE;       // 设置模式
        talk1 |= listType << 2;   // 添加类型
        this.talk1 = (byte) talk1;
        this.talk2 = (byte) (listPage & 0xFF);
    }

    /**
     * 设置普通对话模式（文本页 + 文本段）
     * 将自动清除 talk1 的 D7、D6 特殊功能位
     *
     * @param textPage    文本页码（04-0F）
     * @param textSegment 文本段号
     */
    public void setTextMode(@Range(from = 0x01, to = 0x0F) int textPage,
                            @Range(from = 0x00, to = 0xFF) int textSegment) {
        int talk1 = this.talk1 & SPRITE_MODEL_MASK;
        talk1 |= textPage << 2;     // 设置文本页
        this.talk1 = (byte) talk1;
        this.talk2 = (byte) (textSegment & 0xFF);
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
        String modeStr;
        if (isComputerMode()) {
            modeStr = String.format("LIST[%d]", getComputerIndex());
        } else if (isListMode()) {
            modeStr = String.format("SHOP[%d]", getListPage());
        } else {
            modeStr = String.format("TEXT[page=%02X,seg=%02X]", getTextPage(), getTextSegment());
        }
        return String.format(
                "Sprite{type=%02X(image=%d,dir=%d), x=%02X(%d), y=%02X(%d), talk1=%02X, talk2=%02X(%s), action=%02X, push=%b,ignoreTerrain=%b,lockDir=%b,noAnim=%b}",
                type & 0xFF, getImageId(), getDirection(),
                getRawX() & 0xFF, intX(), getRawY() & 0xFF, intY(),
                talk1 & 0xFF, talk2 & 0xFF, modeStr,
                action & 0xFF,
                hasCanPushed(), hasIgnoreTerrain(), hasLockDirection(), hasDisableMovingAnim()
        );
    }
}
