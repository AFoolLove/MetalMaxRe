package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

/**
 * 立即开启场景剧情
 */
public class SceneScript extends BaseSpriteScript {
    public byte stage;

    public SceneScript(@NotNull String op) {
        super(op);
    }

    public SceneScript(TerminalNode stage) {
        super("28");
        this.stage = BaseSpriteScript.byteVal(stage);
    }

    public SceneScript(byte stage) {
        super("28");
        this.stage = stage;
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", stage);
    }

    /**
     * 结束剧情
     */
    public static class End extends SceneScript {
        public End() {
            super("38");
        }

        @Override
        public String toCode() {
            return op;
        }

        @Override
        public String toScript() {
            return "#scene end";
        }
    }

    @Override
    public String toScript() {
        return String.format("#scene 0x%02X", stage);
    }
}
