package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

/**
 * 给予道具
 */
public abstract class GiveScript extends BaseSpriteScript {
    public byte item;

    public GiveScript(@NotNull String op, TerminalNode item) {
        this(op, BaseSpriteScript.byteVal(item));
    }

    public GiveScript(@NotNull String op, byte item) {
        super(op);
        this.item = item;
    }

    public abstract static class Player extends GiveScript {

        public Player(@NotNull String op, TerminalNode item) {
            super(op, item);
        }

        public Player(@NotNull String op, byte item) {
            super(op, item);
        }

        public static class Item extends Player {
            public byte text;

            public Item(TerminalNode item, TerminalNode text) {
                super("49", item);
                this.text = BaseSpriteScript.byteVal(text);
            }

            public Item(byte item, byte text) {
                super("49", item);
                this.text = text;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X%02X", item, text);
            }

            @Override
            public String toScript() {
                return String.format("#give item 0x%02X 0x%02X", item, text);
            }
        }
    }
}
