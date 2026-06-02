package me.afoolslove.metalmaxre.gamescript;

/**
 * 玩家当前乘坐的所有战车获得勋章
 */
public class MedalScript extends BaseSpriteScript {
    public MedalScript() {
        super("6F");
    }

    @Override
    public String toScript() {
        return "#medal";
    }
}
