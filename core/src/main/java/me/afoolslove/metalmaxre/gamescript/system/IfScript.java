package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import me.afoolslove.metalmaxre.gamescript.i.IBlock;
import me.afoolslove.metalmaxre.gamescript.i.ILabel;
import me.afoolslove.metalmaxre.gamescript.i.IPreviewCode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * 判断脚本
 * <p>
 * 判断事件、队伍等
 *
 * @author AFoolLove
 */
public abstract class IfScript extends ListScript implements ILabel {
    // 目标标签
    private String targetLabel;

    public IfScript(@NotNull String op) {
        super(op);
    }

    public IfScript(@NotNull String op, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
        super(op, new ArrayList<>(), ctx, visitor);

        // 处理 labelTargetStatement
        for (BaseSpriteScript script : scripts) {
            if (script instanceof LabelTargetScript labelTargetScript) {
                setTargetLabel(labelTargetScript.getTargetLabel());
                break;
            }
        }
        scripts.removeIf(script -> script instanceof LabelTargetScript);
    }

    /**
     * 获取目标标签
     */
    public String getTargetLabel() {
        return targetLabel;
    }

    /**
     * 设置目标标签
     */
    public void setTargetLabel(String targetLabel) {
        this.targetLabel = targetLabel;
    }

    /**
     * 判断是否为向上/跳转模式（有目标标签）
     */
    public boolean hasTargetLabel() {
        return targetLabel != null;
    }

    /**
     * 获取偏移量或 blockLength
     * - 标签跳转 if：使用 args[LABEL]（由 updateArgs 计算）
     * - 正常跳转 if：使用 args[BLOCK]
     */
    protected String getOffsetOrBlock() {
        if (isEmptyArgs() || isLoopList) {
            return "00";
        }
        if (hasTargetLabel()) {
            // 有目标标签时使用 LABEL 偏移量
            String label = args.get(ILabel.LABEL);
            return label != null ? label : "00";
        } else {
            // 普通 if 使用 BLOCK
            String block = args.get(IBlock.BLOCK);
            return block != null ? block : "00";
        }
    }

    /**
     * 生成带缩进的脚本块内容
     */
    protected String generateIndentedBlock(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(indentLevel); // 每层4个空格

        boolean debug = false;
        if (debug)
            System.err.println("[IfScript] generateIndentedBlock 被调用, indentLevel=" + indentLevel + ", scripts.size() = " + scripts.size());

        for (BaseSpriteScript script : scripts) {
            if (!(script instanceof EndIfScript)) {
                // 如果脚本有标签，先输出标签（带当前层级的缩进）
                if (script.label != null && !script.label.isEmpty()) {
                    sb.append("\n").append(indent).append(script.label).append(":");
                }

                if (debug) System.err.println("[IfScript]   处理脚本: " + script.getClass().getSimpleName());
                String scriptText = script.toScript();
                if (debug) System.err.println("[IfScript]     toScript() = '" + scriptText.replace("\n", "\\n") + "'");
                // 如果脚本本身包含多行，需要为每一行添加缩进
                String[] lines = scriptText.split("\n", -1);
                for (String line : lines) {
                    sb.append("\n").append(indent).append(line);
                }
            }
        }

        String result = sb.toString();
        if (debug) System.err.println("[IfScript]   返回 = '" + result.replace("\n", "\\n") + "'");
        return sb.toString();
    }

    public static class Event extends IfScript implements IPreviewCode, IBlock {
        public byte eventCode;

        public Event(TerminalNode hex) {
            super("06");
            eventCode = BaseSpriteScript.hex(hex);
        }

        public Event(byte eventCode) {
            super("06");
            this.eventCode = eventCode;
        }

        public Event(TerminalNode hex, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("06", ctx, visitor);
            eventCode = BaseSpriteScript.hex(hex);
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }
            return op + String.format("%02X", eventCode) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X00", eventCode);
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot event #%02X %s", eventCode, getTargetLabel()));
            } else {
                sb.append(String.format("#if event #%02X", eventCode));
                sb.append(generateIndentedBlock(1));
                sb.append("\n#endif");
            }
            return sb.toString();
        }
    }

    public static class Team extends IfScript implements IPreviewCode, IBlock {
        public byte player;

        public Team(TerminalNode hex) {
            super("24");
            player = BaseSpriteScript.hex(hex);
        }

        public Team(byte player) {
            super("24");
            this.player = player;
        }

        public Team(TerminalNode hex, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("24", ctx, visitor);
            player = BaseSpriteScript.hex(hex);
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }

            return op + String.format("%02X", player) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", player) + "00";
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot team #%02X %s", player, getTargetLabel()));
            } else {
                sb.append(String.format("#if team #%02X", player));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static abstract class Tank extends IfScript implements IPreviewCode, IBlock {

        public Tank(@NotNull String op) {
            super(op);
        }

        public Tank(@NotNull String op, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super(op, ctx, visitor);
        }

        /**
         * 如果乘坐了指定的坦克
         */
        public static class Riding extends Tank {
            public byte tank;

            public Riding(TerminalNode tank) {
                super("5A");
                this.tank = BaseSpriteScript.hex(tank);
            }

            public Riding(byte tank) {
                super("5A");
                this.tank = tank;
            }

            public Riding(TerminalNode tank, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
                super("5A", ctx, visitor);
                this.tank = BaseSpriteScript.hex(tank);
            }

            @Override
            public String toCode() {
                if (isEmptyArgs()) {
                    return previewCode();
                }
                return op + String.format("%02X", tank) + getOffsetOrBlock();
            }

            @Override
            public String previewCode() {
                return op + String.format("%02X", tank) + "00";
            }

            @Override
            public int argsLength() {
                return 3;
            }

            @Override
            public String toScript() {
                StringBuilder sb = new StringBuilder();
                if (hasTargetLabel()) {
                    sb.append(String.format("#ifnot tank riding #%02X %s", tank, getTargetLabel()));
                } else {
                    sb.append(String.format("#if tank riding #%02X", tank));
                    sb.append(generateIndentedBlock(1));
                    sb.append(isLoopList ? " #loopif" : "\n#endif");
                }
                return sb.toString();
            }
        }
    }

    /**
     * 如果指定的坦克在当前地图
     */
    public static class Here extends Tank {
        public byte tank;

        public Here(TerminalNode tank) {
            super("5B");
            this.tank = BaseSpriteScript.hex(tank);
        }

        public Here(byte tank) {
            super("5B");
            this.tank = tank;
        }

        public Here(TerminalNode tank, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("5B", ctx, visitor);
            this.tank = BaseSpriteScript.hex(tank);
        }

        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }
            return op + String.format("%02X", tank) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", tank) + "00";
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot tank here #%02X %s", tank, getTargetLabel()));
            } else {
                sb.append(String.format("#if tank here #%02X", tank));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static class RideTank extends IfScript implements IPreviewCode, IBlock {
        public RideTank() {
            super("22");
        }

        public RideTank(ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("22", ctx, visitor);
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }

            return op + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + "00";
        }

        @Override
        public int argsLength() {
            return 2;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot ridetank %s", getTargetLabel()));
            } else {
                sb.append("#if ridetank");
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    /**
     * 判断当前地图是否存在一辆没有被乘坐属于玩家且可以移动的坦克
     */
    public static class HasOkTank extends IfScript implements IPreviewCode, IBlock {
        public HasOkTank() {
            super("2D");
        }

        public HasOkTank(ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("2D", ctx, visitor);
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }

            return op + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + "00";
        }

        @Override
        public int argsLength() {
            return 2;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot hasoktank %s", getTargetLabel()));
            } else {
                sb.append("#if hasoktank");
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    /**
     * 玩家是否携带指定的物品
     */
    public static class HasItem extends IfScript implements IPreviewCode, IBlock {
        public byte item;

        public HasItem(TerminalNode item) {
            super("39");
            this.item = BaseSpriteScript.hex(item);
        }

        public HasItem(byte item) {
            super("39");
            this.item = item;
        }

        public HasItem(TerminalNode item, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("39", ctx, visitor);
            this.item = BaseSpriteScript.hex(item);
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }
            return op + String.format("%02X", item) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", item) + "00";
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot hasitem #%02X %s", item, getTargetLabel()));
            } else {
                sb.append(String.format("#if hasitem #%02X", item));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static class Face extends IfScript implements IPreviewCode, IBlock {
        public String face;

        public Face(String face) {
            super("2C");
            this.face = face;
        }

        public Face(String face, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("2C", ctx, visitor);
            this.face = face;
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }
            return op + face + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + face + "00";
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            String dir = switch (face) {
                case "00" -> "up";
                case "01" -> "down";
                case "02" -> "left";
                case "03" -> "right";
                default -> face;
            };
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot face %s %s", dir, getTargetLabel()));
            } else {
                sb.append("#if face " + dir);
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    /**
     * 判断玩家等级是否大于等于目标等级
     */
    public static class Level extends IfScript implements IPreviewCode, IBlock {
        public int level;

        public Level(TerminalNode level) {
            super("36");
            this.level = BaseSpriteScript.number(level);
        }

        public Level(int level) {
            super("36");
            this.level = level;
        }

        public Level(TerminalNode level, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("36", ctx, visitor);
            this.level = BaseSpriteScript.number(level);
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }
            return op + String.format("%02X", (byte) level) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", (byte) level) + "00";
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot level %d %s", level, getTargetLabel()));
            } else {
                sb.append(String.format("#if level %d", level));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static class Xy extends IfScript implements IPreviewCode, IBlock {
        public byte x;
        public byte y;

        public Xy(TerminalNode x, TerminalNode y) {
            super("08");
            this.x = BaseSpriteScript.byteVal(x);
            this.y = BaseSpriteScript.byteVal(y);
        }

        public Xy(byte x, byte y) {
            super("08");
            this.x = x;
            this.y = y;
        }

        public Xy(TerminalNode x, TerminalNode y, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("08", ctx, visitor);
            this.x = BaseSpriteScript.byteVal(x);
            this.y = BaseSpriteScript.byteVal(y);
        }

        @Override
        public String toCode() {
            return op + String.format("%02X%02X", x, y) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X%02X", x, y) + "00";
        }

        @Override
        public int argsLength() {
            return 4;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot xy 0x%02X 0x%02X %s", x, y, getTargetLabel()));
            } else {
                sb.append(String.format("#if xy 0x%02X 0x%02X", x, y));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static class Area extends IfScript implements IPreviewCode, IBlock {
        public byte XX1;
        public byte XX2;
        public byte YY1;
        public byte YY2;

        public Area(TerminalNode XX1, TerminalNode XX2, TerminalNode YY1, TerminalNode YY2) {
            super("35");
            this.XX1 = BaseSpriteScript.byteVal(XX1);
            this.XX2 = BaseSpriteScript.byteVal(XX2);
            this.YY1 = BaseSpriteScript.byteVal(YY1);
            this.YY2 = BaseSpriteScript.byteVal(YY2);
        }

        public Area(byte XX1, byte XX2, byte YY1, byte YY2) {
            super("35");
            this.XX1 = XX1;
            this.XX2 = XX2;
            this.YY1 = YY1;
            this.YY2 = YY2;
        }

        public Area(TerminalNode XX1, TerminalNode XX2, TerminalNode YY1, TerminalNode YY2, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("35", ctx, visitor);
            this.XX1 = BaseSpriteScript.byteVal(XX1);
            this.XX2 = BaseSpriteScript.byteVal(XX2);
            this.YY1 = BaseSpriteScript.byteVal(YY1);
            this.YY2 = BaseSpriteScript.byteVal(YY2);
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }
            return op + String.format("%02X", XX1) + String.format("%02X", XX2) + String.format("%02X", YY1) + String.format("%02X", YY2) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", XX1) + String.format("%02X", XX2) + String.format("%02X", YY1) + String.format("%02X", YY2) + "00";
        }

        @Override
        public int argsLength() {
            return 6;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot area 0x%02X 0x%02X 0x%02X 0x%02X %s", XX1, XX2, YY1, YY2, getTargetLabel()));
            } else {
                sb.append(String.format("#if area 0x%02X 0x%02X 0x%02X 0x%02X", XX1, XX2, YY1, YY2));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static class Money extends IfScript implements IPreviewCode, IBlock {
        public int money;

        public Money(TerminalNode money) {
            super("45");
            this.money = BaseSpriteScript.number(money);
        }

        public Money(byte money) {
            super("45");
            this.money = money & 0xFF;
        }

        public Money(TerminalNode money, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("45", ctx, visitor);
            this.money = BaseSpriteScript.number(money);
        }

        @Override
        public String toCode() {
            return op + String.format("%02X", money & 0xFF) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", money & 0xFF) + "00";
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot money %d %s", money & 0xFF, getTargetLabel()));
            } else {
                sb.append(String.format("#if money %d", money & 0xFF));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    /**
     * 判断宝藏是否未开启
     */
    public static class Treasure extends IfScript implements IPreviewCode, IBlock {
        public byte treasure;

        public Treasure(TerminalNode treasure) {
            super("5C");
            this.treasure = BaseSpriteScript.byteVal(treasure);
        }

        public Treasure(byte treasure) {
            super("5C");
            this.treasure = treasure;
        }

        public Treasure(TerminalNode treasure, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("5C", ctx, visitor);
            this.treasure = BaseSpriteScript.byteVal(treasure);
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }

            return op + String.format("%02X", treasure) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", treasure) + "00";
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot treasure 0x%02X %s", treasure, getTargetLabel()));
            } else {
                sb.append(String.format("#if treasure 0x%02X", treasure));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    /**
     * 判断玩家当前生命值是否大于目标值
     */
    public static class Hp extends IfScript implements IPreviewCode, IBlock {
        public int hp;

        public Hp(TerminalNode hp) {
            super("5D");
            this.hp = BaseSpriteScript.number(hp);
        }

        public Hp(int hp) {
            super("5D");
            this.hp = hp;
        }

        public Hp(TerminalNode hp, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("5D", ctx, visitor);
            this.hp = BaseSpriteScript.number(hp);
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }
            return op + String.format("%02X", (byte) hp) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", (byte) hp) + "00";
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot hp %d %s", hp & 0xFF, getTargetLabel()));
            } else {
                sb.append(String.format("#if hp %d", hp & 0xFF));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static class TeammateDead extends IfScript implements IPreviewCode, IBlock {
        public byte teammate;

        public TeammateDead(TerminalNode teammate) {
            super("2A");
            this.teammate = BaseSpriteScript.hex(teammate);
        }

        public TeammateDead(byte teammate) {
            super("2A");
            this.teammate = teammate;
        }

        public TeammateDead(TerminalNode teammate, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("2A", ctx, visitor);
            this.teammate = BaseSpriteScript.hex(teammate);
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }

            return op + String.format("%02X", teammate) + getOffsetOrBlock();
        }

        @Override
        public String previewCode() {
            return op + String.format("%02X", teammate) + "00";
        }

        @Override
        public int argsLength() {
            return 3;
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot teammate dead #%02X %s", teammate, getTargetLabel()));
            } else {
                sb.append(String.format("#if teammate dead #%02X", teammate));
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static class Drank extends IfScript implements IBlock {
        public Drank(ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super("4D", ctx, visitor);
        }

        public Drank() {
            super("4D");
        }

        @Override
        public int argsLength() {
            return 2;
        }

        @Override
        public String previewCode() {
            return op + "00";
        }

        @Override
        public String toCode() {
            if (isEmptyArgs()) {
                return previewCode();
            }

            return op + getOffsetOrBlock();
        }

        @Override
        public String toScript() {
            StringBuilder sb = new StringBuilder();
            if (hasTargetLabel()) {
                sb.append(String.format("#ifnot drank %s", getTargetLabel()));
            } else {
                sb.append("#if drank");
                sb.append(generateIndentedBlock(1));
                sb.append(isLoopList ? " #loopif" : "\n#endif");
            }
            return sb.toString();
        }
    }

    public static abstract class Option extends IfScript {

        public Option(@NotNull String op) {
            super(op);
        }

        public Option(@NotNull String op, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
            super(op, ctx, visitor);
        }

        public static class Yes extends Option implements IPreviewCode, IBlock {
            public Yes() {
                super("09");
            }

            public Yes(ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
                super("09", ctx, visitor);
            }

            @Override
            public String toCode() {
                if (isEmptyArgs()) {
                    return previewCode();
                }
                return op + getOffsetOrBlock();
            }

            @Override
            public String previewCode() {
                return op + "00";
            }

            @Override
            public int argsLength() {
                return 2;
            }

            @Override
            public String toScript() {
                StringBuilder sb = new StringBuilder();
                if (hasTargetLabel()) {
                    sb.append(String.format("#ifnot option yes %s", getTargetLabel()));
                } else {
                    sb.append("#if option yes");
                    sb.append(generateIndentedBlock(1));
                    sb.append(isLoopList ? " #loopif" : "\n#endif");
                }
                return sb.toString();
            }
        }
    }
}
