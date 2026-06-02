package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * 对话时设置发言人
 */
public class SpeakerScript extends BaseSpriteScript {
    public byte speaker;

    public SpeakerScript(TerminalNode speaker) {
        super("25");
        this.speaker = BaseSpriteScript.byteVal(speaker);
    }

    public SpeakerScript(byte speaker) {
        super("25");
        this.speaker = speaker;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", speaker);
    }

    @Override
    public String toScript() {
        return String.format("#speaker 0x%02X", speaker);
    }
}
