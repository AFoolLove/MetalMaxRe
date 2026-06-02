package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

public abstract class NpcScript extends BaseSpriteScript {
    public NpcScript(@NotNull String op) {
        super(op);
    }

    /**
     * 控制精灵的朝向，不会移动精灵
     */
    public static abstract class FaceScript extends NpcScript {
        public FaceScript(@NotNull String op) {
            super(op);
        }

        public static class Up extends FaceScript {
            public Up() {
                super("0C");
            }

            @Override
            public String toScript() {
                return "#face up";
            }
        }

        public static class Down extends FaceScript {
            public Down() {
                super("0D");
            }

            @Override
            public String toScript() {
                return "#face down";
            }
        }

        public static class Left extends FaceScript {
            public Left() {
                super("0E");
            }

            @Override
            public String toScript() {
                return "#face left";
            }
        }

        public static class Right extends FaceScript {
            public Right() {
                super("0F");
            }

            @Override
            public String toScript() {
                return "#face right";
            }
        }

        public static class Back extends FaceScript {
            public Back() {
                super("10");
            }

            @Override
            public String toScript() {
                return "#face back";
            }
        }

        public static class Player extends FaceScript {
            public Player() {
                super("11");
            }

            @Override
            public String toScript() {
                return "#face player";
            }
        }
    }


    public static abstract class MoveScript extends NpcScript {
        public MoveScript(@NotNull String op) {
            super(op);
        }

        public static abstract class Direction extends MoveScript {
            public int count;

            public Direction(@NotNull String op) {
                this(op, 1);
            }

            public Direction(@NotNull String op, int count) {
                super(op);
                this.count = count;
            }

            @Override
            public String toCode() {
                if (count == 1) {
                    return op;
                } else {
                    return op.repeat(Math.max(0, count));
                }
            }

            public abstract String getDirectionName();

            @Override
            public String toScript() {
                if (count == 1) {
                    return "#npc move " + getDirectionName();
                } else {
                    return String.format("#npc move %s %d", getDirectionName(), count);
                }
            }
        }

        public static class Up extends Direction {

            public Up() {
                this(1);
            }

            public Up(int count) {
                super("12", count);
            }

            @Override
            public String getDirectionName() {
                return "up";
            }
        }

        public static class Down extends Direction {

            public Down() {
                this(1);
            }

            public Down(int count) {
                super("13", count);
            }

            @Override
            public String getDirectionName() {
                return "down";
            }
        }

        public static class Left extends Direction {
            public Left() {
                this(1);
            }

            public Left(int count) {
                super("14", count);
            }

            @Override
            public String getDirectionName() {
                return "left";
            }
        }

        public static class Right extends Direction {
            public Right() {
                this(1);
            }

            public Right(int count) {
                super("15", count);
            }

            @Override
            public String getDirectionName() {
                return "right";
            }
        }

        public static class To extends MoveScript {
            public byte x;
            public byte y;

            public To(TerminalNode x, TerminalNode y) {
                super("16");
                this.x = BaseSpriteScript.byteVal(x);
                this.y = BaseSpriteScript.byteVal(y);
            }

            public To(byte x, byte y) {
                super("16");
                this.x = x;
                this.y = y;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", x) + String.format("%02X", y);
            }

            @Override
            public String toScript() {
                return String.format("#npc move to 0x%02X 0x%02X", x, y);
            }
        }

        public static class Tp extends MoveScript {
            public byte x;
            public byte y;

            public Tp(TerminalNode x, TerminalNode y) {
                super("20");
                this.x = BaseSpriteScript.byteVal(x);
                this.y = BaseSpriteScript.byteVal(y);
            }

            public Tp(byte x, byte y) {
                super("20");
                this.x = x;
                this.y = y;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", x) + String.format("%02X", y);
            }

            @Override
            public String toScript() {
                return String.format("#npc move tp 0x%02X 0x%02X", x, y);
            }
        }

        /**
         * 相对于屏幕传送位置
         */
        public static class TpOffset extends MoveScript {
            public byte xy;

            public TpOffset(TerminalNode xy) {
                super("3F");
                this.xy = BaseSpriteScript.byteVal(xy);
            }

            public TpOffset(byte xy) {
                super("3F");
                this.xy = xy;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", xy);
            }

            @Override
            public String toScript() {
                return String.format("#npc move tp offset 0x%02X", xy);
            }
        }

        public static class Chase extends MoveScript {
            public int count;

            public Chase() {
                this(0);
            }

            public Chase(int count) {
                super("");
                this.count = count;
            }

            @Override
            public String toCode() {
                if (count == 0) {
                    return "34";
                } else {
                    return "17".repeat(Math.max(1, count));
                }
            }

            @Override
            public String toScript() {
                if (count == 0) {
                    return "#npc move chase";
                } else {
                    return String.format("#npc move chase %d", count);
                }
            }
        }

        public static class Wander extends MoveScript {
            public Byte x1;
            public Byte y1;

            public Byte x2;
            public Byte y2;

            public Wander() {
                super("0A");
            }

            public Wander(TerminalNode x1, TerminalNode y1, TerminalNode x2, TerminalNode y2) {
                super("0A");
                this.x1 = BaseSpriteScript.byteVal(x1);
                this.y1 = BaseSpriteScript.byteVal(y1);
                this.x2 = BaseSpriteScript.byteVal(x2);
                this.y2 = BaseSpriteScript.byteVal(y2);
            }

            public Wander(byte x1, byte y1, byte x2, byte y2) {
                super("0A");
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
            }

            @Override
            public String toCode() {
                if (x1 == null) {
                    return op;
                }
                return "0B" + String.format("%02X", x1) + String.format("%02X", y1) + String.format("%02X", x2) + String.format("%02X", y2);
            }

            @Override
            public String toScript() {
                if (x1 == null) {
                    return "#npc move wander";
                }
                return String.format("#npc move wander 0x%02X 0x%02X 0x%02X 0x%02X", x1, y1, x2, y2);
            }
        }

        /**
         * 走向坦克位置
         */
        public static class Tank extends MoveScript {
            public Tank() {
                super("2E");
            }

            @Override
            public String toScript() {
                return "#npc move tank";
            }
        }
    }


    /**
     * 精灵以固定路径巡逻，直到触碰到玩家再继续执行
     */
    public static class Patrol extends NpcScript {
        public byte type;

        public Patrol(TerminalNode type) {
            super("2F");
            this.type = BaseSpriteScript.byteVal(type);
        }

        public Patrol(byte type) {
            super("2F");
            this.type = type;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", type);
        }

        @Override
        public String toScript() {
            return String.format("#npc patrol 0x%02X", type);
        }
    }

    /**
     * 隐藏精灵，可以使用20指令显示
     */
    public static class HideScript extends NpcScript {
        public HideScript() {
            super("1A");
        }

        @Override
        public String toScript() {
            return "#npc hide";
        }
    }

    /**
     * 将精灵从当前地图彻底移除
     */
    public static class RemoveScript extends NpcScript {
        public RemoveScript() {
            super("55");
        }

        @Override
        public String toScript() {
            return "#npc remove";
        }
    }

    public static abstract class Model extends NpcScript {

        public Model(@NotNull String op) {
            super(op);
        }

        public static class TileType extends Model {
            public byte model;

            public TileType(TerminalNode model) {
                super("1C");
                this.model = BaseSpriteScript.byteVal(model);
            }

            public TileType(byte model) {
                super("1C");
                this.model = model;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", model);
            }

            @Override
            public String toScript() {
                return String.format("#npc model tiletype 0x%02X", model);
            }
        }

        public static class Set extends Model {
            public byte model;

            public Set(TerminalNode model) {
                super("3B");
                this.model = BaseSpriteScript.byteVal(model);
            }

            public Set(byte model) {
                super("3B");
                this.model = model;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", model);
            }

            @Override
            public String toScript() {
                return String.format("#npc model 0x%02X", model);
            }
        }

        /**
         * 精灵变成下一个图像模型
         */
        public static class Next extends Model {
            public Next() {
                super("3C");
            }

            @Override
            public String toScript() {
                return "#npc model next";
            }
        }

        /**
         * 精灵变成上一个图像模型
         */
        public static class Previous extends Model {

            public Previous() {
                super("3D");
            }

            @Override
            public String toScript() {
                return "#npc model previous";
            }
        }
    }


    public abstract static class Anim extends NpcScript {

        public Anim(@NotNull String op) {
            super(op);
        }

        /**
         * 播放精灵动画
         */
        public static class Play extends Anim {
            public byte anim;

            public Play(TerminalNode anim) {
                super("4A");
                this.anim = BaseSpriteScript.byteVal(anim);
            }

            public Play(byte anim) {
                super("4A");
                this.anim = anim;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", anim);
            }

            @Override
            public String toScript() {
                return String.format("#npc anim play 0x%02X", anim);
            }
        }

        /**
         * 精灵动画帧、明奇电疗动画
         */
        public static class Frame extends Anim {
            public byte frame;

            public Frame(TerminalNode frame) {
                super("1B");
                this.frame = BaseSpriteScript.byteVal(frame);
            }

            public Frame(byte frame) {
                super("1B");
                this.frame = frame;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", frame);
            }

            @Override
            public String toScript() {
                return String.format("#npc anim frame 0x%02X", frame);
            }
        }

        /**
         * 明奇电疗动画
         */
        public static class Resume extends Anim {
            public Resume() {
                super("32");
            }

            @Override
            public String toScript() {
                return "#npc anim resume";
            }
        }

        /**
         * 精灵被抛飞动画
         */
        public static class Throw extends Anim {
            public byte b1;
            public byte npc;

            public Throw(TerminalNode b1, TerminalNode npc) {
                super("33");
                this.b1 = BaseSpriteScript.byteVal(b1);
                this.npc = BaseSpriteScript.byteVal(npc);
            }

            public Throw(byte b1, byte npc) {
                super("33");
                this.b1 = b1;
                this.npc = npc;
            }

            @Override
            public String toCode() {
                return op + String.format("%02X", b1) + String.format("%02X", npc);
            }

            @Override
            public String toScript() {
                return String.format("#npc anim throw 0x%02X 0x%02X", b1, npc);
            }
        }
    }

    /**
     * 精灵动作，原地踏步一次
     */
    public static class Act extends NpcScript {
        public Act() {
            super("1F");
        }

        @Override
        public String toScript() {
            return "#npc act";
        }
    }

    /**
     * 精灵属性
     */
    public static class Attrs extends NpcScript {
        public byte attrs;

        public Attrs(TerminalNode attrs) {
            super("29");
            this.attrs = BaseSpriteScript.byteVal(attrs);
        }

        public Attrs(byte attrs) {
            super("29");
            this.attrs = attrs;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", attrs);
        }

        @Override
        public String toScript() {
            return String.format("#npc attrs 0x%02X", attrs);
        }
    }

    /**
     * 精灵进入脚下的坦克
     */
    public static class EnterTank extends NpcScript {
        public EnterTank() {
            super("30");
        }

        @Override
        public String toScript() {
            return "#npc tank enter";
        }
    }

    /**
     * 精灵离开脚下的坦克
     */
    public static class ExitTank extends NpcScript {
        public byte model;

        public ExitTank(TerminalNode model) {
            super("31");
            this.model = BaseSpriteScript.byteVal(model);
        }

        public ExitTank(byte model) {
            super("31");
            this.model = model;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", model);
        }

        @Override
        public String toScript() {
            return String.format("#npc tank exit 0x%02X", model);
        }
    }

    /**
     * 精灵变成玩家，玩家隐藏
     */
    public static class Become extends NpcScript {
        public byte player;

        public Become(TerminalNode player) {
            super("40");
            this.player = BaseSpriteScript.byteVal(player);
        }

        public Become(byte player) {
            super("40");
            this.player = player;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", player);
        }

        @Override
        public String toScript() {
            return String.format("#npc become 0x%02X", player);
        }
    }

    /**
     * 精灵打开面前的门
     */
    public static class OpenDoor extends NpcScript {
        public OpenDoor() {
            super("4F");
        }

        @Override
        public String toScript() {
            return "#npc opendoor";
        }
    }

    /**
     * 在玩家附近随机爆炸
     */
    public static class Explode extends NpcScript {
        public Explode() {
            super("42");
        }

        @Override
        public String toScript() {
            return "#npc explode";
        }
    }

    /**
     * 玩家在精灵的四周或重叠时，玩家收到伤害
     */
    public static class Hurt extends NpcScript {
        public Hurt() {
            super("56");
        }

        @Override
        public String toScript() {
            return "#npc hurt";
        }
    }

    /**
     * 在精灵的位置绘制一个图块
     */
    public static class DrawTile extends NpcScript {
        public byte tile;

        public DrawTile(TerminalNode tile) {
            super("59");
            this.tile = BaseSpriteScript.byteVal(tile);
        }

        public DrawTile(byte tile) {
            super("59");
            this.tile = tile;
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", tile);
        }

        @Override
        public String toScript() {
            return String.format("#npc drawtile 0x%02X", tile);
        }
    }
}
