package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * 将指定地图的坦克全部移动到另一张地图，保持位置和方向
 * <p>
 * 注：玩家也会被传送到目标地图
 */
public class ChangeTankMapScript extends BaseSpriteScript {
    public byte fromMap;
    public byte toMap;

    public ChangeTankMapScript(TerminalNode fromMap, TerminalNode toMap) {
        super("6E");
        this.fromMap = BaseSpriteScript.byteVal(fromMap);
        this.toMap = BaseSpriteScript.byteVal(toMap);
    }

    public ChangeTankMapScript(byte fromMap, byte toMap) {
        super("6E");
        this.fromMap = fromMap;
        this.toMap = toMap;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X%02X", fromMap, toMap);
    }

    @Override
    public String toScript() {
        return String.format("#changetankmap 0x%02X 0x%02X", fromMap, toMap);
    }
}
