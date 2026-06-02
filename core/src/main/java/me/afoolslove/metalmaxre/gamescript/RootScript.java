package me.afoolslove.metalmaxre.gamescript;

import me.afoolslove.metalmaxre.gamescript.system.ListScript;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import java.util.ArrayList;
import java.util.List;

public class RootScript extends ListScript {
    public RootScript(List<BaseSpriteScript> scripts) {
        super("", scripts);
    }

    public RootScript(ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
        super("", new ArrayList<>(), ctx, visitor);
    }

    @Override
    public String toCode() {
        List<BaseSpriteScript> expand = expand(this);

        StringBuilder builder = new StringBuilder();
        for (BaseSpriteScript script : expand) {
            if (script instanceof RootScript) {
                continue;
            }
            builder.append(script.toCode());
        }
        return builder.toString();
    }

    @Override
    public String toScript() {
        StringBuilder sb = new StringBuilder();
        for (BaseSpriteScript script : scripts) {
            sb.append(script.toScript()).append("\n");
        }
        return sb.toString();
    }
}
