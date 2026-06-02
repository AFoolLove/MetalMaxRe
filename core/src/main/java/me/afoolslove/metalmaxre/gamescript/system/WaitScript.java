package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

/**
 * 等待一段时间
 */
public abstract class WaitScript extends BaseSpriteScript {

    public WaitScript(@NotNull String op) {
        super(op);
    }

    public static class Time extends WaitScript {
        public static final byte SHORT = 0x1B;
        public static final byte NORMAL = 0x3C;
        public static final byte LONG = 0x78;

        public byte time;

        public Time(TerminalNode time) {
            super("19");
            this.time = switch (time.getText()) {
                case "short" -> SHORT;
                case "normal" -> NORMAL;
                case "long" -> LONG;
                default -> BaseSpriteScript.byteVal(time);
            };
        }

        public Time(byte time) {
            super("19");
            this.time = time;
        }

        @Override
        public String toCode() {
            return switch (time) {
                case SHORT -> "70";
                case NORMAL -> "71";
                case LONG -> "72";
                default -> op + String.format("%02X", time);
            };
        }

        @Override
        public String toScript() {
            if (time == SHORT) {
                return "#wait time short";
            } else if (time == NORMAL) {
                return "#wait time normal";
            } else if (time == LONG) {
                return "#wait time long";
            } else {
                return String.format("#wait time 0x%02X", time);
            }
        }
    }

    /**
     * 等待事件被关闭，如果没有关闭，精灵会被隐藏
     */
    public static class Event extends WaitScript {
        public byte eventCode;

        public Event(TerminalNode eventCode) {
            super("62");
            this.eventCode = BaseSpriteScript.hex(eventCode);
        }

        public Event(byte eventCode) {
            super("62");
            this.eventCode = eventCode;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", eventCode);
        }

        @Override
        public String toScript() {
            return String.format("#wait event #%02X", eventCode);
        }
    }
}
