package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

public abstract class MoneyScript extends BaseSpriteScript {
    public MoneyScript(@NotNull String op) {
        super(op);
    }

    /**
     * 减少玩家的金钱
     */
    public static class Spend extends MoneyScript {
        public byte money;

        public Spend(TerminalNode money) {
            super("4B");
            this.money = (byte) BaseSpriteScript.number(money);
        }

        public Spend(byte money) {
            super("4B");
            this.money = money;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", money);
        }

        @Override
        public String toScript() {
            return String.format("#money spend %d", money & 0xFF);
        }
    }
}
