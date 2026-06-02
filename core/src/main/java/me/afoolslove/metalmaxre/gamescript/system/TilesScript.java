package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

/**
 * 切换 PPU图库
 */
public class TilesScript extends BaseSpriteScript {

    public TilesScript(@NotNull String op) {
        super(op);
    }

    /**
     * 切换精灵表下半部分的图块代码
     */
    public static class Sprite extends TilesScript {
        public byte bank;

        public Sprite(TerminalNode bank) {
            super("3E");
            this.bank = BaseSpriteScript.byteVal(bank);
        }

        public Sprite(byte bank) {
            super("3E");
            this.bank = bank;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", bank);
        }

        @Override
        public String toScript() {
            return String.format("#tiles sprite 0x%02X", bank);
        }
    }
}
