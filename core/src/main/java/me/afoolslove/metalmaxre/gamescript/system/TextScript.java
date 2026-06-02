package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 显示纯文本或是带选项的文本
 */
public abstract class TextScript extends BaseSpriteScript {
    public Byte page;
    public Byte line;

    public List<String> text;

    public TextScript(@NotNull String op, List<String> text) {
        super(op);
        this.text = text;
    }

    public static class Plain extends TextScript {

        public Plain(List<String> text) {
            super("", text);
        }

        @Override
        public String toCode() {
            StringBuilder builder = new StringBuilder();
            for (String s : text) {
                builder.append(op);
                builder.append("00");
            }
            return builder.toString();
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder("#text");
            for (String s : text) {
                sb.append("\n> ").append(s);
            }
            return sb.toString();
        }
    }

    public static class Option extends TextScript {

        public Option(List<String> text) {
            super("", text);
        }

        @Override
        public String toCode() {
            StringBuilder builder = new StringBuilder();
            for (String s : text) {
                builder.append(op);
                builder.append("00");
            }
            return builder.toString();
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder("#text option default");
            for (String s : text) {
                sb.append("\n> ").append(s);
            }
            return sb.toString();
        }
    }

    public static class Quote extends TextScript {
        private boolean isDynamic;

        public Quote(boolean isDynamic, @NotNull TerminalNode page, @NotNull TerminalNode line) {
            super("", null);
            this.isDynamic = isDynamic;
            this.page = BaseSpriteScript.byteVal(page);
            this.line = BaseSpriteScript.byteVal(line);
        }

        public Quote(boolean isDynamic, byte page, byte line) {
            super("", null);
            this.isDynamic = isDynamic;
            this.page = page;
            this.line = line;
        }

        @Override
        public String toCode() {
            if (isDynamic) {
                return switch (page) {
                    case 0x0E -> String.format("68%02X", line);
                    case 0x0F -> String.format("03%02X", line);
                    default -> String.format("76%02X%02X", page, line);
                };
            } else {
                return switch (page) {
                    case 0x05 -> String.format("01%02X", line);
                    case 0x0B -> String.format("48%02X", line);
                    case 0x0E -> String.format("02%02X", line);
                    default -> String.format("76%02X%02X", page, line);
                };
            }
        }

        @Override
        public String toScript() {
            return String.format("#text quote 0x%02X 0x%02X", page, line);
        }
    }

    /**
     * 根据事件的开或关显示对应的文本段
     */
    public static class Event extends TextScript {
        public byte page;
        public byte eventCode;
        public byte trueLine;
        public byte falseLine;

        public Event(TerminalNode page, TerminalNode eventCode, TerminalNode trueLine, TerminalNode falseLine) {
            super("", null);
            this.page = BaseSpriteScript.byteVal(page);
            this.eventCode = BaseSpriteScript.byteVal(eventCode);
            this.trueLine = BaseSpriteScript.byteVal(trueLine);
            this.falseLine = BaseSpriteScript.byteVal(falseLine);
        }

        public Event(byte page, byte eventCode, byte trueLine, byte falseLine) {
            super("", null);
            this.page = page;
            this.eventCode = eventCode;
            this.trueLine = trueLine;
            this.falseLine = falseLine;
        }

        @Override
        public String toCode() {
            return "%s%02X%02X%02X".formatted(
                    switch (page) {
                        // static
                        case 0x05 -> "63";
                        case 0x0E -> "64";
                        case 0x0B -> "65";
                        default -> throw new IllegalStateException("Unexpected value: " + String.format("%02X", page));
                    }, eventCode, trueLine, falseLine);
        }

        @Override
        public String toScript() {
            return String.format("#text event 0x%02X 0x%02X 0x%02X 0x%02X", page, eventCode, trueLine, falseLine);
        }
    }
}
