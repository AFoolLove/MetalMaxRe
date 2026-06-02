package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * 对话结束后玩家将进行睡觉
 */
public class SleepScript extends BaseSpriteScript {
    public byte room;

    public SleepScript(TerminalNode room) {
        super("1E");
        this.room = BaseSpriteScript.byteVal(room);
    }

    public SleepScript(byte room) {
        super("1E");
        this.room = room;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", room);
    }

    @Override
    public String toScript() {
        return String.format("#sleep 0x%02X", room);
    }
}
