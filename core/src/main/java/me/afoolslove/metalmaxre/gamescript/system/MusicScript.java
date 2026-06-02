package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * 变更音乐
 */
public class MusicScript extends BaseSpriteScript {
    public byte music;

    public MusicScript(TerminalNode music) {
        super("1D");
        this.music = BaseSpriteScript.byteVal(music);
    }

    public MusicScript(byte music) {
        super("1D");
        this.music = music;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", music);
    }

    @Override
    public String toScript() {
        return String.format("#music 0x%02X", music);
    }
}
