package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

public abstract class EventScript extends BaseSpriteScript {
    public byte eventCode;

    public EventScript(@NotNull String op, TerminalNode hex) {
        super(op);
        this.eventCode = BaseSpriteScript.hex(hex);
    }

    public EventScript(@NotNull String op, byte eventCode) {
        super(op);
        this.eventCode = eventCode;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", eventCode);
    }

    public static class Open extends EventScript {
        public Open(TerminalNode hex) {
            super("04", hex);
        }

        public Open(byte eventCode) {
            super("04", eventCode);
        }

        @Override
        public String toScript() {
            return String.format("#event open #%02X", eventCode);
        }
    }

    public static class Close extends EventScript {
        public Close(TerminalNode hex) {
            super("05", hex);
        }

        public Close(byte eventCode) {
            super("05", eventCode);
        }

        @Override
        public String toScript() {
            return String.format("#event close #%02X", eventCode);
        }
    }

    public static class Wait extends EventScript {
        public Wait(TerminalNode hex) {
            super("27", hex);
        }

        public Wait(byte eventCode) {
            super("27", eventCode);
        }

        @Override
        public String toScript() {
            return String.format("#event wait #%02X", eventCode);
        }
    }
}
