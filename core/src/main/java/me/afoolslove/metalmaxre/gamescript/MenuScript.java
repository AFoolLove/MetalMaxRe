package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.TerminalNode;

public class MenuScript extends BaseSpriteScript {
    public byte fun;
    public byte line;

    public MenuScript(TerminalNode fun, TerminalNode line) {
        super("26");
        this.fun = BaseSpriteScript.byteVal(fun);
        this.line = BaseSpriteScript.byteVal(line);
    }

    public MenuScript(byte fun, byte line) {
        super("26");
        this.fun = fun;
        this.line = line;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", fun) + String.format("%02X", line);
    }

    @Override
    public String toScript() {
        return String.format("#menu 0x%02X 0x%02X", fun, line);
    }
}
