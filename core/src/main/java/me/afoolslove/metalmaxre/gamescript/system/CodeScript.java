package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * 直接使用剧情代码，但这可能会造成不可预计的问题
 */
public class CodeScript extends BaseSpriteScript {
    public String codes;

    public CodeScript(List<TerminalNode> codes) {
        super("");
        StringBuilder builder = new StringBuilder();
        for (TerminalNode code : codes) {
            builder.append(String.format("%02X", BaseSpriteScript.byteVal(code)));
        }
        this.codes = builder.toString();
    }

    public CodeScript(String codes) {
        super("");
        this.codes = codes;
    }

    public CodeScript(byte[] codes) {
        super("");
        this.codes = NumberR.toPlainHexString(codes);
    }

    @Override
    public String toCode() {
        return this.codes;
    }

    @Override
    public String toScript() {
        StringBuilder sb = new StringBuilder("#code");
        for (int i = 0; i < codes.length(); i += 2) {
            sb.append(String.format(" 0x%s", codes.substring(i, i + 2)));
        }
        return sb.toString();
    }
}
