package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

/**
 * 交换道具
 */
public class SwapScript extends BaseSpriteScript {
    public SwapScript(@NotNull String op) {
        super(op);
    }

    public static class Player extends SwapScript {
        public Player(@NotNull String op) {
            super(op);
        }

        /**
         * 交换人类道具
         * <p>
         * 注：交易失败不会有任何提示或操作
         */
        public static class Item extends Player {
            public byte B1;
            public byte B2;

            public Item(TerminalNode B1, TerminalNode B2) {
                super("3A");
                this.B1 = BaseSpriteScript.byteVal(B1);
                this.B2 = BaseSpriteScript.byteVal(B2);
            }

            public Item(byte B1, byte B2) {
                super("3A");
                this.B1 = B1;
                this.B2 = B2;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", B1) + String.format("%02X", B2);
            }

            @Override
            public String toScript() {
                return String.format("#swap player item 0x%02X 0x%02X", B1, B2);
            }
        }
    }
}
