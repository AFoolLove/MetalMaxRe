package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;

/**
 * 开局被丢到楼下的第二天
 */
public class NextDayScript extends BaseSpriteScript {
    public NextDayScript() {
        super("21");
    }

    @Override
    public String toScript() {
        return "#nextday";
    }
}
