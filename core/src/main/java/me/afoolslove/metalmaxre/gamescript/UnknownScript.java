package me.afoolslove.metalmaxre.gamescript;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * 未知的脚本
 */
public class UnknownScript extends BaseSpriteScript {
    public List<TerminalNode> unknowns;

    public UnknownScript(List<TerminalNode> list) {
        super("");
        this.unknowns = list;
    }
}
