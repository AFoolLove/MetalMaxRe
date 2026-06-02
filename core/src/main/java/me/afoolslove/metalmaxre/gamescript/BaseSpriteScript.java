package me.afoolslove.metalmaxre.gamescript;

import me.afoolslove.metalmaxre.gamescript.i.IBlock;
import me.afoolslove.metalmaxre.gamescript.i.IDoBlock;
import me.afoolslove.metalmaxre.gamescript.i.ILabel;
import me.afoolslove.metalmaxre.gamescript.system.ListScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class BaseSpriteScript {
    // 标签
    public String label;

    public String op;

    public Map<String, String> args;

    // 脚本在字节流中的实际位置
    public int bytePosition = -1;

    public BaseSpriteScript(@NotNull String op) {
        this.op = op;
    }

    public String toCode() {
        return op;
    }

    public String toScript() {
        return "";
    }

    public boolean isEmptyArgs() {
        return args == null || args.isEmpty();
    }


    public boolean setLabel(TerminalNode label) {
        return setLabel(this, label);
    }

    public static boolean setLabel(BaseSpriteScript script, TerminalNode label) {
        if (label != null) {
            script.label = label.getText().split(":")[0];
            return true;
        }
        return false;
    }

    public static int number(TerminalNode node) {
        return Integer.parseInt(node.getText());
    }

    public static byte hex(TerminalNode node) {
        return (byte) Integer.parseInt(node.getText().substring(1), 16);
    }

    public static byte byteVal(TerminalNode node) {
        return (byte) Integer.parseInt(node.getText().substring(2), 16);
    }

    public static void updateArgs(ListScript listScript) {
        // 将listScript展开为一个List合集
        List<BaseSpriteScript> scripts = expand(listScript);

        // 先更新代码块长度
        for (BaseSpriteScript script : scripts) {
            if (script instanceof IBlock block && script instanceof ListScript lsScript) {
                // 检查是否为有目标标签的 if：必须是 IfScript 且 hasTargetLabel() 为 true
                boolean hasTargetLabel = false;
                if (script instanceof me.afoolslove.metalmaxre.gamescript.system.IfScript ifScript) {
                    hasTargetLabel = ifScript.hasTargetLabel();
                }

                if (!hasTargetLabel) {
                    // 非向上 if，正常计算 BLOCK 值
                    script.args = new HashMap<>();
                    if (lsScript.isLoopList) {
                        script.args.put(IBlock.BLOCK, "00");
                    } else {
                        if (script instanceof IDoBlock) {
                            script.args.put(IBlock.BLOCK, String.format("%02X", (byte) -lsScript.sumScriptCodeLength()));
                        } else {
                            script.args.put(IBlock.BLOCK, String.format("%02X", block.argsLength() + lsScript.sumScriptCodeLength()));
                        }
                    }
                }
                // 如果是向上 if，保留已有的 args（包含正确的 BLOCK 值，如 FF 表示向上）
            }
        }

        // 再更新标签长度
        // 标签位置可以在前或在后
        for (int i = 0, count = scripts.size(); i < count; i++) {
            BaseSpriteScript script = scripts.get(i);
            if (script instanceof ILabel label) {
                if (script.args == null) {
                    script.args = new HashMap<>();
                }

                boolean isPositive = false;     // 用于判断标签在上还是在下，默认在上
                BaseSpriteScript target = null; // 标签目标
                int sumCodeLength = 0;
                for (BaseSpriteScript baseSpriteScript : scripts) {
                    if (baseSpriteScript == script) {
                        isPositive = true;
                        continue;
                    }

                    // 判断标签目标
                    if (Objects.equals(baseSpriteScript.label, label.getTargetLabel())) {
                        // 已找到目标
                        target = baseSpriteScript;
                        break;
                    }
                }

                if (target != null) {
                    // 标签已找到，计算代码长度
                    // 统一使用 toCode().length() / 2 获取每条脚本的完整字节长度
                    if (isPositive) {
                        // 向下遍历：从当前脚本的下一个开始计算（不包括当前脚本）
                        for (int j = i + 1; j < count; j++) {
                            BaseSpriteScript baseSpriteScript = scripts.get(j);
                            if (baseSpriteScript != target) {
                                sumCodeLength += baseSpriteScript.toCode().length() / 2;
                            } else {
                                // 找到目标，增加当前脚本的长度
                                sumCodeLength += script.toCode().length() / 2;
                                break;
                            }
                        }
                    } else {
                        // 向上遍历：统一使用 toCode().length() / 2 获取完整字节长度
                        // 注意：不能使用 sumScriptCodeLength()，因为它只计算子脚本长度，
                        // 对于空 if 块会返回 0，漏掉指令头本身（如 #if event 需要 3 字节）
                        for (int j = i - 1; j >= 0; j--) {
                            BaseSpriteScript baseSpriteScript = scripts.get(j);
                            if (baseSpriteScript != target) {
                                sumCodeLength -= baseSpriteScript.toCode().length() / 2;
                            } else {
                                sumCodeLength -= baseSpriteScript.toCode().length() / 2;
                                break;
                            }
                        }
                    }
                }

                script.args.put(ILabel.LABEL, String.format("%02X", (byte) sumCodeLength));
            }
        }
    }

    public static List<BaseSpriteScript> expand(ListScript listScript) {
        List<BaseSpriteScript> scripts = new ArrayList<>();
        boolean isDoBlock = listScript instanceof IDoBlock;
        if (!isDoBlock) {
            scripts.add(listScript);
        }

        for (BaseSpriteScript script : listScript.scripts) {
            if (script instanceof ListScript) {
                scripts.addAll(expand((ListScript) script));
            } else {
                scripts.add(script);
            }
        }
        if (isDoBlock) {
            scripts.add(listScript);
        }
        return scripts;
    }
}
