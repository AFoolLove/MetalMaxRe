package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

public class FlashScreenScript extends BaseSpriteScript {
    public byte time;

    public FlashScreenScript(TerminalNode time) {
        super("69");
        this.time = BaseSpriteScript.byteVal(time);
    }

    public FlashScreenScript(byte time) {
        super("69");
        this.time = time;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", time);
    }

    @Override
    public String toScript() {
        return String.format("#flashscreen 0x%02X", time);
    }
}
