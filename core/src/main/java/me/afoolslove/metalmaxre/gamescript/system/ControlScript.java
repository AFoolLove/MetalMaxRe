package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.jetbrains.annotations.NotNull;

public abstract class ControlScript extends BaseSpriteScript {

    public ControlScript(@NotNull String op) {
        super(op);
    }

    /**
     * 控制切换，玩家在可操作和不可操作的状态中顺序切换
     */
    public static class Change extends ControlScript {
        public Change() {
            super("43");
        }

        @Override
        public String toScript() {
            return "#control change";
        }
    }
}
