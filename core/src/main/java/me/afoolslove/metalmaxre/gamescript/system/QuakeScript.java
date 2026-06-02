package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * 地震
 */
public class QuakeScript extends BaseSpriteScript {
    public byte time;

    public QuakeScript(TerminalNode time) {
        super("44");
        this.time = BaseSpriteScript.byteVal(time);
    }

    public QuakeScript(byte time) {
        super("44");
        this.time = time;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", time);
    }

    @Override
    public String toScript() {
        return String.format("#quake 0x%02X", time);
    }
}
