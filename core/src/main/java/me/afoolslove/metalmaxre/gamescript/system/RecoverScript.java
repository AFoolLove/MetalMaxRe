package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * 解除指定玩家的异常和满血
 */
public class RecoverScript extends BaseSpriteScript {
    public byte player;

    public RecoverScript(TerminalNode player) {
        super("2B");
        this.player = BaseSpriteScript.hex(player);
    }

    public RecoverScript(byte player) {
        super("2B");
        this.player = player;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", player);
    }

    @Override
    public String toScript() {
        return String.format("#recover #%02X", player);
    }
}
