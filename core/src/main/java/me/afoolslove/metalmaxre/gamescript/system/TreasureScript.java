package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

public abstract class TreasureScript extends BaseSpriteScript {
    public byte treasureId;

    public TreasureScript(TerminalNode treasureId) {
        super("66");
        this.treasureId = byteVal(treasureId);
    }

    public TreasureScript(byte treasureId) {
        super("66");
        this.treasureId = treasureId;
    }


    public static class Close extends TreasureScript {

        public Close(TerminalNode treasureId) {
            super(treasureId);
        }

        public Close(byte treasureId) {
            super(treasureId);
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", treasureId);
        }

        @Override
        public String toScript() {
            return String.format("#treasure close #%02X", treasureId);
        }
    }
}
