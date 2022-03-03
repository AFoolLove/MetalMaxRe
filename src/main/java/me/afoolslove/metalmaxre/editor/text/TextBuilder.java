package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.editor.text.option.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class TextBuilder {
    private final TextRaw texts = new TextRaw();

    public TextBuilder add(IBaseText text) {
        if (text != null) {
            texts.add(text);
        }
        return this;
    }

    public TextBuilder text(TextImpl text) {
        return add(text);
    }

    public TextBuilder text(String text) {
        if (text != null) {
            texts.add(new TextImpl(text));
        }
        return this;
    }

    public TextBuilder text(@NotNull char[] text) {
        texts.add(new TextImpl(text));
        return this;
    }

    public TextBuilder text(@NotNull byte[] source) {
        texts.add(new TextImpl(source));
        return this;
    }

    public TextBuilder waitForTime(WaitTime time) {
        return add(time);
    }

    public TextBuilder waitForTime(byte time) {
        texts.add(new WaitTime(time));
        return this;
    }

    public TextBuilder waitForTime(@Range(from = 0x00, to = 0xFF) int time) {
        texts.add(new WaitTime(time));
        return this;
    }

    public TextBuilder yesAndNo(YesAndNo yesAndNo) {
        return add(yesAndNo);
    }

    public TextBuilder yesAndNo(byte yes, byte no) {
        texts.add(new YesAndNo(yes, no));
        return this;
    }

    public TextBuilder yesAndNo(@Range(from = 0x00, to = 0xFF) int yes,
                                @Range(from = 0x00, to = 0xFF) int no) {
        texts.add(new YesAndNo(yes, no));
        return this;
    }

    public TextBuilder yesAndNo(YesOrNo yesOrNo) {
        return add(yesOrNo);
    }

    public TextBuilder yesOrNo(byte yes) {
        texts.add(new YesOrNo(yes));
        return this;
    }

    public TextBuilder yesOrNo(@Range(from = 0x00, to = 0xFF) int yes) {
        texts.add(new YesOrNo(yes));
        return this;
    }

    public TextBuilder textOffset(TextOffsetImpl textOffset) {
        return add(textOffset);
    }

    public TextBuilder textOffset(int offset) {
        for (int i = offset / 0xFF; i > 0; i--) {
            texts.add(new TextOffsetImpl(0xFF));
        }
        if (offset > 0x100) {
            texts.add(new TextOffsetImpl(offset & 0xFF));
        }
        return this;
    }

    public TextBuilder textSpeed(TextSpeed textSpeed) {
        return add(textSpeed);
    }

    public TextBuilder textSpeed(byte speed) {
        texts.add(new TextSpeed(speed));
        return this;
    }

    public TextBuilder textSpeed(@Range(from = 0x00, to = 0xFF) int speed) {
        texts.add(new TextSpeed(speed));
        return this;
    }

//    public TextBuilder spriteAction(SpriteAction spriteAction) {
//        return add(spriteAction);
//    }

    public TextBuilder rawText(RawText rawText) {
        return add(rawText);
    }

//    public TextBuilder textAction(TextAction textAction) {
//        return add(textAction);
//    }

//    public TextBuilder newLine() {
//        texts.add(TextAction.NEW_LINE);
//        return this;
//    }

//    public TextBuilder waitForKey() {
//        texts.add(TextAction.WAIT_FOR_KEY);
//        return this;
//    }



}
