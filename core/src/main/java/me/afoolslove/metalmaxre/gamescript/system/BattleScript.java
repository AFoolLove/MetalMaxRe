package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * 立即进入战斗
 */
public class BattleScript extends BaseSpriteScript {
    public byte monster;
    public byte eventCode;
    public byte stage;

    public BattleScript(TerminalNode monster, TerminalNode eventCode, TerminalNode stage) {
        super("37");
        this.monster = BaseSpriteScript.byteVal(monster);
        this.eventCode = BaseSpriteScript.byteVal(eventCode);
        this.stage = BaseSpriteScript.byteVal(stage);
    }

    public BattleScript(byte monster, byte eventCode, byte stage) {
        super("37");
        this.monster = monster;
        this.eventCode = eventCode;
        this.stage = stage;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", monster) + String.format("%02X", eventCode) + String.format("%02X", stage);
    }

    @Override
    public String toScript() {
        return String.format("#battle 0x%02X 0x%02X 0x%02X", monster, eventCode, stage);
    }
}
