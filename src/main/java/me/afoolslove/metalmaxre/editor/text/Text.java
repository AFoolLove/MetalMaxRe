package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.editor.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

public class Text {
    public String text;

    public Text(String text) {
        this.text = text;
    }

    public byte[] toBytes() {
        return WordBank.toBytes(text);
    }

    /**
     * 文本构建器
     */
    public static abstract class Builder<T extends Builder<T, V>, V> {
        /**
         * 解析文本
         *
         * @param text 文本
         * @return Builder
         */
        public abstract T text(@NotNull String text);

        /**
         * 等待一段时间后再显示后面的文本
         *
         * @param wait 等待的时间，大多数为 0x3C
         * @return Builder
         */
        public abstract T sleep(byte wait);

        /**
         * 让玩家进行选择，只能添加到在文本段的末尾
         * <p>
         * 索引为当前所在的文本段起始开始算起
         *
         * @param yes 玩家选择 是 时显示的文本索引
         * @param no  玩家选择 否 时显示的文本索引
         * @return Builder
         */
        public abstract T option(@Nullable Byte yes, @Nullable Byte no);

        /**
         * 跳过N个字符后继续显示
         * <p>
         * 不影响被跳过的字符
         *
         * @param length 跳过的字符长度
         * @return Builder
         */
        public abstract T skip(int length);

        /**
         * 控制对话的速度，速度全局有效，完毕后请使用{@link #resetSpeed()}恢复速度
         *
         * @param speed 速度
         * @return Builder
         */
        public abstract T speed(byte speed);

        /**
         * 恢复对话的速度
         *
         * @return Builder
         */
        public abstract T resetSpeed();

        /**
         * 控制被对话的精灵的朝向为上
         * <p>
         * 对话结束后恢复
         * <p>
         * * 必须与目标精灵对话，否则会死机
         *
         * @return Builder
         */
        public abstract T targetLockUp();

        /**
         * 控制被对话的精灵的朝向为下
         * <p>
         * 对话结束后恢复
         * <p>
         * * 必须与目标精灵对话，否则会死机
         *
         * @return Builder
         */
        public abstract T targetLockDown();

        /**
         * 控制被对话的精灵的朝向为左
         * <p>
         * 对话结束后恢复
         * <p>
         * * 必须与目标精灵对话，否则会死机
         *
         * @return Builder
         */
        public abstract T targetLockLeft();

        /**
         * 控制被对话的精灵的朝向为右
         * <p>
         * 对话结束后恢复
         * <p>
         * * 必须与目标精灵对话，否则会死机
         *
         * @return Builder
         */
        public abstract T targetLockRight();

        /**
         * 控制被对话的精灵向上走一步
         * <p>
         * 对话结束后依然保持位置
         * <p>
         * * 必须与目标精灵对话，否则会死机
         *
         * @return Builder
         */
        public abstract T targetMoveUp();

        /**
         * 控制被对话的精灵向下走一步
         * <p>
         * 对话结束后依然保持位置
         * <p>
         * * 必须与目标精灵对话，否则会死机
         *
         * @return Builder
         */
        public abstract T targetMoveDown();

        /**
         * 控制被对话的精灵向左走一步
         * <p>
         * 对话结束后依然保持位置
         * <p>
         * * 必须与目标精灵对话，否则会死机
         *
         * @return Builder
         */
        public abstract T targetMoveLeft();

        /**
         * 控制被对话的精灵向右走一步
         * <p>
         * 对话结束后依然保持位置
         * <p>
         * * 必须与目标精灵对话，否则会死机
         *
         * @return Builder
         */
        public abstract T targetMoveRight();

        /**
         * 显示玩家的名称
         *
         * @param player 玩家
         * @return Builder
         */
        public abstract T playerName(@NotNull Player player);

        /**
         * 显示当前正在操作的玩家的名称
         *
         * @return Builder
         */
        public abstract T currentPlayer();

        /**
         * 换行并由某个人将其说出接下来的文本
         *
         * @param who 某个人
         * @return Builder
         */
        public abstract T whoNameNewline(byte who);

        /**
         * 换行并显示名称
         *
         * @return Builder
         */
        public abstract T nameNewline();

        /**
         * 换行
         *
         * @return Builder
         */
        public abstract T newline();

        /**
         * 不进行解析，直接显示字符
         *
         * @param raw 字符
         * @return Builder
         */
        public abstract T raw(byte... raw);

        /**
         * 不进行解析，直接显示字符
         *
         * @param raw 字符
         * @return Builder
         */
        public abstract T raw(int... raw);

        /**
         * 等待玩家确认按键
         *
         * @return Builder
         */
        public abstract T waitForKey();

        /**
         * 等待玩家确认按键后换行显示名称
         *
         * @return Builder
         */
        public abstract T waitForKeyAndNameNewline();

        /**
         * 构建
         *
         * @return 构建出的数据
         */
        public abstract V build();
    }

    public static class ByteBuilder extends Builder<ByteBuilder, byte[]> {
        private Byte yes;
        private Byte no;

        private final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        public ByteBuilder() {
        }

        public ByteBuilder(@NotNull String text) {
            text(text);
        }

        public ByteBuilder(@Nullable Byte yes, @Nullable Byte no) {
            this.yes = yes;
            this.no = no;
        }

        public ByteBuilder(@NotNull String text, @Nullable Byte yes, @Nullable Byte no) {
            text(text);
            this.yes = yes;
            this.no = no;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder text(@NotNull String text) {
            byteArray.writeBytes(WordBank.toBytes(text));
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder sleep(byte wait) {
            byteArray.write(0xF1);
            byteArray.write(wait);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder option(@Nullable Byte yes, @Nullable Byte no) {
            this.yes = yes;
            this.no = no;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder skip(int length) {
            for (int i = length / 0xFF; i > 0; i--) {
                byteArray.write(0xED);
                byteArray.write(0xFF);
            }
            if (length > 0x100) {
                byteArray.write(0xED);
                byteArray.write(length & 0xFF);
            }
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder speed(byte speed) {
            byteArray.write(0xEE);
            byteArray.write(speed);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder resetSpeed() {
            byteArray.write(0xEE);
            byteArray.write(0x00); // TODO 多少来着，我怎么没有记录！！！
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder targetLockUp() {
            byteArray.write(0xF5);
            byteArray.write(0x0C);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder targetLockDown() {
            byteArray.write(0xF5);
            byteArray.write(0x0D);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder targetLockLeft() {
            byteArray.write(0xF5);
            byteArray.write(0x0E);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder targetLockRight() {
            byteArray.write(0xF5);
            byteArray.write(0x0F);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder targetMoveUp() {
            byteArray.write(0xF5);
            byteArray.write(0x12);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder targetMoveDown() {
            byteArray.write(0xF5);
            byteArray.write(0x13);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder targetMoveLeft() {
            byteArray.write(0xF5);
            byteArray.write(0x14);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder targetMoveRight() {
            byteArray.write(0xF5);
            byteArray.write(0x15);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder playerName(@NotNull Player player) {
            byteArray.write(0xFD);
            byteArray.write(player.getId());
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder currentPlayer() {
            byteArray.write(0xE8);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder whoNameNewline(byte who) {
            byteArray.write(0xF0);
            byteArray.write(who);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder nameNewline() {
            byteArray.write(0xFE);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder newline() {
            byteArray.write(0xE5);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder raw(byte... raw) {
            byteArray.write(0xF6);
            byteArray.writeBytes(raw);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder raw(int... raw) {
            byteArray.write(0xF6);
            for (int b : raw) {
                byteArray.write(b);
            }
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder waitForKey() {
            byteArray.write(0xE4);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ByteBuilder waitForKeyAndNameNewline() {
            byteArray.write(0xFE);
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public byte[] build() {
            if (yes == null && no != null) {
                byteArray.write(0xE3);
                byteArray.write(no);
            } else if (yes != null && no != null) {
                byteArray.write(0xEB);
                byteArray.write(yes);
                byteArray.write(no);
            }
            return byteArray.toByteArray();
        }
    }
}
