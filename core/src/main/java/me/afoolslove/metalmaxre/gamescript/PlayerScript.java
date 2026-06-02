package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerScript extends BaseSpriteScript {
    public PlayerScript(@NotNull String op) {
        super(op);
    }

    public static class Face extends PlayerScript {
        public String direction;

        public Face(TerminalNode direction) {
            this(BaseSpriteScript.byteVal(direction));
        }

        public Face(byte direction) {
            super("6D");
            this.direction = "%02X".formatted(direction);
        }

        public Face(String direction) {
            super("6D");
            this.direction = direction;
        }

        @Override
        public String toCode() {
            return op + direction;
        }

        @Override
        public String toScript() {
            return "#player face 0x" + direction;
        }

        public static class Up extends Face {
            public Up() {
                super("40");
            }

            @Override
            public String toScript() {
                return "#player face up";
            }
        }

        public static class Down extends Face {
            public Down() {
                super("41");
            }

            @Override
            public String toScript() {
                return "#player face down";
            }
        }

        public static class Left extends Face {
            public Left() {
                super("42");
            }

            @Override
            public String toScript() {
                return "#player face left";
            }
        }

        public static class Right extends Face {
            public Right() {
                super("43");
            }

            @Override
            public String toScript() {
                return "#player face right";
            }
        }
    }

    public static class Show extends PlayerScript {
        private Byte custom = null;

        public Show() {
            super("4100");
        }

        public Show(TerminalNode custom) {
            super("41");
            this.custom = BaseSpriteScript.byteVal(custom);
        }

        public Show(byte custom) {
            super("41");
            this.custom = custom;
        }

        @Override
        public String toCode() {
            return custom == null ? op : String.format("%s%02X", op, custom);
        }

        @Override
        public String toScript() {
            return "#player show" + (custom == null ? "" : String.format(" 0x%02X", custom));
        }
    }

    public static class Hide extends PlayerScript {
        public Hide() {
            this("4101");
        }

        public Hide(@NotNull String op) {
            super(op);
        }

        @Override
        public String toScript() {
            return "#player hide";
        }

        /**
         * 隐藏三位主角图像
         */
        public static class All extends Hide {
            public All() {
                super("6C");
            }

            @Override
            public String toScript() {
                return "#player hide all";
            }
        }
    }

    public static class Become extends PlayerScript {
        public byte player;

        public Become(TerminalNode player) {
            super("4E");
            this.player = BaseSpriteScript.byteVal(player);
        }

        public Become(byte player) {
            super("4E");
            this.player = player;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", player);
        }

        @Override
        public String toScript() {
            return String.format("#player become 0x%02X", player);
        }
    }

    /**
     * 传送脚本
     */
    public static class Tp extends PlayerScript {
        public boolean quiet;
        public byte map;
        public byte x;
        public byte y;

        public Tp(TerminalNode map, TerminalNode x, TerminalNode y, boolean quiet) {
            super("");
            this.quiet = quiet;
            this.map = BaseSpriteScript.byteVal(map);
            this.x = BaseSpriteScript.byteVal(x);
            this.y = BaseSpriteScript.byteVal(y);
        }

        public Tp(byte map, byte x, byte y, boolean quiet) {
            super("");
            this.quiet = quiet;
            this.map = map;
            this.x = x;
            this.y = y;
        }

        public Tp() {
            super("4C");
        }

        @Override
        public String toCode() {
            if (!op.isEmpty()) {
                return op;
            }
            return String.format("%s%02X%02X%02X", quiet ? "73" : "5E", map, x, y);
        }

        @Override
        public String toScript() {
            if (!op.isEmpty()) {
                return "#player tp";
            }
            if (quiet) {
                return String.format("#player tp 0x%02X 0x%02X 0x%02X quiet", map, x, y);
            }
            return String.format("#player tp 0x%02X 0x%02X 0x%02X", map, x, y);
        }
    }

    /**
     * 牵引物体
     */
    public static class Pull extends PlayerScript {
        public byte pull;

        public Pull(TerminalNode pull) {
            super("60");
            this.pull = BaseSpriteScript.byteVal(pull);
        }

        public Pull(byte pull) {
            super("60");
            this.pull = pull;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", pull);
        }

        @Override
        public String toScript() {
            return String.format("#player pull 0x%02X", pull);
        }
    }

    /**
     * 停止牵引物体
     */
    public static class Unpull extends PlayerScript {

        public Unpull() {
            super("61");
        }

        @Override
        public String toScript() {
            return "#player unpull";
        }
    }
}
