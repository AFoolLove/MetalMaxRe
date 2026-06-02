package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import me.afoolslove.metalmaxre.gamescript.i.IDoBlock;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;

/**
 * 循环执行代码块内的指令指定次数
 */
public class LoopScript extends ListScript implements IDoBlock {
    public int count;

    public LoopScript(TerminalNode count) {
        super("07", new ArrayList<>());
        this.count = BaseSpriteScript.number(count);
    }

    public LoopScript(int count) {
        super("07", new ArrayList<>());
        this.count = count;
    }

    public LoopScript(TerminalNode count, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
        super("07", new ArrayList<>(), ctx, visitor);
        this.count = BaseSpriteScript.number(count);
    }

    public LoopScript(int count, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
        super("07", new ArrayList<>(), ctx, visitor);
        this.count = count;
    }

    @Override
    public String toCode() {
        if (isEmptyArgs()) {
            return previewCode();
        }
        return op + args.get(BLOCK) + String.format("%02X", (byte) count);
    }

    @Override
    public String previewCode() {
        return op + "00" + String.format("%02X", (byte) count);
    }

    @Override
    public int argsLength() {
        return 3;
    }

    @Override
    public String toScript() {
        StringBuilder sb = new StringBuilder("#do");
        sb.append(generateIndentedBlock(1));
        // #loop 前必须添加换行符，确保与上一行内容正确分隔
        sb.append("\n").append(String.format("#loop %d", count));
        return sb.toString();
    }
}
