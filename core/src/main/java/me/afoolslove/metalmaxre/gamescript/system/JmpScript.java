package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import me.afoolslove.metalmaxre.gamescript.i.ILabel;
import me.afoolslove.metalmaxre.gamescript.i.IPreviewCode;

public class JmpScript extends BaseSpriteScript implements IPreviewCode, ILabel {
    public String targetLabel;

    public JmpScript(String targetLabel) {
        super("18");
        this.targetLabel = targetLabel;
    }

    @Override
    public String getTargetLabel() {
        return targetLabel;
    }

    @Override
    public String toCode() {
        if (isEmptyArgs()) {
            return previewCode();
        }
        return op + args.get(LABEL);
    }

    @Override
    public String previewCode() {
        return op + "00";
    }

    @Override
    public String toScript() {
        return "#jmp " + targetLabel;
    }
}
