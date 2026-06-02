package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

public abstract class TeamScript extends BaseSpriteScript {
    public TeamScript(@NotNull String op) {
        super(op);
    }

    public static class Hide extends TeamScript {

        public Hide() {
            super("5F");
        }

        @Override
        public String toScript() {
            return "#team hide";
        }
    }

    public static class Join extends TeamScript {
        public byte player;

        public Join(TerminalNode player) {
            super("46");
            this.player = BaseSpriteScript.hex(player);
        }

        public Join(byte player) {
            super("46");
            this.player = player;
        }

        public Join(@NotNull String op, TerminalNode player) {
            super(op);
            this.player = BaseSpriteScript.hex(player);
        }

        public Join(@NotNull String op, byte player) {
            super(op);
            this.player = player;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", player);
        }

        @Override
        public String toScript() {
            return String.format("#team join #%02X", player);
        }

        public static class Now extends Join {
            public Now(TerminalNode player) {
                super("23", player);
            }

            public Now(byte player) {
                super("23", player);
            }

            @Override
            public String toScript() {
                return String.format("#team join now #%02X", player);
            }
        }
    }

    public static class Leave extends TeamScript {
        public byte player;

        public Leave(TerminalNode player) {
            super("47");
            this.player = BaseSpriteScript.hex(player);
        }

        public Leave(byte player) {
            super("47");
            this.player = player;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", player);
        }

        @Override
        public String toScript() {
            return String.format("#team leave #%02X", player);
        }
    }
}
