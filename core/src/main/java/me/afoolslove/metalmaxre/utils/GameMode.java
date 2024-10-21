package me.afoolslove.metalmaxre.utils;

/**
 * 游戏模式
 * <p>
 * 情报速度、战斗动画、背景音乐
 */
public class GameMode {
    private byte value = 0x00;

    public void setValue(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public int intValue() {
        return getValue() & 0xFF;
    }

    /**
     * 设置情报速度
     *
     * @param messageSpeed 情报速度，范围为0~4
     */
    public void setMessageSpeed(int messageSpeed) {
        messageSpeed &= 0B0000_0111;
        if (messageSpeed > 0x04) {
            messageSpeed = 0x04;
        }
        this.value = NumberR.set(this.value, (byte) messageSpeed, 0x00, 3);
    }

    /**
     * 开关战斗动画
     *
     * @param animations 开关战斗动画
     */
    public void setAnimations(boolean animations) {
        this.value = NumberR.set(this.value, animations ? (byte) 0 : (byte) 1, 0x05, 1);
    }

    /**
     * 开关音乐
     *
     * @param radio 音乐
     */
    public void setRadio(boolean radio) {
        this.value = NumberR.set(this.value, radio ? (byte) 0 : (byte) 1, 0x06, 1);
    }

    /**
     * 获取情报速度
     *
     * @return 情报速度，范围为0~4
     */
    public int getMessageSpeed() {
        return this.value & 0B0000_0111;
    }

    /**
     * 获取是否开启了战斗动画
     *
     * @return 是否开启了战斗动画
     */
    public boolean isAnimations() {
        return (this.value & 0B0100_0000) == 0;
    }

    /**
     * 获取是否开启了音乐
     *
     * @return 是否开启了音乐
     */
    public boolean isRadio() {
        return (this.value & 0B0010_0000) == 0;
    }
}
