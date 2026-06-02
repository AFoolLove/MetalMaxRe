package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import me.afoolslove.metalmaxre.gamescript.i.IBlock;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

/**
 * 检测器
 *
 * @author AFoolLove
 */
public class DetectorScript extends IfScript implements IBlock {
    public int range;

    public DetectorScript(@NotNull TerminalNode range, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
        super("54", ctx, visitor);
        this.range = DetectorScript.number(range);
    }

    public DetectorScript(byte range) {
        this(range & 0xFF);
    }

    public DetectorScript(int range) {
        super("54");
        this.range = range;
    }

    @Override
    public int argsLength() {
        return 3;
    }

    @Override
    public String previewCode() {
        return op + String.format("%02X", range) + "00";
    }

    @Override
    public String toCode() {
        if (isEmptyArgs()) {
            return previewCode();
        }
        return op + String.format("%02X", range) + getOffsetOrBlock();
    }

    @Override
    public String toScript() {
        StringBuilder sb = new StringBuilder();
        if (hasTargetLabel()) {
            sb.append(String.format("#ifnot detector %d %s", range, getTargetLabel()));
        } else {
            sb.append(String.format("#if detector %d", range));
            sb.append(generateIndentedBlock(1));
            sb.append(isLoopList ? " #loopif" : "\n#endif");
        }
        return sb.toString();
    }
}
