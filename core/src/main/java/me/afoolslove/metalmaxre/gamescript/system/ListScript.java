package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import me.afoolslove.metalmaxre.gamescript.SpriteScriptParser;
import me.afoolslove.metalmaxre.gamescript.i.IPreviewCode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ListScript extends BaseSpriteScript implements IPreviewCode {
    public boolean isLoopList = false;
    public List<BaseSpriteScript> scripts = new ArrayList<>();

    public ListScript(@NotNull String op) {
        super(op);
    }

    public ListScript(@NotNull String op, List<BaseSpriteScript> scripts) {
        super(op);
        this.scripts = scripts;
    }

    public ListScript(@NotNull String op, List<BaseSpriteScript> scripts, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
        super(op);
        this.scripts = scripts;

        parse(this, ctx, visitor);
    }

    public int sumScriptCodeLength() {
        List<BaseSpriteScript> expand = expand(this);
        expand.remove(this);   // 移除自己，使用 IBlock#argsLength 获取自己的长度
        int sum = 0;
        for (BaseSpriteScript script : expand) {
            sum += script.toCode().length() / 2;
        }
        return sum;
    }

    @Override
    public String previewCode() {
        StringBuilder builder = new StringBuilder();
        for (BaseSpriteScript script : scripts) {
            if (script instanceof IPreviewCode previewCode) {
                builder.append(previewCode.previewCode());
            } else {
                builder.append(script.toCode());
            }
        }
        return builder.toString();
    }

    public static void parse(ListScript listScript, ParserRuleContext ctx, AbstractParseTreeVisitor<BaseSpriteScript> visitor) {
        int n = ctx.getChildCount();
        for (int i = 0; i < n; i++) {
            ParseTree child = ctx.getChild(i);

            if (child instanceof TerminalNode) {
                // 跳过非结构数据
                continue;
            }

            if (child instanceof SpriteScriptParser.EndifStatementContext endifStatementContext) {
                // 结束符，用于命令是否循环自身
                if (visitor.visit(endifStatementContext) instanceof EndIfScript endIfScript) {
                    listScript.isLoopList = endIfScript.isLoopList;
                }
                continue;
            }
            BaseSpriteScript script = visitor.visit(child);
            listScript.scripts.add(script);
        }
    }

    /**
     * if结构结束符，用于跳转到指定位置还是循环自身
     */
    public static class EndIfScript extends BaseSpriteScript {
        public boolean isLoopList;

        public EndIfScript(TerminalNode isLoopList) {
            super("");
            this.isLoopList = isLoopList.getText().equals("#loopif");
        }

        @Override
        public String toScript() {
            return isLoopList ? "#loopif" : "#endif";
        }
    }

    /**
     * 标签跳转目标
     * <p>
     * 用于非通常if结构时使用
     */
    public static class LabelTargetScript extends BaseSpriteScript {
        public String targetLabel;

        public LabelTargetScript(TerminalNode targetLabel) {
            super("");
            this.targetLabel = targetLabel.getText();
        }

        public String getTargetLabel() {
            return targetLabel;
        }

    }

    /**
     * 生成带缩进的块内容字符串
     *
     * @param indentLevel 缩进层级（0表示无缩进）
     * @return 格式化后的块内容
     */
    protected String generateIndentedBlock(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "    ".repeat(indentLevel);

        boolean debug = false;
        // 调试：打印脚本数量和内容
        if (debug) System.err.println("generateIndentedBlock 被调用, scripts.size() = " + scripts.size());

        for (int i = 0; i < scripts.size(); i++) {
            BaseSpriteScript script = scripts.get(i);
            if (debug)
                System.err.println("  脚本[" + i + "]: " + script.getClass().getSimpleName() + ", isEndIf=" + (script instanceof EndIfScript));

            if (!(script instanceof EndIfScript)) {
                // 如果脚本有标签，先输出标签（带当前层级的缩进）
                if (script.label != null && !script.label.isEmpty()) {
                    sb.append("\n").append(indent).append(script.label).append(":");
                }

                // 直接调用 toScript()，让每个脚本自己处理内部结构
                String scriptText = script.toScript();
                if (debug) System.err.println("    toScript() 返回: '" + scriptText.replace("\n", "\\n") + "'");

                // 处理多行脚本，为每一行添加当前层级的缩进
                String[] lines = scriptText.split("\n", -1);
                if (debug) System.err.println("    split 后有 " + lines.length + " 行");
                for (String line : lines) {
                    sb.append("\n").append(indent).append(line);
                }
            }
        }

        String result = sb.toString();
        if (debug) System.err.println("  generateIndentedBlock 返回: '" + result.replace("\n", "\\n") + "'");
        return result;
    }

    /**
     * 生成带指定缩进层级的脚本字符串
     *
     * @param indentLevel 缩进层级
     * @return 格式化后的脚本
     */
    public String toScriptWithIndent(int indentLevel) {
        // 默认实现：使用 toScript() 然后添加缩进
        String scriptText = this.toScript();
        String indent = "    ".repeat(indentLevel);

        StringBuilder sb = new StringBuilder();
        String[] lines = scriptText.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(indent).append(lines[i]);
        }
        return sb.toString();
    }
}
