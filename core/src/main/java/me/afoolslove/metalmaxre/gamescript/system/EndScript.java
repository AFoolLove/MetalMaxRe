package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;

/**
 * 结束脚本
 */
public class EndScript extends BaseSpriteScript {
    public EndScript() {
        super("00");
    }

    @Override
    public String toScript() {
        return "#end";
    }
}
