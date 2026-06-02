package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

/**
 * 移动镜头
 */
public abstract class ScrollScript extends BaseSpriteScript {
    public int count;

    public ScrollScript(@NotNull String op, TerminalNode count) {
        super(op);
        this.count = BaseSpriteScript.number(count);
    }

    public ScrollScript(@NotNull String op, int count) {
        super(op);
        this.count = count;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", (byte) count);
    }

    public abstract String getDirection();

    @Override
    public String toScript() {
        return String.format("#scroll %s %d", getDirection(), count);
    }

    public static class Up extends ScrollScript {
        public Up(TerminalNode count) {
            super("50", count);
        }

        public Up(int count) {
            super("50", count);
        }

        @Override
        public String getDirection() {
            return "up";
        }
    }

    public static class Down extends ScrollScript {
        public Down(TerminalNode count) {
            super("51", count);
        }

        public Down(int count) {
            super("51", count);
        }

        @Override
        public String getDirection() {
            return "down";
        }
    }

    public static class Left extends ScrollScript {
        public Left(TerminalNode count) {
            super("52", count);
        }

        public Left(int count) {
            super("52", count);
        }

        @Override
        public String getDirection() {
            return "left";
        }
    }

    public static class Right extends ScrollScript {
        public Right(TerminalNode count) {
            super("53", count);
        }

        public Right(int count) {
            super("53", count);
        }

        @Override
        public String getDirection() {
            return "right";
        }
    }

}
